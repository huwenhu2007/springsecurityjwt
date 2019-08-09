package cn.com.securityjwt.security.login;

import cn.com.securityjwt.entity.JwtUser;
import cn.com.securityjwt.security.JwtUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * jwt效验
 * @Author huwenhu
 * @Date 2019/8/7 10:45
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtUtil jwtUtils;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(antPathMatcher.match("/auth/*", httpServletRequest.getRequestURI())){
            // 过滤不需要通过自定义过滤器的链接
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return ;
        }

        // 支持从请求头中获取jwt-token和从cookie中获取jwt-token
        String auth_token = httpServletRequest.getHeader(header);
        if(StringUtils.isBlank(auth_token)){
            // 请求投中不存在则从cookie中获取
            Cookie[] cookies = httpServletRequest.getCookies();
            if(!ArrayUtils.isEmpty(cookies)){
                for(Cookie c : cookies){
                    if(StringUtils.equals(c.getName(), header))
                        auth_token = c.getValue();
                }
            }
        }

        if (StringUtils.isNotEmpty(auth_token) && auth_token.startsWith(tokenHead)) {
            auth_token = auth_token.substring(tokenHead.length());
            String username = jwtUtils.getUsernameFromToken(auth_token);
            logger.info(String.format("Checking authentication for user %s.", username));
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                JwtUser user = jwtUtils.getUserFromToken(auth_token);
                if (jwtUtils.validateToken(auth_token)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    logger.info(String.format("Authenticated user %s, setting security context", username));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 获取不到token，走用户、密码验证
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
