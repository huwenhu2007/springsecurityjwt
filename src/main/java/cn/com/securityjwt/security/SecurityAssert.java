package cn.com.securityjwt.security;

import cn.com.securityjwt.entity.JwtUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @Author huwenhu
 * @Date 2019/8/8 14:05
 **/
public class SecurityAssert {

    /**
     *
     */
    public static void collectioNoEmpty(Collection coll){
        if(coll.isEmpty()){
            throw new IllegalArgumentException("collection is not empty.");
        }
    }

    public static void accessIsNotNull(Collection coll, String strMessage){
        if(Objects.isNull(coll)){
            throw new AccessDeniedException(strMessage);
        }
    }

    public static void accessNoEmpty(Collection coll, String strMessage){
        if(coll.isEmpty()){
            throw new AccessDeniedException(strMessage);
        }
    }

    public static void accessNoEmpty(Map map, String strMessage){
        if(map.isEmpty()){
            throw new AccessDeniedException(strMessage);
        }
    }

    public static void accessNoExist(String strMessage){
        throw new AccessDeniedException(strMessage);
    }

    public static void userIsNull(JwtUser jwtUser){
        if(Objects.isNull(jwtUser)){
            throw new UsernameNotFoundException("user is null.");
        }
    }
}
