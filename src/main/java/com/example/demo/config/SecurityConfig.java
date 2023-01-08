package com.example.demo.config;

import com.example.demo.config.jwt.JwtAuthEntryPoint;
import com.example.demo.config.jwt.JwtRequestFilter;
import com.example.demo.config.jwt.JwtUtil;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public JwtRequestFilter authJwtFilter(){
        return new JwtRequestFilter();
    }


    @Bean //construyo una cadena de filtros de seguridad
    // se usa el patron builder
    //primero le indico el tipo de authorizacion para los serverlet request
    // que que se ejecute cuando haga match con /urls/hello y si tiene el role de USER
    //  y que lo authentique por el metodo Basic de HTTP, que puede ser Bearer tambien en caso de JWT
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return  http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthEntryPoint())
                .and()
                .authorizeHttpRequests((authorize) ->
                        authorize

                                .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT).hasAnyRole("USER","ADMIN")
                                .requestMatchers(HttpMethod.PATCH).hasAnyRole("USER","ADMIN")
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST).hasRole("ADMIN")
                                .requestMatchers("/urls").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/urls/**").permitAll()
                                .anyRequest().denyAll()
                )
                //.formLogin(Customizer.withDefaults())
                //ejecuto mi filtro custom antes de que se ejecute el UsernamePasswordAuthenticationFilter
                .addFilterBefore(authJwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /*

    @Bean // establece la conexion con la db externa en la que va a guardar
    // los usuarios, con sus roles correspondientes, usando el esquema por default de spring
    // pero este metodo, no te crea las tablas, por lo que agregan a mano
    //si no lo especificas, te usa lo que configuraste en el application.properties
    // de esta forma podes usar la mimsma db o otra aparte en postgresql por ejemplo para los usuarios
    DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/url-shortner");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("");

        return dataSourceBuilder.build();
    }
    */

    @Bean // registra un usuario usando el passoword encoder que configuramos abajo
    // lo guarda en la base de datos que configuramos con el bean de arriba
    public UserDetailsManager userDetailsService(DataSource dataSource) {

        // en realidad se debe guardar directamente hasheado la contraseña con el metodo que queres,
        // por eso esta deprecado el metodo withDefaultPasswordEncoder
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("franpass1234")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin123")
                .roles("ADMIN")
                .build();

        // los UserDetails los dejo como ayuda de memoria xD

        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        /*
        //crea los usuarios en la db
        // en mi caso ya lo tengo guardados, sino tendría que descomentarlo
        users.createUser(user);
        users.createUser(admin);
*/
        return users;
    }


    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }



    @Bean // configuro una serie de atajos del password encoder, que puedo seleccionar
    // el que quiera cuando queira, pero en este caso, seleccione por idForEncode a bcrypt
    public static PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";

        Map encoders = new HashMap();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("sha256", new StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
