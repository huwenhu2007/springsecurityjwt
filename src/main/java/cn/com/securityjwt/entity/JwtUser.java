package cn.com.securityjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author huwenhu
 * @Date 2019/8/7 10:53
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser implements UserDetails {

    private Long id;
    private String name;
    private String account;
    private String password;
    /**
     * 密码更新时间
     */
    private Date lastPasswordResetDate;
    /**
     * 权限
     */
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Long id,String name,String password,List<String> authorities) {
        this.id =id;
        this.name = name;
        this.account = name;
        this.password = password;
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        if(!CollectionUtils.isEmpty(authorities)){
            for(String code : authorities){
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(code);
                simpleGrantedAuthorities.add(simpleGrantedAuthority);
            }
        }
        this.authorities = simpleGrantedAuthorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities){
        this.authorities = authorities;
    }

    public void setAuthorities(List<String> authorities){
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
        if(!Objects.isNull(authorities) && !CollectionUtils.isEmpty(authorities)){
            for(String code : authorities){
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(code);
                simpleGrantedAuthorities.add(simpleGrantedAuthority);
            }
        }
        this.authorities = simpleGrantedAuthorities;
    }

    /**
     * 获取权限信息，目前只会拿来存角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return account;
    }


    @Override
    public boolean isAccountNonExpired() {
        // 账号是否未过期，默认是false，记得要改一下
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        // 账号是否未锁定，默认是false，记得也要改一下
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        // 账号凭证是否未过期，默认是false，记得还要改一下
        return true;
    }


    @Override
    public boolean isEnabled() {
        // 这个有点抽象不会翻译，默认也是false，记得改一下
        return true;
    }

}
