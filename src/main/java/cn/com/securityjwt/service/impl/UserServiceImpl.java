package cn.com.securityjwt.service.impl;

import cn.com.securityjwt.entity.ResponseUserToken;
import cn.com.securityjwt.entity.JwtUser;
import cn.com.securityjwt.security.JwtUtil;
import cn.com.securityjwt.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @Author huwenhu
 * @Date 2019/8/7 12:08
 **/
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    public ResponseUserToken login(String username, String password){
        //用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final JwtUser userDetail = (JwtUser) authentication.getPrincipal();
        userDetail.setAuthorities(authentication.getAuthorities());
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        return new ResponseUserToken(token, userDetail);
    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new RuntimeException("用户名或密码无效");
        }
    }


}
