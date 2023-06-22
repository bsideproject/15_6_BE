package bside.NotToDoClub.config;

import bside.NotToDoClub.domain_name.auth.config.MyAccessDeniedHandler;
import bside.NotToDoClub.domain_name.auth.config.MyAuthenticationEntryPoint;
import bside.NotToDoClub.domain_name.auth.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring 인증 권한 처리를 위한 configuration
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // 인증
                .antMatchers("/security-login/info").authenticated()
                // 인가
                .antMatchers("/security-login/admin/**").hasAuthority(UserRole.ADMIN.name())
                .anyRequest().permitAll()
                .and()
                // Form Login 방식 적용
                .formLogin()
                // 로그인 할 때 사용할 파라미터들
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginPage("/security-login/login")     // 로그인 페이지 URL
                .defaultSuccessUrl("/security-login")   // 로그인 성공 시 이동할 URL
                .failureUrl("/security-login/login")    // 로그인 실패 시 이동할 URL
                .and()
                .logout()
                .logoutUrl("/security-login/logout")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                // OAuth 로그인
                .and()
                .oauth2Login()
                .loginPage("/security-login/login")
                .defaultSuccessUrl("/security-login")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        http
                .exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .accessDeniedHandler(new MyAccessDeniedHandler());
        http.cors().disable();
    }
}
