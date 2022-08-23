/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.security;


import com.atlas.crawler.entity.User;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PasswordExpirationFilter implements Filter {

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (isUrlExcluded(httpServletRequest)) {
            chain.doFilter(request, response);
        }else {
            User user = getLoggedInUser();

            if(user != null && !userService.isEnabled(user)){

                ((HttpServletRequest) request).getSession().setAttribute("userDisabled","user is disabled");

                showLoginPage(response, httpServletRequest);
            }

            if (user != null && userService.isPasswordExpired(user)) {

                ((HttpServletRequest) request).getSession().setAttribute("passwordExpire","man batel shodam");

                showChangePasswordPage(response, httpServletRequest);

            } else {
                chain.doFilter(request, response);
            }
        }
    }

    private void showChangePasswordPage(ServletResponse response,
                                        HttpServletRequest httpRequest) throws IOException {


        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String redirectURL = httpRequest.getContextPath() + "/";
        httpResponse.sendRedirect(redirectURL);
    }

    private void showLoginPage(ServletResponse response,
                                        HttpServletRequest httpRequest) throws IOException, ServletException {


        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String redirectURL = httpRequest.getContextPath() + "/login";
        httpResponse.sendRedirect(redirectURL);
    }


    private boolean isUrlExcluded(HttpServletRequest httpRequest) {
        String url = httpRequest.getRequestURL().toString();
        if (url.endsWith(".css") || url.endsWith(".png") || url.endsWith(".js") ||
                url.endsWith(".jpg") || url.endsWith(".svg") || url.endsWith("/") || url.endsWith("/login") ||
                url.endsWith("ttf")  || url.endsWith("woff2")|| url.endsWith("changePassword")) {
            return true;
        }

        return false;
    }

    private User getLoggedInUser() {

        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();

        Object principal = null;
        if(authentication != null){
            principal = authentication.getPrincipal();
        }

        if(principal != null  && principal instanceof SecurityUserDetails){
            SecurityUserDetails userDetails = (SecurityUserDetails) principal;

            User user = userDetails.getUser();
            return user;

        }

        return null;

    }

}
