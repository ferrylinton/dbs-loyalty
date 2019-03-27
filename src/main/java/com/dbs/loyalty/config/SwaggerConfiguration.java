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

	private ModelRef ERROR = new ModelRef("ErrorResponse");
	
	private ResponseMessage OK = new ResponseMessageBuilder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.name())
            .responseModel(ERROR)
            .build();
	
	private ResponseMessage INTERNAL_SERVER_ERROR = new ResponseMessageBuilder()
    		.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
    		.message(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .responseModel(ERROR)
            .build();
	
	private ResponseMessage UNAUTHORIZED = new ResponseMessageBuilder()
		    .code(HttpStatus.UNAUTHORIZED.value())
		    .message(HttpStatus.UNAUTHORIZED.name())
		    .responseModel(ERROR)
		    .build();
	
	private ResponseMessage FORBIDDEN = new ResponseMessageBuilder()
            .code(HttpStatus.FORBIDDEN.value())
            .message(HttpStatus.FORBIDDEN.name())
            .responseModel(ERROR)
            .build();
	
	private ResponseMessage NOT_FOUND = new ResponseMessageBuilder()
            .code(HttpStatus.NOT_FOUND.value())
            .message(HttpStatus.NOT_FOUND.name())
            .responseModel(ERROR)
            .build();
	
	private ResponseMessage BAD_REQUEST = new ResponseMessageBuilder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(HttpStatus.BAD_REQUEST.name())
            .responseModel(ERROR)
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
	            .globalResponseMessage(RequestMethod.GET, GETResponseMessages())
	            .globalResponseMessage(RequestMethod.POST, POSTResponseMessages())
	            .globalResponseMessage(RequestMethod.PUT, PUTResponseMessages())
	            .globalResponseMessage(RequestMethod.DELETE, DELETEResponseMessages())
	            .additionalModels(typeResolver.resolve(ErrorResponse.class))
	            .tags(
	            	new Tag(SwaggerConstant.Authentication, Constant.EMPTY, 0),
	            	new Tag(SwaggerConstant.Customer, Constant.EMPTY, 1),
	            	new Tag(SwaggerConstant.PromoCategory, Constant.EMPTY, 2),
	            	new Tag(SwaggerConstant.Promo, Constant.EMPTY, 3),
	            	new Tag(SwaggerConstant.Reward, Constant.EMPTY, 4)
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
    
    private List<ResponseMessage> GETResponseMessages() {
    	return Arrays.asList(OK, INTERNAL_SERVER_ERROR, UNAUTHORIZED, FORBIDDEN, NOT_FOUND);
    }
    
    private List<ResponseMessage> POSTResponseMessages() {
    	return Arrays.asList(OK, BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, UNAUTHORIZED);
    }

    private List<ResponseMessage> PUTResponseMessages() {
    	return Arrays.asList(OK, INTERNAL_SERVER_ERROR, BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND);
    }
    
    private List<ResponseMessage> DELETEResponseMessages() {
    	return Arrays.asList(OK, INTERNAL_SERVER_ERROR, UNAUTHORIZED, FORBIDDEN, NOT_FOUND);
    }
    
}
