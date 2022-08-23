package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.model.CVE;
import com.atlas.crawler.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * @author Reza Dehghani
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private CoreService coreService;

    @PostMapping("/report/register-cve")
    public String registerCVE(@ModelAttribute("cve") CVE cve, BindingResult bindingResult, HttpServletRequest request) {
        try {

            if (bindingResult.hasErrors()) {

                String errors = "";
                for (Object object:bindingResult.getAllErrors()) {
                    if (object instanceof FieldError) {
                        FieldError fieldError = (FieldError) object;
                        errors = errors +"   *"+ fieldError.getDefaultMessage() + ".   ";

                    }
                }

                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", errors);
                request.getSession().setAttribute("messageIcon", "error");

            } else {

                List<String> pkgList = convertStringToList(cve.getProduct());
                Integer errorLineNumber = checkIntegrityPackages(pkgList);

                if (errorLineNumber != -1) {

                    String t1 = " خط شماره ";
                    String t2 = String.valueOf(errorLineNumber);
                    String t3 = " در بخش product name خطا دارد ";
                    t3 = t1 + t2 + t3;

                    request.getSession().setAttribute("messageTitle", "خطا");
                    request.getSession().setAttribute("messageContent", t3);
                    request.getSession().setAttribute("messageIcon", "error");
                    return "redirect:/ordinary/report/reports";

                }


                Connection connection = coreService.getConnection();
                Adapter adapter = new Adapter(connection);
                adapter.saveCve(cve);

                request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                request.getSession().setAttribute("messageContent", "آسیب پذیری با موفقیت ایجاد شد");
                request.getSession().setAttribute("messageIcon", "success");
            }
        } catch (Exception e) {

            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");

        }

        return "redirect:/ordinary/report/reports";
    }

    private List<String> convertStringToList(String file) {
        String pkg[] = file.split("\\r?\\n");
        List<String> temp = Arrays.asList(pkg.clone());

        return temp;
    }

    private Integer checkIntegrityPackages(List<String> packages) {

        int line = 0;
        for (String st : packages) {
            line++;
            int index = st.indexOf(" ");
            int index2 = st.indexOf("*");

            if ((index == -1) && (index2 == -1)) {
                return line;
            }
        }
        return -1;
    }

}
