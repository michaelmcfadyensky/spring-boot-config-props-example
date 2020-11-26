package com.example.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("local")
class ConfigTest {
    ConfigurableApplicationContext ctx;

    @AfterEach
    public void tearDown() {
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    void defaultProperties() {
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("properties", new HashMap<>()));
        Assertions.assertThatCode(() -> {
            ctx = new SpringApplicationBuilder(ConfigurationTestApplication.class)
                    .web(WebApplicationType.NONE)
                    .environment(environment)
                    .run();
            SectionConfigurationProperties bean = ctx.getBean(SectionConfigurationProperties.class);
            Assertions.assertThat(bean).isNotNull();
            Assertions.assertThat(bean.getSection())
                    .isNotNull()
                    .hasSize(2);

        }).doesNotThrowAnyException();
    }

    @Test
    void propertiesSuccessfulOverride() {
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> source = new HashMap<>();
        source.put("test.section.key2.propB", "jusssz");
        source.put("test.section[key1].propA", "tomasz");
//        source.put("test.testing[0].a", "tj");
        propertySources.addFirst(new MapPropertySource("properties", source));
        Assertions.assertThatCode(() -> {
            ctx = new SpringApplicationBuilder(ConfigurationTestApplication.class)
                    .web(WebApplicationType.NONE)
                    .environment(environment)
                    .profiles("local")
                    .run();
            SectionConfigurationProperties bean = ctx.getBean(SectionConfigurationProperties.class);
            Assertions.assertThat(bean).isNotNull();
            Assertions.assertThat(bean.getSection().get("key1").getPropA()).isEqualTo("tomasz");
            Assertions.assertThat(bean.getSection().get("key1").getPropB()).isEqualTo("local-p");
            Assertions.assertThat(bean.getSection().get("key2").getPropA()).isEqualTo("local-yml");
            Assertions.assertThat(bean.getSection().get("key2").getPropB()).isEqualTo("jusssz");

        }).doesNotThrowAnyException();
    }


    @EnableConfigurationProperties(value = SectionConfigurationProperties.class)
    @Import({
            ConfigurationPropertiesAutoConfiguration.class,
            PropertyPlaceholderAutoConfiguration.class
    })
    public static class ConfigurationTestApplication {
    }
}