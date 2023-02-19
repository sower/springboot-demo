package me.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @date 2022/09/04
 */
@Configuration
@EnableWebSecurity
// 开启注解设置权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  /** 密码明文加密方式配置 */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /** 获取AuthenticationManager（认证管理器），登录时认证使用 */
  //  @Bean
  //  public AuthenticationManager authenticationManager(
  //      AuthenticationConfiguration authenticationConfiguration) throws Exception {
  //    return authenticationConfiguration.getAuthenticationManager();
  //  }

  @Bean
  AuthenticationManager authenticationManager(PasswordEncoder encoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(encoder);
    return new ProviderManager(provider);
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        // 基于 token，不需要 csrf
        .csrf()
        .disable()
        // 基于 token，不需要 session
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // 设置权限
        .authorizeRequests(
            authorize ->
                authorize
                    // 请求放开
                    .antMatchers("/**")
                    .permitAll()
                    // 其他地址的访问均需验证权限
                    .anyRequest()
                    .authenticated())
        .build();
  }

  /** 配置跨源访问(CORS) */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }
}
