package com.zinnaworks.deploy.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zinnaworks.deploy.Application;
import com.zinnaworks.deploy.entity.ProjectConfig;

@EnableConfigurationProperties({ProjectConfig.class})
@Controller
@EnableAsync 
@SpringBootApplication(scanBasePackageClasses = Application.class)
public class ApplicationContextConfig {

    @RequestMapping("/")
    public String home() {
        return "redirect:/deploy/smd";
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/webapp/static/pages/error/401.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/webapp/static/pages/error/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/webapp/static/pages/error/500.html");

            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationContextConfig.class, args);
    }
}
