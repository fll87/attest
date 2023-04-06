package com.qzt.web.core.config;

import com.qzt.common.utils.sign.ToolUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;

public class CustomApplicationProperties implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //处理加密内容（获取到原有配置，然后解密放到新的map 里面（key是原有key））
        HashMap<String, Object> map = new HashMap<>();
        // 获取所有的配置文件
        MutablePropertySources properties = environment.getPropertySources();
        //遍历配置文件
        for (PropertySource<?> ps : properties) {
            if (ps instanceof OriginTrackedMapPropertySource) {
                OriginTrackedMapPropertySource source = (OriginTrackedMapPropertySource) ps;
                String name = "spring.mail.password";
                if(source.containsProperty(name)){
                    OriginTrackedValue value = (OriginTrackedValue) source.getSource().get(name);
                    String decryptValue = ToolUtil.decryptSm4((String)value.getValue());
                    map.put(name, decryptValue);
                }
            }
        }
        // 将解密的数据放入环境变量，并处于第一优先级上 (这里一定要注意，覆盖其他配置)
        if (!map.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource("custom-encrypt", map));
        }
    }
}
