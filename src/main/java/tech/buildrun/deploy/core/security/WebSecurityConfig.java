package tech.buildrun.deploy.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                                               authorizeRequests
                                                       .requestMatchers("/public-api/getfiles").permitAll()
                                                       .anyRequest()
                                                       .authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(sessionManagement ->
                                           sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Define os usuários e suas credenciais em memória
        var userDetailsService = new InMemoryUserDetailsManager();
        var user = User.withUsername("user")
                .password(passwordEncoder().encode("password")) // {noop} é usado para evitar codificação de senha em exemplos
                .roles("USER")
                .build();
        var admin = User.withUsername("kiev")
                .password(passwordEncoder().encode("123"))
                .roles("ADMIN")
                .build();
        userDetailsService.createUser(user);
        userDetailsService.createUser(admin);
        return userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
