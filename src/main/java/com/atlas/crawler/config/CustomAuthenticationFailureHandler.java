/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.config;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.entity.Setting;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.service.LogService;
import com.atlas.crawler.service.SettingService;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private SettingService settingService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        request.getSession().setAttribute("error",exception.getMessage());


        String userName = request.getParameter("username");
        User user = userService.findByUserName(userName);
        Setting setting = settingService.getSetting();

        if(user != null){

            Log log = new Log();
            log.setEventType("Login Unsuccessful");
            log.setEventResult(0);
            log.setFormName("login");

            Long LoginTimeStamp = System.currentTimeMillis();


            log.setTimeStampStartDate(LoginTimeStamp);
            log.setTimeStampFinishDate(LoginTimeStamp);
            logService.save(userName, request, log);

            if(user.getEnabled()){
                if(user.getAccountNonLocked()){

                    if(user.getFailedAttempt() < setting.getMaxFailedAttempts()){
                        userService.increaseFailedAttempts(user);
                    }else {
                        Log log2 = new Log();
                        log2.setEventType("Login Locked");
                        log2.setEventResult(1);
                        log2.setFormName("login");
                        Long LoginTimeStamp2 = System.currentTimeMillis();


                        log2.setTimeStampStartDate(LoginTimeStamp2);
                        log2.setTimeStampFinishDate(LoginTimeStamp2);
                        logService.save(userName, request, log2);
                        userService.lock(user);
                        request.getSession().setAttribute("error","userLocked");

                    }
                }else if(!user.getAccountNonLocked()){

                    // code lock user

                    if(userService.unlockWhenTimeExpired(user)){
                        Log log3 = new Log();
                        log3.setEventType("Login UnLocked");
                        log3.setEventResult(1);
                        log3.setFormName("login");
                        Long LoginTimeStamp3 = System.currentTimeMillis();

                        log3.setTimeStampStartDate(LoginTimeStamp3);
                        log3.setTimeStampFinishDate(LoginTimeStamp3);
                        logService.save(userName, request, log3);

                        request.getSession().setAttribute("error","userUnLocked");

                    }
                }
            }else{
                request.getSession().setAttribute("error","userDisabled");
            }


        }

        String redirectUrl = request.getContextPath() + "/login";

        super.setDefaultFailureUrl(redirectUrl);


        super.onAuthenticationFailure(request, response, exception);
    }


}
