package cn.com.securityjwt.security.login;

import cn.com.securityjwt.entity.JwtUser;
import cn.com.securityjwt.mapper.AuthMapper;
import cn.com.securityjwt.security.SecurityAssert;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 登录身份证验证
 * @Author huwenhu
 * @Date 2019/8/7 10:31
 **/
@Component(value="customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Qualifier("authMapper")
    AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        JwtUser jwtUser = authMapper.findByUsername(userName);
        SecurityAssert.userIsNull(jwtUser);
        List<String> authorities = authMapper.findRoleByUserId(jwtUser.getId());
        jwtUser.setAuthorities(authorities);
        jwtUser.setAccount(jwtUser.getName());
        return jwtUser;
    }

}
