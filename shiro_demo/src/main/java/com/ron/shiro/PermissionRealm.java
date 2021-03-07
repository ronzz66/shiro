package com.ron.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义realm域对象
 *  继承AuthorizingRealm
 *      重写两个方法
 *          doGetAuthorizationInfo
 *              获取用户的授权数据(用户权限数据)
 *          doGetAuthenticationInfo
 *              根据用户名和密码登录,将用户保存(安全数据)
 */

public class PermissionRealm extends AuthorizingRealm{


    @Override
    public void setName(String name) {
        //自定义realm名称
        super.setName("permissionRealm");
    }

    /**
     * 授权方法
     *  PrincipalCollection:包含了已经认证的安全信息
     *
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取自定义安全数据(这里是用户名)
        String principal = (String) principalCollection.getPrimaryPrincipal();
        //2.根据id或名称查询用户
        //3.查询用户角色和权限

        List<String> roles=new ArrayList<String>();
        roles.add("role1");
        roles.add("role2");
        List<String> perms=new ArrayList<String>();
        perms.add("user:update");
        perms.add("user:save");
        //4.构造返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);//设置角色
        info.addStringPermissions(perms);//设置权限


        return info;
    }
    /**
     *  认证方法
     *      1.比较用户是否成功登录
     *      2.把安全信息存入shiro
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.构造UsernamePasswordToken
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //2.获取用户名和密码
        String username = token.getUsername();
        String password=new String(token.getPassword());
        //3.根据用户名和密码查询数据库
        //4.比较是否和数据库一致
        if (password.equals("123456")){//模拟数据
            //5.成功则存入安全数据到shiro
                //1.自定义安全数据(可以为id或者用户名) 2.一般为密码 3.realm名称
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(username,password,this.getName());
            return info;
        }else {
            //6.失败返回null或抛出异常
            throw new RuntimeException("用户名或密码出错");
        }

    }
}
