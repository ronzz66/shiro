package cn.itcast.shiro.realm;

import cn.itcast.shiro.domain.Permission;
import cn.itcast.shiro.domain.Role;
import cn.itcast.shiro.domain.User;
import cn.itcast.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//自定义域对象
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    public void setName(String name) {
        super.setName("CustomRealm");
    }

    /**
     * 认证登录方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException 传递的用户名密码 token
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1.构造UsernamePasswordToken
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //2.获取用户名和密码
        String username = token.getUsername();
        String password=new String(token.getPassword());
        //3.根据用户名和密码查询数据库
        User user = userService.findByName(username);
        System.out.println(user);
        //4.比较是否和数据库一致
        if (user!=null && password.equals(user.getPassword())){//模拟数据
            //5.成功则存入安全数据到shiro
            //1.自定义安全数据 2.一般为密码 3.realm名称
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
            return info;
        }else {
            //6.失败返回null或抛出异常
            throw new RuntimeException("用户名或密码出错");
        }
    }


    /**
     * 授权方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取自定义安全数据(这里是用户)
        User user = (User)principalCollection.getPrimaryPrincipal();
        //2.根据id或名称查询用户
        //3.查询用户角色和权限
        Set<String> roles =new HashSet<>();//角色集合
        Set<String> perms =new HashSet<>();//权限集合
        for (Role role : user.getRoles()) {//这里使用的是springdatajpa执行认证查询时已经自动查询出角色和权限
            roles.add(role.getName());
            for (Permission permission : role.getPermissions()) {
                perms.add(permission.getCode());
                System.out.println(permission.getCode());
            }
        }

        //4.构造返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);//设置角色
        info.addStringPermissions(perms);//设置权限


        return info;
    }


}
