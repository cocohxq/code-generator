package com.github.codegenerator.main.listener;


import com.github.codegenerator.common.util.ContextContainer;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //会话过期则删除构建上下文
        ContextContainer.getSessionContext().remove(se.getSession().getId());
    }
}
