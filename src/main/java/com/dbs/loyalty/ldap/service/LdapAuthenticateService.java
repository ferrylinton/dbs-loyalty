package com.dbs.loyalty.ldap.service;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.AuthenticationLdapProperties;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.exception.LdapConnectException;
import com.dbs.loyalty.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LdapAuthenticateService {
	
	private String LDAP_CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	private String SIMPLE = "simple";
	
	private String NONE = "none";
	
	private Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([09af]{3,4}).*");

	private static final int PASSWORD_EXPIRED = 0x532;
	
	private static final int ACCOUNT_DISABLED = 0x533;
	
	private static final int ACCOUNT_EXPIRED = 0x701;

	private static final int ACCOUNT_LOCKED = 0x775;

	private DirContext searchContext = null;
	
	private final LdapProperties ldapProperties;
    
    private final AuthenticationLdapProperties applicationLdapProperties;
	
	public String getDn(String username) throws NamingException {
		SearchControls searchCtrls = new SearchControls();
		searchCtrls.setReturningAttributes(new String[] {});
		searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		if(searchContext == null) {
			searchContext = new InitialDirContext(getDnEnv());
		}
		
		NamingEnumeration<SearchResult> answer = searchContext.search(ldapProperties.getBase(), applicationLdapProperties.getAttribute() + "=" + username, searchCtrls);
		
		if (answer.hasMore()) {
			return answer.next().getNameInNamespace();
		}
		
		return Constant.EMPTY;
	}
	
	public boolean authenticate(String username, String password) {
		DirContext ctx = null;
		boolean authenticated = false;

		try {
			ctx = new InitialDirContext(getEnv(username, password));
			authenticated = true;
		} catch (NamingException e) {
			log.error(e.getLocalizedMessage(), e);
			if ((e instanceof AuthenticationException) || (e instanceof OperationNotSupportedException)) {
				handleBindException(username, e);
				authenticated = false;
			}else if(e instanceof CommunicationException) {
				throw new LdapConnectException(MessageService.getMessage("message.ldapConnectException"));
			}else {
				throw new RuntimeException(e);
			}
		}finally {
			closeContext(ctx);
		}

		return authenticated;
	}
	
	private Hashtable<String, Object> getDnEnv(){
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CTX_FACTORY);
		env.put(Context.PROVIDER_URL, ldapProperties.getUrls()[0]);
		
		if(ldapProperties.getUsername() != null && ldapProperties.getPassword() != null) {
			env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
			env.put(Context.SECURITY_PRINCIPAL, ldapProperties.getUsername());
			env.put(Context.SECURITY_CREDENTIALS, ldapProperties.getPassword());
		}else {
			env.put(Context.SECURITY_AUTHENTICATION, NONE);
		}
		
		return env;
	}
	
	private Hashtable<String, Object> getEnv(String username, String password) throws NamingException{
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CTX_FACTORY);
		env.put(Context.PROVIDER_URL, ldapProperties.getUrls()[0]);
		env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
		env.put(Context.SECURITY_PRINCIPAL, getDn(username));
		env.put(Context.SECURITY_CREDENTIALS, password);
		
		return env;
	}
	
	private void handleBindException(String bindPrincipal, NamingException exception) {
		handleResolveObj(exception);

		int subErrorCode = parseSubErrorCode(exception.getMessage());

		if (subErrorCode <= 0) {
			return;
		}

		raiseExceptionForErrorCode(subErrorCode, exception);
	}

	private void handleResolveObj(NamingException exception) {
		Object resolvedObj = exception.getResolvedObj();
		boolean serializable = resolvedObj instanceof Serializable;
		if (resolvedObj != null && !serializable) {
			exception.setResolvedObj(null);
		}
	}
	
	private int parseSubErrorCode(String message) {
		Matcher m = SUB_ERROR_CODE.matcher(message);

		if (m.matches()) {
			return Integer.parseInt(m.group(1), 16);
		}

		return 1;
	}
	
	private void raiseExceptionForErrorCode(int code, NamingException exception) {
		String hexString = Integer.toHexString(code);
		Throwable cause = new ActiveDirectoryAuthenticationException(hexString, exception.getMessage(), exception);
		switch (code) {
		case PASSWORD_EXPIRED:
			throw new CredentialsExpiredException(MessageService.getMessage(
					"LdapAuthenticationProvider.credentialsExpired",
					"User credentials have expired"), cause);
		case ACCOUNT_DISABLED:
			throw new DisabledException(MessageService.getMessage(
					"LdapAuthenticationProvider.disabled", "User is disabled"), cause);
		case ACCOUNT_EXPIRED:
			throw new AccountExpiredException(MessageService.getMessage(
					"LdapAuthenticationProvider.expired", "User account has expired"),
					cause);
		case ACCOUNT_LOCKED:
			throw new LockedException(MessageService.getMessage(
					"LdapAuthenticationProvider.locked", "User account is locked"), cause);
		default:
			throw badCredentials(cause);
		}
	}
	
	private BadCredentialsException badCredentials() {
		return new BadCredentialsException("Bad credentials");
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		return (BadCredentialsException) badCredentials().initCause(cause);
	}
	
	/**
	 * Close the given JNDI Context and ignore any thrown exception. This is
	 * useful for typical <code>finally</code> blocks in JNDI code.
	 * 
	 * @param context the JNDI Context to close (may be <code>null</code>)
	 */
	private void closeContext(DirContext context) {
		if (context != null) {
			try {
				context.close();
			}catch (NamingException ex) {
				log.debug("Could not close JNDI DirContext", ex);
			}catch (Throwable ex) {
				log.debug("Unexpected exception on closing JNDI DirContext", ex);
			}
		}
	}
	
}
