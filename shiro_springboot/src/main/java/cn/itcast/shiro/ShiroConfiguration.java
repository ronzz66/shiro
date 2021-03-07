package cn.itcast.shiro;


import cn.itcast.shiro.realm.CustomRealm;
import cn.itcast.shiro.session.CustomSessionManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

//shiro整合springboot配置
@Configuration
public class ShiroConfiguration {

    //创建Realm域对象
    @Bean
    public CustomRealm getRealm(){
        return new CustomRealm();
    }

    //创建安全管理器（管理所有的realm域对象）
    @Bean
    public SecurityManager getSecurityManager(CustomRealm customRealm){
        //安全管理器如果不指定则使用默认的会话管理器
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager(customRealm);

        //将自定义的会话管理器注册到安全管理器中(通过redis存储安全数据)
        securityManager.setSessionManager(sessionManager());
        //将自定义redis缓存管理器注册到安全管理器
        securityManager.setCacheManager(cacheManager());

        return securityManager;

    }

    /**
     * 创建shiro 过滤器工厂
     *  Shiro进行权限控制是通过一组filter集合进行控制的
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        //创建过滤器工厂
        ShiroFilterFactoryBean factory=new ShiroFilterFactoryBean();

        //设置安全管理器
        factory.setSecurityManager(securityManager);
        //未登录跳转的页面
        factory.setLoginUrl("/autherror?code=1");//未登录页面，跳转到登录页面
        factory.setUnauthorizedUrl("/autherror?code=2");//未授权
        //设置过滤器集合
        Map<String,String>filterMap=new LinkedHashMap<>();//确保有序
        //filterMap.put("/user/home","anon");//可以匿名访问
        //filterMap.put("/user/home","perms[user-home]");//必须拥有user-home权限才能访问
        //filterMap.put("/user/home","roles[系统管理员]");//必须拥有系统管理员权限才能访问

        filterMap.put("/user/**","authc");//必须认证登录后访问
        factory.setFilterChainDefinitionMap(filterMap);
        return factory;
    }
    //开启shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //-------------------------------自定义会话管理器-----------------------------------
    @Value("${spring.redis.host}")
    private String host;//redis地址
    @Value("${spring.redis.port}")
    private String port;//redis端口

    //1.redis控制器操作redis
    public RedisManager redisManager(){

        RedisManager redisManager = new RedisManager();
        redisManager.setPort(Integer.parseInt(port));
        redisManager.setHost(host);
        return redisManager;
    }
    //2.sessiomDao
    public RedisSessionDAO sessionDAO(){
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());//设置RedisManager
        return sessionDAO;
    }
    //3.自定义会话管理器
    public DefaultWebSessionManager sessionManager(){
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }
    //4.缓存管理器
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager=new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
    //-------------------------------自定义会话管理器-----------------------------------






}
