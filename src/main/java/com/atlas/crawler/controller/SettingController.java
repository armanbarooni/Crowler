/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import com.atlas.crawler.entity.Setting;
import com.atlas.crawler.model.SettingModel;
import com.atlas.crawler.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/supervisor/manage/manage-index")
    public String manageSite(Model model, HttpServletRequest request) {
        Setting setting = settingService.getSetting();
        SettingModel settingModel = new SettingModel();

        String expireMonth = setting.getExpireDate() + " ماه ";
        settingModel.setMonthExpirePassword(expireMonth);
        settingModel.setNumberOfAttempts(setting.getMaxFailedAttempts());

        String lockTime = setting.getLockTimeDuration() + " ساعت ";
        settingModel.setLockTime(lockTime);


        if (request.getSession().getAttribute("messageTitle") != null) {

            model.addAttribute("messageTitle", request.getSession().getAttribute("messageTitle"));
            model.addAttribute("messageContent", request.getSession().getAttribute("messageContent"));
            model.addAttribute("messageIcon", request.getSession().getAttribute("messageIcon"));

            request.getSession().removeAttribute("messageTitle");
            request.getSession().removeAttribute("messageContent");
            request.getSession().removeAttribute("messageContent");
        }

        model.addAttribute("setting", settingModel);


        return "supervisor/index-manage";
    }

    @PostMapping("/manager/manage/expire-password")
    public String updateExpiredPasswordTime(@RequestParam(name = "month",required = false) Integer month, HttpServletRequest request) {

        try {
            Setting setting = settingService.getSetting();

            if (month != null && month > 0 && month < 13) {
                setting.setExpireDate(month);
                settingService.update(setting);
                request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                request.getSession().setAttribute("messageContent", "زمان انقضا با موفقیت بروزرسانی شد");
                request.getSession().setAttribute("messageIcon", "success");
            } else {
                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", "اطلاعات ورودی صحیح نیست");
                request.getSession().setAttribute("messageIcon", "error");
            }

        } catch (Exception e) {
            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");
        }


        return "redirect:/supervisor/manage/manage-index";
    }

    @PostMapping("/manager/manage/attempts-login")
    public String updateAttemptsNumber(@RequestParam(name = "attemptNumber", required = false) Integer attemptNumber,
                                       @RequestParam(name = "lockTime", required = false) Integer lockTime,
                                       HttpServletRequest request) {

        try {
            Setting setting = settingService.getSetting();

            if (attemptNumber != null && lockTime != null) {

                if (attemptNumber > 0 && attemptNumber <= 10 && lockTime > 0 && lockTime < 73) {
                    setting.setMaxFailedAttempts(attemptNumber);
                    setting.setLockTimeDuration(lockTime);
                    settingService.update(setting);

                    request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                    request.getSession().setAttribute("messageContent", "اطلاعات با موفقیت بروزرسانی شد");
                    request.getSession().setAttribute("messageIcon", "success");

                    return "redirect:/supervisor/manage/manage-index";

                }

            }


            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "اطلاعات ورودی صحیح نیست");
            request.getSession().setAttribute("messageIcon", "error");


        } catch (Exception e) {
            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");
        }


        return "redirect:/supervisor/manage/manage-index";
    }


}
