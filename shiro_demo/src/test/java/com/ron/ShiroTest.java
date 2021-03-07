package com.ron;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;//需要手动引入此依赖
import org.junit.Before;
import org.junit.Test;

//根据配置文件 认证和授权
public class ShiroTest {

    @Before
    public void before(){
        //1.根据配置文件创建工厂
        Factory<SecurityManager> managerFactory = new IniSecurityManagerFactory("classpath:shiro2.ini");
        //2.获取SecurityManager
        SecurityManager securityManager = managerFactory.getInstance();
        //3.绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);

    }
    /**
     * 测试用户认证
     *  1.认证用户登录
     *
     */
    @Test
    public void test(){
        //4.构造subject
        Subject subject = SecurityUtils.getSubject();
        //5.构造shiro登录数据
        String username="zhangsan";
        String password="123456";
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        //6.主体登录
        subject.login(token);
        //7.验证是否登录成功
        System.out.println("是否登录成功:"+subject.isAuthenticated());
        //8.获取登录数据
        System.out.println(subject.getPrincipal());
    }

    /**
     * 用户授权
     */
    @Test
    public void test2(){
        //4.构造subject
        Subject subject = SecurityUtils.getSubject();
        //5.构造shiro登录数据信息
        String username="lisi";
        String password="123456";
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        //6.主体登录
        subject.login(token);
        //7.验证权限
        System.out.println(subject.hasRole("role1"));//判断是否具有该角色
        System.out.println(subject.isPermitted("user:find"));//判断是否具有该权限
    }


}
