package com.dbs.loyalty.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorResponse;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;

import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RequiredArgsConstructor
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	private ModelRef error = new ModelRef("ErrorResponse");
	
	private ResponseMessage ok = new ResponseMessageBuilder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.name())
            .responseModel(error)
            .build();
	
	private ResponseMessage internalServerError = new ResponseMessageBuilder()
    		.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
    		.message(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .responseModel(error)
            .build();
	
	private ResponseMessage unauthorized = new ResponseMessageBuilder()
		    .code(HttpStatus.UNAUTHORIZED.value())
		    .message(HttpStatus.UNAUTHORIZED.name())
		    .responseModel(error)
		    .build();
	
	private ResponseMessage forbidden = new ResponseMessageBuilder()
            .code(HttpStatus.FORBIDDEN.value())
            .message(HttpStatus.FORBIDDEN.name())
            .responseModel(error)
            .build();
	
	private ResponseMessage notFound = new ResponseMessageBuilder()
            .code(HttpStatus.NOT_FOUND.value())
            .message(HttpStatus.NOT_FOUND.name())
            .responseModel(error)
            .build();
	
	private ResponseMessage badRequest = new ResponseMessageBuilder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(HttpStatus.BAD_REQUEST.name())
            .responseModel(error)
            .build();
	
	private final TypeResolver typeResolver;
	
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
	            .forCodeGeneration(true)
	            .genericModelSubstitutes(ResponseEntity.class)
	            .securityContexts(Lists.newArrayList(securityContext()))
	            .securitySchemes(Lists.newArrayList(apiKey()))
	            .globalResponseMessage(RequestMethod.GET, getResponseMessages())
	            .globalResponseMessage(RequestMethod.POST, postResponseMessages())
	            .globalResponseMessage(RequestMethod.PUT, putResponseMessages())
	            .globalResponseMessage(RequestMethod.DELETE, deleteResponseMessages())
	            .additionalModels(typeResolver.resolve(ErrorResponse.class))
	            .tags(
	            	new Tag(SwaggerConstant.AUTHENTICATION, Constant.EMPTY, 0),
	            	new Tag(SwaggerConstant.CUSTOMER, Constant.EMPTY, 1),
	            	new Tag(SwaggerConstant.PROMO_CATEGORY, Constant.EMPTY, 2),
	            	new Tag(SwaggerConstant.PROMO, Constant.EMPTY, 3),
	            	new Tag(SwaggerConstant.LOVED_ONE, Constant.EMPTY, 4),
	            	new Tag(SwaggerConstant.REWARD, Constant.EMPTY, 5)
	            )
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.dbs.loyalty.web.controller.rest"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false);
	}

	private ApiKey apiKey() {
        return new ApiKey(SwaggerConstant.JWT, HttpHeaders.AUTHORIZATION, In.HEADER.name());
    }

	@Bean
    public SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder().scopeSeparator(",")
	        .additionalQueryStringParams(null)
	        .useBasicAuthenticationWithAccessCodeGrant(false).build();
    }
	
    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/api/*"))
            .build();
    }
    
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { authorizationScope };
        return Lists.newArrayList(new SecurityReference(SwaggerConstant.JWT, authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("DBS Loyalty", "API To Access DBS Loyalty", "0.0.1", null, null, null, null, Collections.emptyList());
    }
    
    private List<ResponseMessage> getResponseMessages() {
    	return Arrays.asList(ok, internalServerError, unauthorized, forbidden, notFound);
    }
    
    private List<ResponseMessage> postResponseMessages() {
    	return Arrays.asList(ok, badRequest, internalServerError, notFound, unauthorized);
    }

    private List<ResponseMessage> putResponseMessages() {
    	return Arrays.asList(ok, internalServerError, badRequest, unauthorized, forbidden, notFound);
    }
    
    private List<ResponseMessage> deleteResponseMessages() {
    	return Arrays.asList(ok, internalServerError, unauthorized, forbidden, notFound);
    }
    
}
