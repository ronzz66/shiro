package cn.itcast.shiro.session;


import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

//自定义SessionManager会话管理器(从请求头中获取sessionId,再通过redis进行操作)
public class CustomSessionManager extends DefaultWebSessionManager {


    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头Authorization中的数据(SessionId)
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)){
            //如果没有,则生成新的sessionId返回
            return super.getSessionId(request, response);

        }else {
            //如果有,则复用sessionId返回
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");//sessionid来源
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);//session具体的值
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);//是否需要验证
            return id;
        }
    }
}
