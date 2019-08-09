package cn.com.securityjwt.security.tag;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 将security标签加入freemarker
 * @Author huwenhu
 * @Date 2019/8/9 9:52
 **/
public class ClassPathTldsLoader {

    /**
     * 指定路径
     */
    private static final String SECURITY_TLD = "/security.tld";

    final private List<String> classPathTlds;

    public ClassPathTldsLoader(String... classPathTlds) {
        super();
        if(ArrayUtils.isEmpty(classPathTlds)){
            this.classPathTlds = Arrays.asList(SECURITY_TLD);
        }else{
            this.classPathTlds = Arrays.asList(classPathTlds);
        }
    }
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void loadClassPathTlds() {
        freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(classPathTlds);
    }

}
