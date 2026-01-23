package com.mycompany.filter;

import com.mycompany.controller.content.ContentRequestHook;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ContentRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String requestUri = ((HttpServletRequest) request).getRequestURI();
            if (requestUri != null && (requestUri.endsWith("/faq") || requestUri.endsWith("/faq/"))) {
                ContentRequestHook.run();
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
