/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import cn.apiclub.captcha.Captcha;
import com.atlas.crawler.captcha.CaptchaGenerator;
import com.atlas.crawler.captcha.CaptchaUtils;
import com.atlas.crawler.entity.Setting;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.service.LogService;
import com.atlas.crawler.service.SettingService;
import com.atlas.crawler.service.UserService;
import com.atlas.crawler.utils.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@SessionAttributes("counter")
public class LoginController {

    @Autowired
    private CaptchaGenerator captchaGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private SettingService settingService;

    @ModelAttribute("counter")
    public AtomicInteger failureCounter() {
        return new AtomicInteger(0);
    }

    @GetMapping("/login")
    public String loginPage(ModelMap modelMap, HttpSession httpSession,HttpServletRequest request) throws ServletException {



        Object error = httpSession.getAttribute("error");
        String errorMsg;
        String errorTitle;
        String errorIcon;
        if(error != null){
            if (error.toString().equals("CaptchaNotFound")){
                errorMsg = "کد امنیتی را وارد کنید";
                errorTitle = "هشدار";
                errorIcon = "warning";

            }else if(error.toString().equals("CaptchaNotMatch")){
                errorMsg = "کد امنیتی اشتباه است";
                errorTitle = "خطا";
                errorIcon = "error";

            }else if(error.toString().equals("userLocked")){

                Setting setting = settingService.getSetting();

                String t1 = " حساب کاربری شما قفل شد بعد از ";
                String t2 = String.valueOf(setting.getLockTimeDuration());
                String t3 = " ساعت مجددا تلاش کنید ";

                errorMsg = t1+t2+t3;
                errorTitle = "هشدار";
                errorIcon = "warning";

            }else if(error.toString().equals("userUnLocked")){
                errorMsg = "حساب کاربری شما باز شد. لطفا مجددا اقدام به ورود نمایید";
                errorTitle = "توجه";
                errorIcon = "info";

            }else if(error.toString().equals("userDisabled")){
                request.logout();
                errorMsg = "حساب کاربری شما توسط مدیر سیستم غیرفعال شده است";
                errorTitle = "توجه";
                errorIcon = "info";
                request.getSession().setAttribute("messageTitle", errorTitle);
                request.getSession().setAttribute("messageContent", errorMsg);
                request.getSession().setAttribute("messageIcon", errorIcon);

                return "redirect:/login";
            }
            else{
                errorMsg = "نام کاربری یا رمز عبور اشتباه است";
                errorTitle = "خطا";
                errorIcon = "error";

            }

            modelMap.addAttribute("messageTitle", errorTitle);
            modelMap.addAttribute("messageContent", errorMsg);
            modelMap.addAttribute("messageIcon", errorIcon);
            httpSession.removeAttribute("error");
        }

        Object changePassword = httpSession.getAttribute("messageTitle");
        if(changePassword != null){
            modelMap.addAttribute("messageTitle", httpSession.getAttribute("messageTitle"));
            modelMap.addAttribute("messageContent", httpSession.getAttribute("messageContent"));
            modelMap.addAttribute("messageIcon", httpSession.getAttribute("messageIcon"));

            httpSession.removeAttribute("messageTitle");
            httpSession.removeAttribute("messageContent");
            httpSession.removeAttribute("messageContent");
        }


        AtomicInteger counter = (AtomicInteger) modelMap.get("counter");
        if(counter.intValue() >= ConstantUtils.MAX_CAPTCHA_TRIES){
            Captcha captcha = captchaGenerator.createCaptcha(200,50);
            httpSession.setAttribute("captcha",captcha);
            modelMap.addAttribute("captchaEncode", CaptchaUtils.encodeBase64(captcha));
        }

        return "login";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam(name = "old-password") String oldPassword,
                                 @RequestParam(name = "new-password") String newPassword,
                                 @RequestParam(name = "new-repeat-password") String newRepeatPassword,
                                 Authentication authentication,
                                 HttpServletRequest request,
                                 Model model) throws ServletException {


        try {
            if (newPassword.equals(newRepeatPassword)) {

                String userName = authentication.getName();

                User user = userService.findByUserName(userName);
                Integer status = userService.changePassword(oldPassword, newPassword, newRepeatPassword, user);

                if (status == 2) {
                    request.getSession().setAttribute("messageTitle", "خطا");
                    request.getSession().setAttribute("messageContent", "رمز عبور قبلی و رمز عبور انتخابی یکسان است");
                    request.getSession().setAttribute("messageIcon", "error");

                } else if (status == 3) {

                    request.getSession().setAttribute("messageTitle", "خطا");
                    request.getSession().setAttribute("messageContent", "رمز عبور قبلی اشتباه است");
                    request.getSession().setAttribute("messageIcon", "error");

                } else if (status == 4) {

                    request.logout();

                    request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                    request.getSession().setAttribute("messageContent", "رمز عبور با موفقیت تغییر کرد لطفا مجددا وارد شوید");
                    request.getSession().setAttribute("messageIcon", "success");

                    return "redirect:/login";

                }
            } else {

                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", "رمز عبور و تکرار آن یکسان نیستند");
                request.getSession().setAttribute("messageIcon", "error");

            }


        } catch (Exception e) {
            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");
        }

        return "redirect:/";
    }
}
