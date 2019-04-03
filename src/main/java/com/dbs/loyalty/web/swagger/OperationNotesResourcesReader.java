package com.dbs.loyalty.web.swagger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.apache.commons.codec.Charsets;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import com.google.common.io.Resources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@RequiredArgsConstructor
@Slf4j
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class OperationNotesResourcesReader implements OperationBuilderPlugin {
    
	private final DescriptionResolver descriptions;
   
    @Override
    public void apply(OperationContext context) {
    	Optional<ApiNotes> methodAnnotation = context.findAnnotation(ApiNotes.class).toJavaUtil();
    	
        if (methodAnnotation.isPresent() && StringUtils.hasText(methodAnnotation.get().value())) {
 
            final String mdFile = methodAnnotation.get().value();
            URL url = Resources.getResource(mdFile);
            String text;
            
            try {
                text = Resources.toString(url, Charsets.UTF_8);
            } catch (IOException e) {
                log.error("Error while reading markdown description file {}", mdFile, e);
                text = "Markdown file " + mdFile + " not loaded";
            }
 
            context.operationBuilder().notes(descriptions.resolve(text));
        }
    }
 
    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
