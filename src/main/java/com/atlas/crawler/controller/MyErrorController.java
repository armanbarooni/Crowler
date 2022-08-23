/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @Autowired
    private LogService logService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Authentication authentication) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object msg = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        String errorMessage = null;

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "NOT FOUND";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "INTERNAL SERVER ERROR";

            }else if(statusCode == HttpStatus.UNAUTHORIZED.value()){
                errorMessage = "UNAUTHORIZED";

            }else if(statusCode == HttpStatus.BAD_REQUEST.value()){
                errorMessage = "BAD REQUEST";


            }else if(statusCode == HttpStatus.FORBIDDEN.value()){
                errorMessage = "FORBIDDEN";


            }else if(statusCode == HttpStatus.REQUEST_TIMEOUT.value()){
                errorMessage = "REQUEST TIMEOUT";

            }else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()){
                errorMessage = "SERVICE UNAVAILABLE";

            }
        }
        if(errorMessage == null && msg != null){
            errorMessage = msg.toString();
        }

        if(authentication != null){

            String userName = authentication.getName();
            Log log = new Log();
            log.setEventType("Error "+errorMessage);
            log.setEventResult(0);
            log.setFormName(requestUri.toString());

            Long errorTimeStamp = System.currentTimeMillis();

            log.setTimeStampStartDate(errorTimeStamp);
            log.setTimeStampFinishDate(errorTimeStamp);

            logService.save(userName ,request,log);
        }

        return "error";
    }


    @Override
    public String getErrorPath() {
        return "error";
    }

//    need to @ControllerAdvice
//    @ExceptionHandler(value = Exception.class)
//    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
//
//        System.out.println("\n\n\n ************* \n\n\n");
//
//        System.out.println("Request: " + req.getRequestURL() + " raised " + ex);
//        System.out.println("Request: " + ex.getMessage() + " raised " + ex);
//        System.out.println("Request: " + req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) + " raised " + ex);
//        ex.printStackTrace();
//
//
//        System.out.println("\n\n\n ************* \n\n\n");
//
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", ex);
//        mav.addObject("url", req.getRequestURL());
//        mav.setViewName("error");
//        return mav;
//
////        return "error";
//
//    }

}
