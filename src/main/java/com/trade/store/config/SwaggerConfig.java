package com.trade.store.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 *
	 *
	 * @return the ApiInfo
	 */
	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("TradeStore API").description("APIs for handling Trade Management.")
				.license("").licenseUrl("http://unlicense.org")
				.termsOfServiceUrl("https://www.TradeStore.com/en/legal-information").version("1.0")
				.contact(new Contact("TradeStore Services", "https://www.TradeStore.com/en", "")).build();
	}

	/**
	 * Configurs the Swagger documentation for WorkQueueManagement API Application.
	 *
	 * @return the Docket
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(EnableSwaggerDocumentation.class))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	/**
	 * Web endpoint servlet handler mapping for actuator path configuration.
	 *
	 * @param webEndpointsSupplier        : WebEndpointsSupplier
	 * @param servletEndpointsSupplier    : ServletEndpointsSupplier
	 * @param controllerEndpointsSupplier : ControllerEndpointsSupplier
	 * @param endpointMediaTypes          : EndpointMediaTypes
	 * @param corsProperties              : CorsEndpointProperties
	 * @param webEndpointProperties       : WebEndpointProperties
	 * @param environment                 : Environment
	 * @return the WebMvcEndpointHandlerMapping
	 */
	@Bean
	public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
			ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
			EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
			WebEndpointProperties webEndpointProperties, Environment environment) {
		List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
		Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
		allEndpoints.addAll(webEndpoints);
		allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
		allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
		String basePath = webEndpointProperties.getBasePath();
		EndpointMapping endpointMapping = new EndpointMapping(basePath);
		boolean registerLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
		return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
				corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
				registerLinksMapping, null);
	}

	/**
	 * Should register links mapping.
	 *
	 * @param webProps : WebEndpointProperties
	 * @param env      : Environment
	 * @param basePath : String
	 * @return true/false, if successful/failed.
	 */
	private boolean shouldRegisterLinksMapping(WebEndpointProperties webProps, Environment env, String basePath) {
		return webProps.getDiscovery().isEnabled()
				&& (StringUtils.hasText(basePath) || ManagementPortType.get(env).equals(ManagementPortType.DIFFERENT));
	}

}
