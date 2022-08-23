/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.config;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.service.LogService;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	private UserService userService;

	@Autowired
	private LogService logService;

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String userName = authentication.getName();

		userService.updateLastLogin(userName);


		Log log = new Log();
		log.setEventType("Login Successful");
		log.setEventResult(1);
		log.setFormName("login");
		Long LoginTimeStamp = System.currentTimeMillis();


		log.setTimeStampStartDate(LoginTimeStamp);
		log.setTimeStampFinishDate(LoginTimeStamp);

		logService.save(userName ,request,log);

		HttpSession session = request.getSession();
		User user = userService.findByUserName(userName);
		session.setAttribute("user", user);

		if (user.getFailedAttempt() >0){
			userService.resetFailedAttempts(user.getUserName());
		}
		
		response.sendRedirect(request.getContextPath()+"/");
		
	}

}
