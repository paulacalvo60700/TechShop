package com.tienda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Usuarios de ejemplo (no permanentes), como pide la clase 9.
    @Bean
    public UserDetailsService users(PasswordEncoder encoder,
            @Value("${techshop.demo-password:Demo2026!}") String password) {
        UserDetails admin = User.withUsername("juan")
                .password(encoder.encode(password)).roles("ADMIN").build();
        UserDetails vendedor = User.withUsername("rebeca")
                .password(encoder.encode(password)).roles("VENDEDOR").build();
        UserDetails cliente = User.withUsername("pedro")
                .password(encoder.encode(password)).roles("USUARIO").build();
        return new InMemoryUserDetailsManager(admin, vendedor, cliente);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    // El catálogo es público y permite filtrar categorías.
                    .requestMatchers(HttpMethod.GET, "/",
                            "/login", "/acceso_denegado", "/error",
                            "/css/**", "/js/**", "/img/**", "/fav/**",
                            "/webjars/**", "/uploads/**").permitAll()
                    // Solo el filtro por ID numérico es público; /consultas/listado NO lo es.
                    .requestMatchers(new RegexRequestMatcher("^/consultas/[0-9]+$", "GET")).permitAll()
                    // Operaciones que modifican datos: solo ADMIN, también en servidor.
                    .requestMatchers(HttpMethod.POST, "/categoria/**", "/producto/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/categoria/nuevo", "/producto/nuevo",
                            "/categoria/modificar/**", "/producto/modificar/**").hasRole("ADMIN")
                    // Lectura de inventario y reportes de consulta.
                    .requestMatchers("/carrito", "/carrito/**").hasAnyRole("ADMIN", "USUARIO")
                    .requestMatchers("/categoria/listado", "/producto/listado", "/consultas/**")
                            .hasAnyRole("ADMIN", "VENDEDOR")
                    .anyRequest().authenticated()
                )
                .formLogin(login -> login
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll())
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
                    .permitAll())
                .exceptionHandling(errors -> errors.accessDeniedPage("/acceso_denegado"))
                .build();
    }
}
