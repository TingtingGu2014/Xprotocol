/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xprotocol.web.config;

import com.xprotocol.utils.UtilsStringHelper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author zhao0677
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebFilter implements Filter {

    private static final boolean CONDITION = true;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//		logger("Initiating WebFilter >> ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (CONDITION == true) {
            HttpServletRequest req = (HttpServletRequest) request;
            String path = req.getPathInfo();
            System.out.println("*** current req path ="+path);
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(req);
            String xsrf = requestWrapper.getHeader("X-XSRF-TOKEN");
            if(UtilsStringHelper.isEmptyString(xsrf) || !xsrf.matches("([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})")){
                String xsrfCookieVal = XprotocolWebUtils.getHttpRequestCookieByName("XSRF-TOKEN", req);
                if(!UtilsStringHelper.isEmptyString(xsrfCookieVal)){
                    requestWrapper.addHeader("X-XSRF-TOKEN", xsrfCookieVal);
                }
            }            
            chain.doFilter(requestWrapper, response); 
        } else {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void destroy() {
//		logger.debug("Destroying WebFilter >> ");
    }
}