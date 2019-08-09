package cn.com.securityjwt.security.access;

import cn.com.securityjwt.entity.Pression;
import cn.com.securityjwt.mapper.AuthMapper;
import cn.com.securityjwt.security.SecurityAssert;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 权限缓存
 * @Author huwenhu
 * @Date 2019/8/8 14:46
 **/
@Component
public class AccessCache {

    private static final String CACHE_KEY = "_key";

    @Autowired
    AuthMapper authMapper;

    private CacheLoader<String, Map<String, Collection<ConfigAttribute>>> createCacheLoader() {
        return new CacheLoader<String, Map<String, Collection<ConfigAttribute>>>() {
            @Override
            public Map<String, Collection<ConfigAttribute>> load(String key) throws Exception {
                // 数据不存在时，重新从数据库获取
                List<Pression> list = authMapper.findResourceRoles();
                SecurityAssert.accessNoEmpty(list, "access is not empty.");
                SecurityAssert.accessIsNotNull(list, "access is null");

                Map<String, Collection<ConfigAttribute>> map = new HashMap<>();
                ConfigAttribute cfg;
                Collection<ConfigAttribute> coll;

                for(Pression pre : list){
                    coll = map.get(pre.getUrl());
                    if(Objects.isNull(coll)){
                        coll = new ArrayList<>();
                    }
                    cfg = new SecurityConfig(pre.getRole());
                    coll.add(cfg);
                    map.put(pre.getUrl(), coll);
                }

                return map;
            }
        };
    }

    private LoadingCache<String, Map<String, Collection<ConfigAttribute>>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(createCacheLoader());


    public Map<String, Collection<ConfigAttribute>> get() throws IllegalArgumentException {
        try {
            return cache.get(CACHE_KEY);
        } catch(ExecutionException e){
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
