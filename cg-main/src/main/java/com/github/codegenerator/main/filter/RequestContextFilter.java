package com.github.codegenerator.main.filter;

import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class RequestContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //多会话的支持
        SessionGenerateContext context = null;
        if (req.getRequestURI().endsWith("/init")) {
            context = new SessionGenerateContext();
            context.setSessionId(req.getSession().getId());
            context.getGenerateInfo().setCodepath(FileUtils.concatPath(ContextContainer.USER_CODE_DIR, context.getSessionId(), "/"));
            //访问首页时，重置会话对应的上下文
            ContextContainer.getSessionContext().put(req.getSession().getId(), context);
        } else {
            //设置当前当前线程的context为session对应的context，一个session中的所有请求共用一个context
            context = ContextContainer.getSessionContext().get(req.getSession().getId());
            if (null == context) {
                ((HttpServletResponse) response).sendRedirect("/config/init");//会话过期了就跳转到首页
                return;
            }
        }
        ContextContainer.setContext(context);
        context.resetErrorMsgs();//重置错误
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
