package cn.com.securityjwt.security.access;

import cn.com.securityjwt.security.SecurityAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;

/**
 * 1. decide 方法是判定是否拥有权限的决策方法，
 * 2. authentication 是释CustomUserService中循环添加到 GrantedAuthority 对象中的权限信息集合.
 * 3. object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
 * 4. configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的
 * url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
 * @Author huwenhu
 * @Date 2019/8/8 13:33
 **/
@Component
public class MyAccessDecisionManager implements AccessDecisionManager{

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
        SecurityAssert.accessNoEmpty(authorites, "user has not available permissions.");

        String needRole;
        ConfigAttribute c;
        for(Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext();){
            c = iter.next();
            needRole = c.getAttribute();
            for(GrantedAuthority ga : authorites){
                if(StringUtils.equals(needRole, ga.getAuthority())){
                    return ;
                }
            }
        }

        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        SecurityAssert.accessNoExist(String.format("user [%s] has not uri [%s] access", authentication.getName(), request.getRequestURI()));
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
