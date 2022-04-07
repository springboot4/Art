// package com.fxz.system.configure;
//
// import com.fxz.system.properties.FxzServerSystemProperties;
// import com.fxz.system.properties.FxzSwaggerProperties;
// import lombok.RequiredArgsConstructor;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import springfox.documentation.builders.OAuthBuilder;
// import springfox.documentation.builders.PathSelectors;
// import springfox.documentation.builders.RequestHandlerSelectors;
// import springfox.documentation.service.*;
// import springfox.documentation.spi.DocumentationType;
// import springfox.documentation.spi.service.contexts.SecurityContext;
// import springfox.documentation.spring.web.plugins.Docket;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
// import java.util.Arrays;
// import java.util.Collections;
//
/// **
// * @author fxz
// */
// @EnableSwagger2
// @Configuration
// @RequiredArgsConstructor
// public class FxzWebConfigure {
//
// private final FxzServerSystemProperties properties;
//
// @Bean
// public Docket swaggerApi() {
// FxzSwaggerProperties swagger = properties.getSwagger();
// return new Docket(DocumentationType.SWAGGER_2).select()
// .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage())).paths(PathSelectors.any()).build()
// .apiInfo(apiInfo(swagger)).securitySchemes(Collections.singletonList(securityScheme(swagger)))
// .securityContexts(Collections.singletonList(securityContext(swagger)));
// }
//
// private ApiInfo apiInfo(FxzSwaggerProperties swagger) {
// return new ApiInfo(swagger.getTitle(), swagger.getDescription(), swagger.getVersion(),
// null,
// new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
// swagger.getLicense(),
// swagger.getLicenseUrl(), Collections.emptyList());
// }
//
// private SecurityScheme securityScheme(FxzSwaggerProperties swagger) {
// GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(swagger.getGrantUrl());
//
// return new
// OAuthBuilder().name(swagger.getName()).grantTypes(Collections.singletonList(grantType))
// .scopes(Arrays.asList(scopes(swagger))).build();
// }
//
// private SecurityContext securityContext(FxzSwaggerProperties swagger) {
// return SecurityContext.builder()
// .securityReferences(
// Collections.singletonList(new SecurityReference(swagger.getName(), scopes(swagger))))
// .forPaths(PathSelectors.any()).build();
// }
//
// private AuthorizationScope[] scopes(FxzSwaggerProperties swagger) {
// return new AuthorizationScope[] { new AuthorizationScope("test", "") };
// }
//
// }