/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.controller;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.entity.Role;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.model.CrmUser;
import com.atlas.crawler.model.VisibleUser;
import com.atlas.crawler.service.LogService;
import com.atlas.crawler.service.RoleService;
import com.atlas.crawler.service.SettingService;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/manager/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private LogService logService;


    @GetMapping("/users")
    public String homeUser(Model model, HttpServletRequest request) {

        if (request.getSession().getAttribute("messageTitle") != null) {

            model.addAttribute("messageTitle", request.getSession().getAttribute("messageTitle"));
            model.addAttribute("messageContent", request.getSession().getAttribute("messageContent"));
            model.addAttribute("messageIcon", request.getSession().getAttribute("messageIcon"));

            request.getSession().removeAttribute("messageTitle");
            request.getSession().removeAttribute("messageContent");
            request.getSession().removeAttribute("messageContent");
        }

        model.addAttribute("crmUser", new CrmUser());


        return "manager/index-user";
    }

    @ResponseBody
    @GetMapping(value = "/users-list", produces = "application/json")
    public List<VisibleUser> findUsers(@RequestParam("role") String roleName, @RequestParam("name") String lastName) {

        try {

            lastName = lastName.trim();
            List<User> tempUsers = null;

            if (roleName != null && roleName.trim().length() != 0) {
                Role role = whatsRole(roleName);
                if (role == null)
                    return null;

                if (lastName.length() == 0) {
                    tempUsers = userService.findUsersByRoles(role);

                } else {
                    tempUsers = userService.findUsersByLastNameAndRoles(role, lastName);

                }
            } else if (lastName.length() != 0) {
                tempUsers = userService.findUsersByLastName(lastName);
            }

            return convertUserToVisibleUser(tempUsers);

        } catch (Exception e) {
            return null;
        }

    }

    private List<VisibleUser> convertUserToVisibleUser(List<User> tempUsers) {
        List<VisibleUser> users = new ArrayList<>();

        for (User user : tempUsers) {
            VisibleUser tempUser = new VisibleUser();
            tempUser.setId(user.getId());
            tempUser.setFirstName(user.getFirstName());
            tempUser.setLastName(user.getLastName());
            tempUser.setUserName(user.getUserName());
            tempUser.setLastLogin(convertTimeStampToDate(user.getTimeStampLastLogin()));
            tempUser.setRole(convertUserRolesToString(user));
            users.add(tempUser);
        }

        return users;
    }

    private String convertUserRolesToString(User user) {
        String stringRoles = "";
        for (Role role : user.getRoles()) {
            String temp = role.getName();
            switch (temp) {
                case "ROLE_MANAGER":
                    temp = "مدیر";
                    break;
                case "ROLE_SUPERVISOR":
                    temp = "ناظر";
                    break;
                case "ROLE_EXPERT":
                    temp = "خبره";
                    break;
                case "ROLE_ORDINARY":
                    temp = "عادی";
                    break;
            }

            stringRoles += temp + " , ";
        }
        stringRoles = stringRoles.substring(0, stringRoles.length() - 2);
        return stringRoles;
    }

    private String convertTimeStampToDate(String stringTimeStamp) {
        Long timeStamp = Long.parseLong(stringTimeStamp);
        Date date = new Date(timeStamp);
        String pattern = "hh:mm:ss  yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String myDate = formatter.format(date);

        return myDate;
    }

    private Role whatsRole(String roleName) {

        String RealRole = "";
        if (roleName.equals("manager")) {
            RealRole = "ROLE_MANAGER";

        } else if (roleName.equals("supervisor")) {
            RealRole = "ROLE_SUPERVISOR";

        } else if (roleName.equals("expert")) {
            RealRole = "ROLE_EXPERT";

        } else if (roleName.equals("ordinary")) {
            RealRole = "ROLE_ORDINARY";

        } else {
            return null;
        }

        Role role = roleService.findRoleByName(RealRole);

        return role;
    }

    @GetMapping("/show-user/{id}")
    public String showUser(@PathVariable Integer id, Model model) {
        try {
            User user = provideUserToShow(id);
            model.addAttribute("user", user);


        } catch (Exception e) {
            model.addAttribute("messageTitle", "خطا");
            model.addAttribute("messageContent", "خطای سرور");
            model.addAttribute("messageIcon", "error");
        }

        return "manager/show-user";
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable Integer id, Model model) {

        try {
            User user = provideUserToShow(id);
            CrmUser crmUser = new CrmUser();

            List<String[]> roles = new ArrayList<>();

            roles.add(new String[]{"ROLE_MANAGER", "مدیر"});
            roles.add(new String[]{"ROLE_SUPERVISOR", "ناظر"});
            roles.add(new String[]{"ROLE_EXPERT", "خبره"});
            roles.add(new String[]{"ROLE_ORDINARY", "عادی"});

            crmUser.setFirstName(user.getFirstName());
            crmUser.setLastName(user.getLastName());
            crmUser.setUserName(user.getUserName());
            String roleName = user.getRoles().stream().findFirst().get().getName();
            crmUser.setRoleName(roleName);


            model.addAttribute("roles", roles);
            model.addAttribute("crmUser", crmUser);
            model.addAttribute("userId", id);
        } catch (Exception e) {
            model.addAttribute("messageTitle", "خطا");
            model.addAttribute("messageContent", "خطای سرور");
            model.addAttribute("messageIcon", "error");
        }


        return "manager/edit-user";
    }

    private User provideUserToShow(Integer id) {
        User user = userService.findById(id);

        Long fullTimeExDate = settingService.getSetting().getExpireDate() * user.getPasswordExpirationTime();
        fullTimeExDate += Long.valueOf(user.getTimeStampExpireDate());

        String ex = String.valueOf(fullTimeExDate);

        user.setTimeStampCreatedDate(convertTimeStampToDate(user.getTimeStampCreatedDate()));
        user.setTimeStampExpireDate(convertTimeStampToDate(ex));
        user.setTimeStampLastLogin(convertTimeStampToDate(user.getTimeStampLastLogin()));

        return user;
    }

    @PostMapping("/register-user")
    public String registerUser(Authentication authentication,@Valid @ModelAttribute("crmUser") CrmUser crmUser,
                               BindingResult bindingResult, HttpServletRequest request) {

        try {
            User existingUser = userService.findByUserName(crmUser.getUserName());

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

            } else if (existingUser != null) {

                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", "نام کاربری تکراری است");
                request.getSession().setAttribute("messageIcon", "error");

            } else {

                crmUser.setRoles(convertRoleNameToCollectionRoles(crmUser.getRoleName()));
                userService.save(crmUser);

                String userName = authentication.getName();
                Log log = new Log();
                log.setEventType("Create User: "+crmUser.getUserName()+" Role: "+crmUser.getRoleName());
                log.setEventResult(1);
                log.setFormName("Create User");
                Long createUserTimeStamp = System.currentTimeMillis();

                log.setTimeStampStartDate(createUserTimeStamp);
                log.setTimeStampFinishDate(createUserTimeStamp);
                logService.save(userName, request, log);


                request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                request.getSession().setAttribute("messageContent", "کاربر با موفقیت ایجاد شد");
                request.getSession().setAttribute("messageIcon", "success");
            }
        } catch (Exception e) {

            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");

        }

        return "redirect:/manager/user/users";
    }


    @PostMapping("/update-user")
    public String updateUser(Authentication authentication,
                             @Valid @ModelAttribute("crmUser") CrmUser crmUser,
                            BindingResult bindingResult,
                             @RequestParam(name = "userId") Integer userId,
                            HttpServletRequest request) {
        try {

            boolean otherErrorField = false;

            if (bindingResult.hasErrors()) {

                String errors = "";
                for (Object object:bindingResult.getAllErrors()) {
                    if(object instanceof FieldError){
                        FieldError fieldError = (FieldError) object;

                        errors = errors +"   *"+ fieldError.getDefaultMessage() + ".   ";

                        if(!otherErrorField &&
                            !(fieldError.getDefaultMessage().equals("passwordRequired") ||
                             fieldError.getDefaultMessage().equals("طول رمز عبور حداقل 4 و حداکثر 32 حرف می تواند باشد"))){

                            otherErrorField = true;

                        }
                    }
                }


                if(otherErrorField){
                    request.getSession().setAttribute("messageTitle", "خطا");
                    request.getSession().setAttribute("messageContent", errors);
                    request.getSession().setAttribute("messageIcon", "error");
                }

            }

            if(!otherErrorField){

                Collection<Role> roles = new ArrayList<>();

                roles.add(roleService.findRoleByName(crmUser.getRoleName()));
                crmUser.setRoles(roles);
                userService.update(crmUser, userId);

                String userName = authentication.getName();
                Log log = new Log();
                log.setEventType("Update User: "+crmUser.getUserName()+" Role: "+crmUser.getRoleName());
                log.setEventResult(1);
                log.setFormName("Update User");
                Long updateUserTimeStamp = System.currentTimeMillis();


                log.setTimeStampStartDate(updateUserTimeStamp);
                log.setTimeStampFinishDate(updateUserTimeStamp);
                logService.save(userName, request, log);

                request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                request.getSession().setAttribute("messageContent", "کاربر با موفقیت ویرایش شد");
                request.getSession().setAttribute("messageIcon", "success");
            }

        } catch (Exception e) {

            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");


        }


        return "redirect:/manager/user/users";
    }

    private Collection<Role> convertRoleNameToCollectionRoles(String roleName) {
        Role role = whatsRole(roleName);
        Collection<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }


    @PostMapping("/remove-user")
    public String removeUser(@RequestParam(name = "userId") Integer userId,
                             HttpServletRequest request, Authentication auth) {

        try {

            String userName = auth.getName();
            User user = userService.findByUserName(userName);
            if (user.getId() == userId) {
                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", "نمی توانید خودتان را حذف کنید");
                request.getSession().setAttribute("messageIcon", "error");
            } else {

                userService.deleteById(userId);
                request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                request.getSession().setAttribute("messageContent", "کاربر با موفقیت حذف شد");
                request.getSession().setAttribute("messageIcon", "success");
            }


        } catch (Exception e) {

            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");


        }

        return "redirect:/manager/user/users";
    }

    @PostMapping("/state-user")
    public String changeStateUser(@RequestParam(name = "userId") Integer userId,
                             HttpServletRequest request, Authentication auth) {

        try {

            String userName = auth.getName();
            User authUser = userService.findByUserName(userName);
            if (authUser.getId() == userId) {
                request.getSession().setAttribute("messageTitle", "خطا");
                request.getSession().setAttribute("messageContent", "نمی توانید خودتان را غیرفعال کنید");
                request.getSession().setAttribute("messageIcon", "error");
            } else {
                User user = userService.findById(userId);
                String msg = "";
                Log log = new Log();

                if(user.getEnabled()){
                    msg = "کاربر با موفقیت غیرفعال شد";
                    log.setEventType("Disabled User: "+user.getUserName()+" Role: "+user.getRoles().stream().findFirst().get().getName());
                    log.setFormName("Disabled User");


                }else{
                    msg = "کاربر با موفقیت فعال شد";
                    log.setEventType("Enabled User: "+user.getUserName()+" Role: "+user.getRoles().stream().findFirst().get().getName());
                    log.setFormName("Enabled User");
                }
                log.setEventResult(1);
                Long stateUserTimeStamp = System.currentTimeMillis();


                log.setTimeStampStartDate(stateUserTimeStamp);
                log.setTimeStampFinishDate(stateUserTimeStamp);
                logService.save(userName, request, log);

                userService.changeStateById(user);
                request.getSession().setAttribute("messageTitle", "موفقیت آمیز");
                request.getSession().setAttribute("messageContent", msg);
                request.getSession().setAttribute("messageIcon", "success");
            }


        } catch (Exception e) {

            request.getSession().setAttribute("messageTitle", "خطا");
            request.getSession().setAttribute("messageContent", "خطای سرور");
            request.getSession().setAttribute("messageIcon", "error");


        }

        return "redirect:/manager/user/users";
    }


}
