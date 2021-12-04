package cn.zjiali.api.config;

import cn.zjiali.api.model.entity.AmUser;
import cn.zjiali.api.model.response.Result;
import cn.zjiali.api.service.AmUserService;
import cn.zjiali.api.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import java.io.PrintWriter;

/**
 * @author zJiaLi
 * @since 2021-12-01 14:52
 */
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final AmUserService amUserService;
    private final CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    private final CustomUrlDecisionManager customUrlDecisionManager;

    public CustomWebSecurityConfigurerAdapter(AmUserService amUserService, CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource, CustomUrlDecisionManager customUrlDecisionManager) {
        this.amUserService = amUserService;
        this.customFilterInvocationSecurityMetadataSource = customFilterInvocationSecurityMetadataSource;
        this.customUrlDecisionManager = customUrlDecisionManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationEventPublisher(defaultAuthenticationEventPublisher());
        auth.userDetailsService(amUserService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .antMatcher("/**")
                .authorizeRequests();
        http.cors().and().csrf().disable();
        registry
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .antMatchers("/login").permitAll().anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1).sessionRegistry(sessionRegistry()).expiredSessionStrategy(event -> {
                    ResponseUtil.out(event.getResponse(), Result.fail("您已在另一台设备登录，本次登录已下线!"));
                }).maxSessionsPreventsLogin(true)
        //没有认证时，在这里处理结果，不要重定向
        /*.accessDeniedPage("")*/;
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    AmUser amUser = (AmUser) authentication.getPrincipal();
                    amUser.setPassword(null);
                    Result<AmUser> ok = Result.success("登录成功!", amUser);
                    String s = new ObjectMapper().writeValueAsString(ok);
                    out.write(s);
                    out.flush();
                    out.close();
                }
        );
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    Result<Object> respBean = Result.fail(exception.getMessage());
                    if (exception instanceof LockedException) {
                        respBean.setMessage("账户被锁定，请联系管理员!");
                    } else if (exception instanceof CredentialsExpiredException) {
                        respBean.setMessage("密码过期，请联系管理员!");
                    } else if (exception instanceof AccountExpiredException) {
                        respBean.setMessage("账户过期，请联系管理员!");
                    } else if (exception instanceof DisabledException) {
                        respBean.setMessage("账户被禁用，请联系管理员!");
                    } else if (exception instanceof BadCredentialsException) {
                        respBean.setMessage("用户名或者密码输入错误，请重新输入!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respBean));
                    out.flush();
                    out.close();
                }
        );
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/doLogin");
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        //sessionStrategy.setExceptionIfMaximumExceeded(true);
        loginFilter.setSessionAuthenticationStrategy(sessionStrategy);
        return loginFilter;
    }
}
