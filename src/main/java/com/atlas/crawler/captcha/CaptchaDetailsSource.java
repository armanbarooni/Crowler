/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.captcha;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CaptchaDetailsSource implements AuthenticationDetailsSource<HttpServletRequest , CaptchaDetails> {

    @Override
    public CaptchaDetails buildDetails(HttpServletRequest context) {
        return new CaptchaDetails(context);
    }
}
