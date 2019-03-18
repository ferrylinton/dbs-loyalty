package com.dbs.loyalty.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;

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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	private static final ModelRef ERROR = new ModelRef("Error");
	
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
	            .forCodeGeneration(true)
	            .genericModelSubstitutes(ResponseEntity.class)
	            .securityContexts(Lists.newArrayList(securityContext()))
	            .securitySchemes(Lists.newArrayList(apiKey()))
	            .globalResponseMessage(RequestMethod.GET, getResponseMessages())
	            .tags(
	            	new Tag(TagConstant.Authentication, Constant.EMPTY, 0),
	            	new Tag(TagConstant.Customer, Constant.EMPTY, 1),
	            	new Tag(TagConstant.Promo, Constant.EMPTY, 2),
	            	new Tag(TagConstant.PromoCategory, Constant.EMPTY, 3),
	            	new Tag(TagConstant.Reward, Constant.EMPTY, 4)
	            )
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.dbs.loyalty.web.controller.rest"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false);
	}

	private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
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
    	return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(500)
                        .message("Server error")
                        .responseModel(ERROR)
                        .build(),
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Bad request – wrong usage of the API")
                        .responseModel(ERROR)
                        .build(),
                new ResponseMessageBuilder()
                        .code(401)
                        .message("No or invalid authentication")
                        .responseModel(ERROR)
                        .build(),
                new ResponseMessageBuilder()
                        .code(403)
                        .message("Not permitted to access for users role")
                        .responseModel(ERROR)
                        .build(),
                new ResponseMessageBuilder()
                        .code(404)
                        .message("Requested resource not available (anymore)")
                        .responseModel(ERROR)
                        .build()
        );
    }

}
