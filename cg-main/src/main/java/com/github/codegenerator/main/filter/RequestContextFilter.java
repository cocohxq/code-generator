package com.github.codegenerator.main.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.DataUtil;
import com.github.codegenerator.common.util.FileUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

            //有操作
            String operation = request.getParameter("op");
            if(!StringUtils.isEmpty(operation)){
                request.setAttribute("op",operation);

                //加载相应数据
                String selectedConfig = request.getParameter("sc");
                if(!StringUtils.isEmpty(selectedConfig)){
                    request.setAttribute("selectedConfig",selectedConfig);
                    Config config = DataUtil.getData("cb", selectedConfig, Config.class);
                    request.setAttribute("configInfo",config);
                }else{
                    request.setAttribute("configInfo",new Config());
                }
            }else{
                List<String> configList = DataUtil.getDataNameList();
                configList.add(0,"请选择");
                if(null != configList){
                    request.setAttribute("configList", configList);
                }
            }

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
