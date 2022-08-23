/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.config;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Autowired
	private LogService logService;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		String userName = authentication.getName();

		Log log = new Log();
		log.setEventType("Logout");
		log.setEventResult(1);
		log.setFormName("logout");
		Long LogoutTimeStamp = System.currentTimeMillis();

		log.setTimeStampStartDate(LogoutTimeStamp);
		log.setTimeStampFinishDate(LogoutTimeStamp);

		logService.save(userName ,request,log);


		super.onLogoutSuccess(request, response, authentication);
	}

	
}
