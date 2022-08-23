package io.electrosalaf.fancystore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private String
}
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.electrosalaf.fancystore"))
                .paths(PathSelectors.any())
                .build().apiInfo(getApiInfo());
    }

    public ApiInfo getApiInfo() {
        Contact contact = new Contact("electrosalaf", "https://electrosalaf.io", "contact.electrosalaf@gmail.com");
        return new ApiInfoBuilder()
                .title("FancyStore API")
                .description("FancyStore API Documentation")
                .version("1.0.0")
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }

