package cn.zjiali.api.config;

import cn.zjiali.api.constants.SecConstants;
import cn.zjiali.api.model.entity.AmUser;
import cn.zjiali.api.model.response.Result;
import cn.zjiali.api.service.AmUserService;
import cn.zjiali.api.utils.ResponseUtil;
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
                    ResponseUtil.out(event.getResponse(), Result.fail(SecConstants.EXPIRED_SESSION_MSG));
                }).maxSessionsPreventsLogin(true)
        //没有认证时，在这里处理结果，不要重定向
        /*.accessDeniedPage("")*/;
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
                    AmUser amUser = (AmUser) authentication.getPrincipal();
                    amUser.setPassword(null);
                    Result<AmUser> ok = Result.success(SecConstants.LOGIN_SUCCESS_MSG, amUser);
                    ResponseUtil.out(response, ok);
                }
        );
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
                    Result<Object> respBean = Result.fail(exception.getMessage());
                    if (exception instanceof LockedException) {
                        respBean.setMessage(SecConstants.ACCOUNT_LOCKED_MSG);
                    } else if (exception instanceof CredentialsExpiredException) {
                        respBean.setMessage(SecConstants.PWD_EXPIRED_MSG);
                    } else if (exception instanceof AccountExpiredException) {
                        respBean.setMessage(SecConstants.ACCOUNT_EXPIRED_MSG);
                    } else if (exception instanceof DisabledException) {
                        respBean.setMessage(SecConstants.ACCOUNT_DISABLED_MSG);
                    } else if (exception instanceof BadCredentialsException) {
                        respBean.setMessage(SecConstants.BAD_CREDENTIALS_MSG);
                    }
                    ResponseUtil.out(response, respBean);
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
