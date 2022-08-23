/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.captcha;

import cn.apiclub.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CaptchaAuthenticationProvider extends DaoAuthenticationProvider {


    @Autowired
    @Override
    public void setUserDetailsService(@Qualifier("userServiceImpl") UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(passwordEncoder());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);

        Object object = authentication.getDetails();
        if(!(object instanceof CaptchaDetails)) {
            throw new InsufficientAuthenticationException("CaptchaNotFound");
        }

        CaptchaDetails captchaDetails = (CaptchaDetails) object;
        Captcha captcha = captchaDetails.getCaptcha();
        if(captcha != null) {
            if(!captcha.getAnswer().equals(captchaDetails.getAnswer())) {
                throw new InsufficientAuthenticationException("CaptchaNotMatch");
            }
        }
    }

    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

}
