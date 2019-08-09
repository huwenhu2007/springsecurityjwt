package cn.com.securityjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 权限信息
 * @Author huwenhu
 * @Date 2019/8/8 14:59
 **/
@Data
@AllArgsConstructor
public class Pression {
    private String url;
    private String role;
}
