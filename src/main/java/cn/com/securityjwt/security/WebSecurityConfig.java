package cn.com.securityjwt.security;

import cn.com.securityjwt.security.access.MyAccessDecisionManager;
import cn.com.securityjwt.security.access.MyFilterSecurityInterceptor;
import cn.com.securityjwt.security.access.MyInvocationSecurityMetadataSourceService;
import cn.com.securityjwt.security.login.JwtAuthenticationEntryPoint;
import cn.com.securityjwt.security.login.JwtAuthenticationTokenFilter;
import cn.com.securityjwt.security.tag.ClassPathTldsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author huwenhu
 * @Date 2019/8/7 10:13
 **/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final AccessDeniedHandler accessDeniedHandler;

    private final UserDetailsService customUserDetailsService;

    private final MyInvocationSecurityMetadataSourceService myInvocationSecurityMetadataSourceService;

    private final MyAccessDecisionManager myAccessDecisionManager;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler,
                             @Qualifier("restAuthenticationAccessDeniedHandler") AccessDeniedHandler accessDeniedHandler,
                             @Qualifier("customUserDetailsService") UserDetailsService customUserDetailsService,
                             MyInvocationSecurityMetadataSourceService myInvocationSecurityMetadataSourceService,
                             MyAccessDecisionManager myAccessDecisionManager,
                             JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customUserDetailsService = customUserDetailsService;
        this.myInvocationSecurityMetadataSourceService = myInvocationSecurityMetadataSourceService;
        this.myAccessDecisionManager = myAccessDecisionManager;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(customUserDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }
    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter,放在验证过滤器之前
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加权限过滤器
        httpSecurity.addFilterBefore(new MyFilterSecurityInterceptor(myInvocationSecurityMetadataSourceService, myAccessDecisionManager), FilterSecurityInterceptor.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 不经过过滤链的请求
        web.ignoring().antMatchers("/auth/*");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自动加载security-taglibs
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ClassPathTldsLoader.class)
    public ClassPathTldsLoader classPathTldsLoader(){
        return new ClassPathTldsLoader();
    }

}
