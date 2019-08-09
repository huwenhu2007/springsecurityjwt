package cn.com.securityjwt.service;


import cn.com.securityjwt.entity.ResponseUserToken;

/**
 * @Author huwenhu
 * @Date 2019/8/7 12:08
 **/
public interface UserService {

    public ResponseUserToken login(String username, String password);

}
