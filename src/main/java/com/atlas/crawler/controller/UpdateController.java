/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

@Controller
@RequestMapping("/expert")
public class UpdateController {

    @Autowired
    private CoreService coreService;

    @GetMapping("/update-db")
    public String updateDatabaseIndex(HttpServletRequest request, Model model) {

        if (request.getSession().getAttribute("messageTitle") != null) {

            model.addAttribute("messageTitle", request.getSession().getAttribute("messageTitle"));
            model.addAttribute("messageContent", request.getSession().getAttribute("messageContent"));
            model.addAttribute("messageIcon", request.getSession().getAttribute("messageIcon"));

            request.getSession().removeAttribute("messageTitle");
            request.getSession().removeAttribute("messageContent");
            request.getSession().removeAttribute("messageContent");
        }

        return "expert/index-update";
    }

    @PostMapping("/update-database")
    public String updateDatabase(@RequestParam(value = "distribution", required = false) String distribution,
                                 @RequestParam(value = "reference", required = false) String reference,
                                 HttpServletRequest request) {

        try {


                if (
                        (reference.equalsIgnoreCase("tenable") ||
                                reference.equalsIgnoreCase("nvd_all") ||
                                reference.equalsIgnoreCase("recent") ||
                                reference.equalsIgnoreCase("nvd_current"))) {

                    Connection connection = coreService.getConnection();
                    Adapter adapter = new Adapter(connection);
                    adapter.updateDatabase(distribution, reference);

                    request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                    request.getSession().setAttribute("messageContent", "بروزرسانی با موفقیت شروع شد");
                    request.getSession().setAttribute("messageIcon", "success");

                }



        } catch (Exception e) {

            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");

        }


        return "redirect:/expert/update-db";

    }

}
