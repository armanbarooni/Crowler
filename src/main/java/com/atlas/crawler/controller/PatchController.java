package com.atlas.crawler.controller;

import com.atlas.crawler.adapter.Adapter;
import com.atlas.crawler.entity.Log;
import com.atlas.crawler.service.CoreService;
import com.atlas.crawler.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Reza Dehghani
 */
@Controller
@RequestMapping("ordinary/patch")
public class PatchController {

    @Autowired
    private CoreService coreService;

    @Autowired
    private LogService logService;

    @GetMapping("/patch-index")
    public String patchIndex(Model model, HttpServletRequest request){

        if (request.getSession().getAttribute("messageTitle") != null) {

            model.addAttribute("messageTitle", request.getSession().getAttribute("messageTitle"));
            model.addAttribute("messageContent", request.getSession().getAttribute("messageContent"));
            model.addAttribute("messageIcon", request.getSession().getAttribute("messageIcon"));

            request.getSession().removeAttribute("messageTitle");
            request.getSession().removeAttribute("messageContent");
            request.getSession().removeAttribute("messageContent");
        }

        return "ordinary/index-patch";
    }

    @PostMapping("/download-path")
    public String downloadPatch(@RequestParam(value = "distribution",required = false) String distribution,
                                @RequestParam(value = "version", required = false) String version,
                                @RequestParam(value = "pkg", required = false) String packages,
                                HttpServletRequest request, Authentication authentication){

        try {
            if(distribution.equals("fedora") ||distribution.equals("centos")){

                if(isNumeric(version.trim()) && packages.trim().length()>0){
                    List<String> pkgs = convertStringToList(packages);
                    Adapter adapter = new Adapter(coreService.getConnection());
                    adapter.downloadPatchs(distribution,version,pkgs);

                    String userName = authentication.getName();
                    Log log = new Log();
                    log.setEventType("Create Patch: "+packages);
                    log.setEventResult(1);
                    log.setFormName("Create Patch");
                    Long patchTimeStamp = System.currentTimeMillis();


                    log.setTimeStampStartDate(patchTimeStamp);
                    log.setTimeStampFinishDate(patchTimeStamp);
                    logService.save(userName, request, log);



                    request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                    request.getSession().setAttribute("messageContent", "وصله با موفقیت ایجاد شد");
                    request.getSession().setAttribute("messageIcon", "success");

                }else {
                    request.getSession().setAttribute("messageTitle", "خطا");
                    request.getSession().setAttribute("messageContent", "نسخه یا بسته ها نمی تواند خالی باشد");
                    request.getSession().setAttribute("messageIcon", "error");
                }
            }else {
                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", "توزیع وارد شده صحیح نیست");
                request.getSession().setAttribute("messageIcon", "error");
            }

        }catch (Exception e){
            e.printStackTrace();
            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");
        }




        return "redirect:/ordinary/patch/patch-index";
    }

    private List<String> convertStringToList(String file) {
        String pkg[] = file.split("\\r?\\n");
        List<String> temp = Arrays.asList(pkg.clone());

        return temp;
    }

    private boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
