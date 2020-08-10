package com.czq.cas;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册验证器
 * @author chengzequn@foxmail.com
 * @since 2020/8/6 15:01
 */
@Configuration("AuthenticationEventExecutionPlanConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class AuthenticationEventExecutionPlanConfiguration implements AuthenticationEventExecutionPlanConfigurer {

    @Autowired
    private CasConfigurationProperties properties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    @Bean(name="securityManager")
    public SecurityManager securityManager(){
        DefaultSecurityManager securityManager=new DefaultSecurityManager();
        //设置自定义realm
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    @Bean
    public ShiroRealm shiroRealm(){
        ShiroRealm shiroRealm=new ShiroRealm();
        shiroRealm.setCachingEnabled(false);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(false);
        //启动授权缓存，即缓存AuthorizationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(false);
        return shiroRealm;
    }

    @Bean
    public AuthenticationHandler authenticationHandler(){
        UsernamePasswordAuthenticationHandler handler=
                new UsernamePasswordAuthenticationHandler(UsernamePasswordAuthenticationHandler.class.getSimpleName(),
                        servicesManager,new DefaultPrincipalFactory(),1);
        return handler;
    }
    
    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(authenticationHandler());
    }
}
