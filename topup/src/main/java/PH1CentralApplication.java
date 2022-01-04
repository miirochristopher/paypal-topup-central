import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@EnableRetry
@RefreshScope
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.ph1.topup.central"})
@EntityScan(basePackages = {"com.ph1.topup.central"})
@ComponentScan(basePackages = {"com.ph1.topup.central"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class PH1CentralApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(PH1CentralApplication.class, args);
	}
	
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	static class SecurityConfig
	{
		@Bean
		public WebSecurityConfigurerAdapter webSecurityConfigurer( @Value("${kc.realm}") String realm,
				KeycloakOauth2UserService keycloakOidcUserService, KeycloakLogoutHandler keycloakLogoutHandler
		)
		{
			return new WebSecurityConfigurerAdapter()
			{
				@Override
				public void configure(HttpSecurity http) throws Exception
				{
					http.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
							.and()
							.authorizeRequests()
							.anyRequest()
							.permitAll()
							.and()
							.logout()
							.addLogoutHandler(keycloakLogoutHandler)
							.and()
							.oauth2Login()
							.userInfoEndpoint()
							.oidcUserService(keycloakOidcUserService)
							.and()
							.loginPage(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + realm);

				}

			};

		}

		@Bean
		KeycloakOauth2UserService keycloakOidcUserService(OAuth2ClientProperties oauth2ClientProperties)
		{

			NimbusJwtDecoderJwkSupport jwtDecoder = new NimbusJwtDecoderJwkSupport(oauth2ClientProperties.getProvider().get("keycloak").getJwkSetUri());

			SimpleAuthorityMapper authoritiesMapper = new SimpleAuthorityMapper();
			authoritiesMapper.setConvertToUpperCase(true);

			return new KeycloakOauth2UserService(jwtDecoder, authoritiesMapper);
		}

		@Bean
		KeycloakLogoutHandler keycloakLogoutHandler()
		{
			return new KeycloakLogoutHandler(new RestTemplate());
		}

	}

	@RequiredArgsConstructor
	static class KeycloakOauth2UserService extends OidcUserService
	{

		private final OAuth2Error INVALID_REQUEST = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);

		private final JwtDecoder jwtDecoder;

		private final GrantedAuthoritiesMapper authoritiesMapper;

		/**
		 * Augments {@link OidcUserService#loadUser(OidcUserRequest)} to add authorities
		 * provided by Keycloak.
		 *
		 * Needed because {@link OidcUserService#loadUser(OidcUserRequest)} (currently)
		 * does not provide a hook for adding custom authorities from a
		 * {@link OidcUserRequest}.
		 */
		@Override
		public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException
		{

			OidcUser user = super.loadUser(userRequest);

			Set<GrantedAuthority> authorities = new LinkedHashSet<>();
			authorities.addAll(user.getAuthorities());
			authorities.addAll(extractKeycloakAuthorities(userRequest));

			return new DefaultOidcUser(authorities, userRequest.getIdToken(), user.getUserInfo(), "preferred_username");

		}

		/**
		 * Extracts {@link GrantedAuthority GrantedAuthorities} from the AccessToken in
		 * the {@link OidcUserRequest}.
		 *
		 * @param userRequest
		 * @return
		 */
		private Collection<? extends GrantedAuthority> extractKeycloakAuthorities(OidcUserRequest userRequest)
		{

			Jwt token = parseJwt(userRequest.getAccessToken().getTokenValue());

			@SuppressWarnings("unchecked")
			Map<String, Object> resourceMap = (Map<String, Object>) token.getClaims().get("resource_access");
			String clientId = userRequest.getClientRegistration().getClientId();

			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> clientResource = (Map<String, Map<String, Object>>) resourceMap.get(clientId);

			if (CollectionUtils.isEmpty(clientResource))
			{
				return Collections.emptyList();
			}

			@SuppressWarnings("unchecked")
			List<String> clientRoles = (List<String>) clientResource.get("roles");

			if (CollectionUtils.isEmpty(clientRoles))
			{
				return Collections.emptyList();
			}

			Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(clientRoles.toArray(new String[0]));
			if (authoritiesMapper == null) {
				return authorities;
			}

			return authoritiesMapper.mapAuthorities(authorities);
		}

		private Jwt parseJwt(String accessTokenValue)
		{

			try {
				// Token is already verified by spring security infrastructure
				return jwtDecoder.decode(accessTokenValue);
			} catch (JwtException e)
			{
				throw new OAuth2AuthenticationException(INVALID_REQUEST, e);
			}
		}
	}

	/**
	 * Propagates logouts to Keycloak.
	 *
	 * Necessary because Spring Security 5 (currently) doesn't support
	 * end-session-endpoints.
	 */
	@Slf4j
	@RequiredArgsConstructor
	static class KeycloakLogoutHandler extends SecurityContextLogoutHandler
	{

		private final RestTemplate restTemplate;

		@Override
		public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		{
			super.logout(request, response, authentication);

			propagateLogoutToKeycloak((OidcUser) authentication.getPrincipal());
		}

		private void propagateLogoutToKeycloak(OidcUser user)
		{

			String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(endSessionEndpoint)
					.queryParam("id_token_hint", user.getIdToken().getTokenValue());

			ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);

			if (logoutResponse.getStatusCode().is2xxSuccessful())
			{
				log.info("Successfully logged out!!");
			}
			else {
				log.info("Could not propagate logout!!");
			}

		}

	}

}
