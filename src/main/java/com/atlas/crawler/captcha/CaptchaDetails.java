/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.captcha;

import cn.apiclub.captcha.Captcha;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class CaptchaDetails implements Serializable {
    private static final long serialVersionUID = 8372386434886698719L;

    private final String answer;
    private final Captcha captcha;

    public CaptchaDetails(HttpServletRequest request){
        this.answer = request.getParameter("captcha");
        Object object = WebUtils.getSessionAttribute(request,"captcha");
        this.captcha = (object instanceof Captcha) ? (Captcha) object:null;
    }

    public String getAnswer() {
        return answer;
    }

    public Captcha getCaptcha() {
        return captcha;
    }
}
