package com.ph1.topup.central.configurers;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigResolver
{
	@Bean
	public KeycloakSpringBootConfigResolver KeycloakConfigResolver()
	{
		return new KeycloakSpringBootConfigResolver();
	}

}
