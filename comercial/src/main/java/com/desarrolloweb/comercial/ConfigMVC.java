package com.desarrolloweb.comercial;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigMVC implements WebMvcConfigurer{

    @Value("${uploads.path}")
	private String uploadsExtDir;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // String rutaAbsUploads = Paths.get("uploads").toAbsolutePath().toUri().toString();
        String rutaAbsUploads = Paths.get(uploadsExtDir).toUri().toString();
        logger.info("*** Ruta absoluta de 'uploads' " + rutaAbsUploads + " en " + getClass());
        registry.addResourceHandler("/uploads/**").addResourceLocations(rutaAbsUploads);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("error/error_403");
        registry.addViewController("/").setViewName("inicio");
    }

    
    
}
