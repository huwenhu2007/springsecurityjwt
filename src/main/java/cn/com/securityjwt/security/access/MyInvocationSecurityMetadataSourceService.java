package cn.com.securityjwt.security.access;

import cn.com.securityjwt.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 1. 加载权限信息，缓存5分钟
 * 2. 过滤资源对应的权限， object 可获取 HttpServletRequest，通过请求资源对权限进行过滤
 * @Author huwenhu
 * @Date 2019/8/8 14:42
 **/
@Component
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource{

    @Autowired
    private AccessCache accessCache;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Map<String, Collection<ConfigAttribute>> map = accessCache.get();
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)) {
                return map.get(resUrl);
            }
        }
        throw new AccessDeniedException(String.format("uri [%s] access is not exist", request.getRequestURI()));
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
