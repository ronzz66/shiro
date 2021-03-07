package cn.itcast.shiro.controller;

import cn.itcast.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md2Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Enumeration;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //添加
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String add() {

        return "添加用户成功";
    }
    //添加

    /**
     * shiro注解配置
     *
     * @RequiresPermissions() 访问此方法必须具有的权限
     * @RequiresRoles() 访问此方法必须拥有的角色
     * 注：如果权限信息不匹配，注解配置会抛出异常，过滤器则setUnauthorizedUrl
     */

    @RequiresRoles("系统管理员")
    @RequiresPermissions("user-home")
    @RequestMapping(value = "/user/home")
    public String home() {

        return "访问个人主页";
    }

    //查询
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String find() {
        return "查询用户成功";
    }

    //更新
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String update(String id) {
        return "更新用户成功";
    }

    //删除
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete() {
        return "删除用户成功";
    }


    //登录异常
    @RequestMapping("/autherror")
    public String autherror(int code) {
        return code == 1 ? "未登录" : "未授权";
    }

    //用户登录
    @RequestMapping(value = "/login")
    public String login(String username, String password) {
        try {
            //密码加密  1.加密内容 2.盐 3.加密次数
            password = new Md2Hash(password, username, 3).toString();
            //构造登录令牌
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            //获取主体
            Subject subject = SecurityUtils.getSubject();
            //获取session
            String id = (String)subject.getSession().getId();

            //执行认证
            subject.login(token);
            return "登录成功"+":"+id;
        } catch (AuthenticationException e) {
            return "登录失败";
        }
    }

    //登录成功后，打印所有session内容
    @RequestMapping(value = "/show")
    public String show(HttpSession session) {
        // 获取session中所有的键值
        Enumeration<?> enumeration = session.getAttributeNames();
        // 遍历enumeration中的
        while (enumeration.hasMoreElements()) {
            // 获取session键值
            String name = enumeration.nextElement().toString();
            // 根据键值取session中的值
            Object value = session.getAttribute(name);
            // 打印结果
            System.out.println("<B>" + name + "</B>=" + value + "<br>/n");
        }
        return "查看session成功";
    }

}
