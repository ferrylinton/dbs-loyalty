package com.dbs.loyalty.ldap;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.config.LdapProperties;
import com.dbs.loyalty.service.MessageService;

@Service
public class LdapService {

	private final Logger LOG = LoggerFactory.getLogger(LdapService.class);
			
	private static final String LDAP_CTX_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	private static final String SIMPLE = "simple";
	
	private static final String NONE = "none";
	
	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([0-9a-f]{3,4}).*");

	private static final int PASSWORD_EXPIRED = 0x532;
	
	private static final int ACCOUNT_DISABLED = 0x533;
	
	private static final int ACCOUNT_EXPIRED = 0x701;

	private static final int ACCOUNT_LOCKED = 0x775;
	
	private LdapProperties ldapProperties;

	private DirContext searchContext = null;
	
	public LdapService(LdapProperties ldapProperties) {
		this.ldapProperties = ldapProperties;
	}
	
	public String getDn(String username) throws NamingException {
		SearchControls searchCtrls = new SearchControls();
		searchCtrls.setReturningAttributes(new String[] {});
		searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		if(searchContext == null) {
			searchContext = new InitialDirContext(getDnEnv());
		}
		
		NamingEnumeration<SearchResult> answer = searchContext.search(ldapProperties.getBase(), String.format(ldapProperties.getSearchFilter(username)), searchCtrls);
		
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
			LOG.error(e.getLocalizedMessage(), e);
			if ((e instanceof AuthenticationException) || (e instanceof OperationNotSupportedException)) {
				handleBindException(username, e);
				throw badCredentials(e);
			}else {
				throw new RuntimeException(e);
			}
		}finally {
			closeContext(ctx);
		}
		
		System.out.println("authenticated : " + authenticated);
		return authenticated;
	}
	
	private Hashtable<String, Object> getDnEnv(){
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CTX_FACTORY);
		env.put(Context.PROVIDER_URL, ldapProperties.getUrl());
		
		if(ldapProperties.getPrincipal() != null && ldapProperties.getCredentials() != null) {
			System.out.println(SIMPLE);
			env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
			env.put(Context.SECURITY_PRINCIPAL, ldapProperties.getPrincipal());
			env.put(Context.SECURITY_CREDENTIALS, ldapProperties.getCredentials());
		}else {
			System.out.println(NONE);
			env.put(Context.SECURITY_AUTHENTICATION, NONE);
		}
		
		return env;
	}
	
	private Hashtable<String, Object> getEnv(String username, String password) throws NamingException{
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CTX_FACTORY);
		env.put(Context.PROVIDER_URL, ldapProperties.getUrl());
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

		return -1;
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
				LOG.debug("Could not close JNDI DirContext", ex);
			}catch (Throwable ex) {
				LOG.debug("Unexpected exception on closing JNDI DirContext", ex);
			}
		}
	}
	
}
