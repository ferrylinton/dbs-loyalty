package com.dbs.loyalty.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorDataApi;
import com.dbs.loyalty.web.response.ErrorResponse;
import com.dbs.loyalty.web.response.UnauthorizedResponse;
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

	private ModelRef errorResponse = new ModelRef("ErrorResponse");
	
	private ModelRef unauthorizedResponse = new ModelRef("UnauthorizedResponse");
	
	private ModelRef errorDataApi = new ModelRef("ErrorDataApi");

	private ResponseMessage internalServerError = new ResponseMessageBuilder()
    		.code(500)
    		.message("Internal Server Error")
            .responseModel(errorDataApi)
            .build();
	
	private ResponseMessage notFound = new ResponseMessageBuilder()
    		.code(404)
    		.message("Not Found")
            .responseModel(errorDataApi)
            .build();
	
	private ResponseMessage unauthorized = new ResponseMessageBuilder()
		    .code(401)
		    .message("Unauthorized")
		    .responseModel(unauthorizedResponse)
		    .build();
	
	private ResponseMessage badRequest = new ResponseMessageBuilder()
            .code(400)
            .message("Bad Request")
            .responseModel(errorResponse)
            .build();
	
	private final TypeResolver typeResolver;
	
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(ZonedDateTime.class, String.class)
				.apiInfo(apiInfo())
	            .forCodeGeneration(true)
	            .genericModelSubstitutes(ResponseEntity.class)
	            .securityContexts(Lists.newArrayList(securityContext()))
	            .securitySchemes(Lists.newArrayList(apiKey()))
	            .globalResponseMessage(RequestMethod.GET, getResponseMessages())
	            .globalResponseMessage(RequestMethod.POST, postResponseMessages())
	            .globalResponseMessage(RequestMethod.PUT, putResponseMessages())
	            .globalResponseMessage(RequestMethod.DELETE, deleteResponseMessages())
	            .additionalModels(
	            		typeResolver.resolve(ErrorResponse.class), 
	            		typeResolver.resolve(UnauthorizedResponse.class),
	            		typeResolver.resolve(ErrorDataApi.class))
	            .tags(
	            	new Tag(SwaggerConstant.ADDRESS, Constant.EMPTY, 1),
	            	new Tag(SwaggerConstant.AIRPORT_ASSISTANCE, Constant.EMPTY, 2),
	            	new Tag(SwaggerConstant.AUTHENTICATION, Constant.EMPTY, 3),
	            	new Tag(SwaggerConstant.CUSTOMER, Constant.EMPTY, 4),
	            	new Tag(SwaggerConstant.EVENT, Constant.EMPTY, 5),
	            	new Tag(SwaggerConstant.FEEDBACK, Constant.EMPTY, 6),
	            	new Tag(SwaggerConstant.LOVED_ONE, Constant.EMPTY, 7),
	            	new Tag(SwaggerConstant.ORDER, Constant.EMPTY, 8),
	            	new Tag(SwaggerConstant.PRODUCT, Constant.EMPTY, 9),
	            	new Tag(SwaggerConstant.PROMO, Constant.EMPTY, 10),
	            	new Tag(SwaggerConstant.PROMO_CATEGORY, Constant.EMPTY, 11),
	            	new Tag(SwaggerConstant.REWARD, Constant.EMPTY, 12),
	            	new Tag(SwaggerConstant.VERIFICATION_TOKEN, Constant.EMPTY, 13),
	            	new Tag(SwaggerConstant.WELLNESS, Constant.EMPTY, 14),
	            	new Tag(SwaggerConstant.TADA, Constant.EMPTY, 15)
	            )
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.dbs.loyalty.web.controller.rest"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false);
	}

	private ApiKey apiKey() {
        return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, In.HEADER.name());
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
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("DBS Loyalty", "API To Access DBS Loyalty", "0.0.1", null, null, null, null, Collections.emptyList());
    }
    
    private List<ResponseMessage> getResponseMessages() {
    	return Arrays.asList(internalServerError, unauthorized, notFound);
    }
    
    private List<ResponseMessage> postResponseMessages() {
    	return Arrays.asList(internalServerError, unauthorized, notFound, badRequest);
    }

    private List<ResponseMessage> putResponseMessages() {
    	return Arrays.asList(internalServerError, unauthorized, notFound, badRequest);
    }
    
    private List<ResponseMessage> deleteResponseMessages() {
    	return Arrays.asList(internalServerError, unauthorized, notFound);
    }
    
}
