package com.ron;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

//*使用realm域进行认证和授权
public class ShiroTest2 {
    public static void main(String[] args) {
        //1.根据配置文件创建工厂
        Factory<SecurityManager> managerFactory = new IniSecurityManagerFactory("classpath:shiro3.ini");
        //2.获取SecurityManager
        SecurityManager securityManager = managerFactory.getInstance();
        //3.绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);
        //4.构造subject
        Subject subject = SecurityUtils.getSubject();
        //5.模拟shiro登录数据信息
        String username="张三";
        String password="123456";
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        //6.主体登录-->采用realm域进行认证
        subject.login(token);
        //7.验证权限
        System.out.println(subject.hasRole("role1"));//判断是否具有该角色
        System.out.println(subject.isPermitted("user:find"));//判断是否具有该权限
    }
}
