package com.example.house.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String,String> filterMap=new LinkedHashMap<>();
        filterMap.put("/admin/*","authc,roles[admin]");
        filterMap.put("/admin/*/*","authc,roles[admin]");
        filterMap.put("/user/main","user");
        filterMap.put("/user/account","user");
        filterMap.put("/user/set","user");
        filterMap.put("/user/result","user");
        filterMap.put("/user/setPassword","user");
        filterMap.put("/user/billList","user");
        filterMap.put("/user/billList/*","user");
        filterMap.put("/user/addBill/*","user");
        bean.setFilterChainDefinitionMap(filterMap);
        bean.setLoginUrl("/user/login");
        bean.setUnauthorizedUrl("/user/noauth");
        return bean;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
