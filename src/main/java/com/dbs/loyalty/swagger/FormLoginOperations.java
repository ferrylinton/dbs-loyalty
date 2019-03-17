package com.dbs.loyalty.swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.model.Login;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

import springfox.documentation.builders.ApiDescriptionBuilder;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

@Primary
@Component
public class FormLoginOperations extends ApiListingScanner {

	private TypeResolver typeResolver;

	public FormLoginOperations(TypeResolver typeResolver, ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
		super(apiDescriptionReader, apiModelReader, pluginsManager);
		this.typeResolver = typeResolver;
	}

	@Override
	public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
		final Multimap<String, ApiListing> def = super.scan(context);
		final List<ApiDescription> apis = new LinkedList<>();
		final List<Operation> operations = new ArrayList<>();
		
		operations.add(
				new OperationBuilder(new CachingOperationNameGenerator())
				.method(HttpMethod.POST)
				.uniqueId("authentication")
				.tags(new HashSet<>(Arrays.asList(("authentication"))))
				.codegenMethodNameStem("loginPost")
				.position(0)
				.parameters(Arrays.asList(
						new ParameterBuilder()
							.name("login")
							.parameterType("body")
							.type(typeResolver.resolve(Login.class))
							.modelRef(new ModelRef("Login"))
							.build()))
				.responseModel(new ModelRef("String"))
				.responseMessages(responseMessages())
				.summary("Log in") //
				.notes("Here you can log in")
				.build()
				);

		Ordering<Operation> operationOrdering = new Ordering<Operation>() {
            @Override
            public int compare(Operation left, Operation right) {
                return left.getMethod().name().compareTo(right.getMethod().name());
            }
        };
        
		apis.add(new ApiDescriptionBuilder(operationOrdering)
						.path("/api/authenticate")
						.description("JWT Authentication")
						.operations(operations)
						.hidden(false)
						.build());
//		apis.add(new ApiDescription("/api/login/", "Authentication documentation", operations, false));

		def.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
				.apis(apis).description("Custom authentication").build());

		return def;
	}
	
	private Set<ResponseMessage> responseMessages() {
		Set<ResponseMessage> messages = new HashSet<>();
		messages.add(new ResponseMessageBuilder()
                .code(200)
                .responseModel(new ModelRef("String"))
                .build());
		
        return messages;
    }
	
}
