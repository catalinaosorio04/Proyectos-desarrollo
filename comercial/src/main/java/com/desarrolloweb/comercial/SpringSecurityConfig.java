package com.desarrolloweb.comercial;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.desarrolloweb.comercial.model.service.UsuarioDetailService;

@Configuration
@EnableMethodSecurity // (opcional, si usas @PreAuthorize o similares)
public class SpringSecurityConfig {

    private final UsuarioDetailService usuarioDetailService;

    public SpringSecurityConfig(UsuarioDetailService usuarioDetailService) {
        this.usuarioDetailService = usuarioDetailService;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // @Bean
    // UserDetailsService userDetailsService() throws Exception {
    // InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    // manager.createUser(User.withUsername("jefe").password(passwordEncoder().encode("Abc123")).roles("ADMIN",
    // "USER").build());
    // manager.createUser(User.withUsername("luis").password(passwordEncoder().encode("Abc123")).roles("USER").build());

    // return manager;
    // }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/comercial/clienteslistar",
                                "/comercial/productoslistar", "/comercial/categoriaslistar", "/reportes/**")
                        .permitAll()
                        .requestMatchers("/comercial/clienteconsultar/**", "/comercial/productoconsultar/**",
                                "/comercial/categoriaconsultar/**")
                        .hasAnyRole("USER")
                        .requestMatchers("/uploads/**").hasAnyRole("USER")
                        .requestMatchers("/comercial/clientenuevo/**", "/comercial/clienteeliminar/**",
                                "/comercial/clientemodificar/**")
                        .hasAnyRole("ADMIN")
                        .requestMatchers("/comercial/productonuevo/**", "/comercial/productoeliminar/**",
                                "/comercial/productomodificar/**")
                        .hasAnyRole("ADMIN")
                        .requestMatchers("/comercial/facturanueva/**").hasAnyRole("ADMIN")
                        .requestMatchers("/comercial/mantenimientolistar/**").hasAnyRole("MMTO")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login") // Indica que usas tu propia página
                        .permitAll() // Permite acceso a /login sin autenticación
                )
                .logout(logout -> logout
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error_403"));
        return http.build();
    }

}
