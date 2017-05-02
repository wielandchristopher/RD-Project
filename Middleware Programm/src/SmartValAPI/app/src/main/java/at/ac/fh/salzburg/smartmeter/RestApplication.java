package at.ac.fh.salzburg.smartmeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by reimarklammer on 04.04.17.
 */
@SpringBootApplication
@ImportResource("classpath:springldap.xml")
public class RestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }


    @Configuration
    @Order(1)
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .antMatcher("**")
                    .authorizeRequests()
                    .anyRequest().anonymous()
                    .and()
                    .httpBasic().disable();
        }
    }

}

