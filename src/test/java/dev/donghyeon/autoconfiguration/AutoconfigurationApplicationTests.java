package dev.donghyeon.autoconfiguration;

import dev.donghyeon.greeter.GreetingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.donghyeon.greeter.GreeterConfigParams.USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;


class AutoconfigurationApplicationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(GreeterAutoConfiguration.class));

    @Test
    public void greeterConfigExists() {
        this.contextRunner.run((context -> assertThat(context).hasSingleBean(GreetingConfig.class)));
    }

    @Test
    public void settingsAdded() {
        this.contextRunner.withUserConfiguration(MyGreeterConfig.class)
                .run((context -> assertThat(context.getBean(GreetingConfig.class).getProperty(USER_NAME))
                .isEqualTo("testUserName")));
    }

    @Test
    public void noSettingsAdded() {
        this.contextRunner.run((context ->
                assertThat(context.getBean(GreetingConfig.class).getProperty(USER_NAME))
                        .isEqualTo(System.getProperty("user.name"))));
    }



    //no runtime-generated subclass is necessary.
    @Configuration(proxyBeanMethods = false)
    static class MyGreeterConfig {

        @Bean
        public GreetingConfig myGreeterConfig() {
            GreetingConfig greetingConfig = new GreetingConfig();
            greetingConfig.put(USER_NAME, "testUserName");
            return greetingConfig;
        }

    }
}

