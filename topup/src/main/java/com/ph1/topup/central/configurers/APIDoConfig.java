package com.ph1.topup.central.configurers;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class APIDoConfig 
{
	@Value("${keycloak.auth-server-url}")
	private String AUTH_SERVER;

	@Value("${keycloak.credentials.secret}")
	private String CLIENT_SECRET;

	@Value("${keycloak.resource}")
	private String CLIENT_ID;

	@Value("${keycloak.realm}")
	private String REALM;

	private static final String OAUTH_NAME = "ph1-topup-central-client";
	private static final String ALLOWED_PATHS = "/.*";
	private static final String GROUP_NAME = "ph1-topup-central-client";
	private static final String TITLE = "PH1TopupCentral REST API";
	private static final String DESCRIPTION = "API Documentation for PH1TopupCentral REST API";
	private static final String VERSION = "1.0.0";

	@Bean
	public Docket api()
	{
		return new  Docket(DocumentationType.SWAGGER_2)
					.groupName(GROUP_NAME)
					.useDefaultResponseMessages(true)
					.apiInfo(apiInfo())
					.select()
				    .apis(RequestHandlerSelectors.basePackage("com.ph1.topup.central.application"))
				    .apis(Predicates.not(RequestHandlerSelectors.basePackage("com.ph1.topup.central.controllers")))
					.paths(regex(ALLOWED_PATHS))
					.build()
					.securitySchemes(Collections.singletonList(securityScheme()))
					.securityContexts(Collections.singletonList(securityContext()));
	}

	private ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()
				   .title(TITLE)
				   .description(DESCRIPTION)
				   .version(VERSION)
				   .build();
	}

	@Bean
	public SecurityConfiguration security()
	{
		return  SecurityConfigurationBuilder.builder()
				.realm(REALM)
				.clientId(CLIENT_ID)
				.clientSecret(CLIENT_SECRET)
				.appName(GROUP_NAME)
				.scopeSeparator(" ")
				.build();
	}

	private SecurityScheme securityScheme()
	{
		GrantType grantType = new AuthorizationCodeGrantBuilder()
								  .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/realms/" + REALM + "/protocol/openid-connect/token", GROUP_NAME))
								  .tokenRequestEndpoint(new TokenRequestEndpoint(AUTH_SERVER + "/realms/" + REALM + "/protocol/openid-connect/auth", CLIENT_ID, CLIENT_SECRET))
								  .build();

		return  new OAuthBuilder()
				.name(OAUTH_NAME)
				.grantTypes(Collections.singletonList(grantType))
				.scopes(Arrays.asList(scopes()))
				.build();
	}

	private AuthorizationScope[] scopes()
	{
		return  new AuthorizationScope[] {
				new AuthorizationScope("user", "for CRUD operations"),
				new AuthorizationScope("read", "for read operations"),
				new AuthorizationScope("write", "for write operations")
		};
	}

	private SecurityContext securityContext()
	{
		return  SecurityContext.builder()
				.securityReferences(Collections.singletonList(new SecurityReference(OAUTH_NAME, scopes())))
				.forPaths(regex(ALLOWED_PATHS))
				.build();
	}
	
}



