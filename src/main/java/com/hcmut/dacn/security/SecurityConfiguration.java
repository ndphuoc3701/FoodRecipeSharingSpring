//package com.hcmut.dacn.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
////@EnableWebSecurity
//public class SecurityConfiguration  {
//
//    @Autowired
//    DataSource dataSource;
////
////    @Bean
////    UserDetailsService userDetailsService(){
////
////    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////                .inMemoryAuthentication()
////                .withUser("user1")
////                .password(passwordEncoder().encode("user1Pass"))
////                .authorities("ROLE_USER");
//        auth
////                .jdbcAuthentication()
//                .inMemoryAuthentication()
////                .dataSource(dataSource)
////                .withDefaultSchema()
//                .withUser(User.withUsername("user").password("userp").roles("USER"))
//                .withUser(User.withUsername("admin").password("adminp").roles("ADMIN"));
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)
//            throws Exception {
//        httpSecurity.authorizeHttpRequests(authz->
//                authz.requestMatchers("/api/recipes/cc").hasRole("USER")
//                        .requestMatchers("/api/recipes/dd").hasRole("ADMIN")
//                .anyRequest()
//                .authenticated()).formLogin();
//
////        httpSecurity.csrf()
////                .ignoringAntMatchers("/h2-console/**");
////        httpSecurity.headers()
////                .frameOptions()
////                .sameOrigin();
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
