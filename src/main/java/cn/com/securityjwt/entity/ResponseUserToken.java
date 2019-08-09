package cn.com.securityjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author huwenhu
 * @Date 2019/8/7 13:56
 **/
@Data
@AllArgsConstructor
public class ResponseUserToken {
    private String token;
    private JwtUser userDetail;
}
