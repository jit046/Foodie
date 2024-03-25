package com.example.todo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class OpenApiConfiguration {
	
	@Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    private String tenantId;

    @Bean
    OpenAPI customOpenAPI() {
        OAuthFlow authorizationCodeFlow = new OAuthFlow();
        authorizationCodeFlow.setAuthorizationUrl(String.format("https://login.microsoftonline.com/%s/oauth2/v2.0/authorize", tenantId));
        authorizationCodeFlow.setRefreshUrl(String.format("https://login.microsoftonline.com/%s/oauth2/v2.0/token", tenantId));
        authorizationCodeFlow.setTokenUrl(String.format("https://login.microsoftonline.com/%s/oauth2/v2.0/token", tenantId));
        authorizationCodeFlow.setScopes(new Scopes()
        		.addString("api://tims-todo/Todo.User", "Access todo as a user")
        		.addString("api://tims-todo/Todo.Admin", "Access todo as an admin"));
        OAuthFlows oauthFlows = new OAuthFlows();
        oauthFlows.authorizationCode(authorizationCodeFlow);
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        securityScheme.setFlows(oauthFlows);
        return new OpenAPI()
            .info(new Info().title("RESTful APIs for SimpleTodo"))
            .components(new Components().addSecuritySchemes("Microsoft Entra ID", securityScheme));
    }
}