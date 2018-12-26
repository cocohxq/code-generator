package com.github.codegenerator.main.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    public static Cookie newCookie(String name, String value, String domain, Integer maxAge, String path){
        Cookie cookie = new Cookie(name, value);
        if (!StringUtils.isEmpty(domain)) {
            cookie.setDomain(domain);
        }
        if (null != maxAge) {
            cookie.setMaxAge(maxAge);
        }
        if (!StringUtils.isEmpty(path)) {
            cookie.setPath(path);
        }
        return cookie;
    }

    public static String getStringFromCookie(HttpServletRequest request, String name, String defaultValue){
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0 || StringUtils.isEmpty(name)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (!name.equals(cookie.getName())) {
                continue;
            }
            return cookie.getValue();
        }
        return null;
    }

}
