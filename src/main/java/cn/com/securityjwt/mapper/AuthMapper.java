package cn.com.securityjwt.mapper;

import cn.com.securityjwt.entity.JwtUser;
import cn.com.securityjwt.entity.Pression;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author JoeTao
 * createAt: 2018/9/17
 */
@Repository(value = "authMapper")
public interface AuthMapper {
    /**
     * 根据用户名查找用户
     * @param name
     * @return
     */
    JwtUser findByUsername(@Param("name") String name);

    /**
     * 通过用户id查询角色信息
     * @param lId
     * @return
     */
    List<String> findRoleByUserId(@Param("lId") Long lId);

    /**
     * 获取角色与资源的信息
     * @return
     */
    List<Pression> findResourceRoles();

}
