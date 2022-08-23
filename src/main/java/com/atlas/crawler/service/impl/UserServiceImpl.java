/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service.impl;

import com.atlas.crawler.dao.UserRepository;
import com.atlas.crawler.entity.Role;
import com.atlas.crawler.entity.Setting;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.model.CrmUser;
import com.atlas.crawler.security.SecurityUserDetails;
import com.atlas.crawler.service.SettingService;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingService settingService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;




    @Override
    @Transactional
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    @Transactional
    public User findById(Integer id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;

        if (result.isPresent()) {
            user = result.get();
        } else {
            throw new RuntimeException("Did not find user id ");
        }

        return user;
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void save(CrmUser crmUser) {
        User user = new User();

        user.setUserName(crmUser.getUserName());
        user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
        user.setFirstName(crmUser.getFirstName());
        user.setLastName(crmUser.getLastName());
        user.setRoles(crmUser.getRoles());
        user.setTimeStampCreatedDate(String.valueOf(System.currentTimeMillis()));
        String expireAndLastLoginTimeStamp = "832435200000";
        user.setTimeStampExpireDate(expireAndLastLoginTimeStamp);
        user.setTimeStampLastLogin(expireAndLastLoginTimeStamp);
        user.setFailedAttempt(0);
        user.setTimeStampLockTime("0");
        user.setAccountNonLocked(true);
        user.setEnabled(true);


        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(CrmUser crmUser, Integer id) {

        User user = this.findById(id);
        user.setUserName(crmUser.getUserName());
        user.setFirstName(crmUser.getFirstName());
        user.setLastName(crmUser.getLastName());
        user.setRoles(crmUser.getRoles());
        if (crmUser.getPassword().length() != 0) {
            user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
            String expireAndLastLoginTimeStamp = "832435200000";
            user.setTimeStampExpireDate(expireAndLastLoginTimeStamp);
        }

        userRepository.save(user);

    }

    @Override
    @Transactional
    public void updateLastLogin(String userName) {
        User user = this.findByUserName(userName);
        String lastLoginTimeStamp = String.valueOf(System.currentTimeMillis());
        user.setTimeStampLastLogin(lastLoginTimeStamp);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Integer changePassword(String oldPassword, String newPassword, String newRepeatPassword, User user) {

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return 3;
        }else  if (oldPassword.equals(newPassword)) {
            return 2;
        }
        else {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setTimeStampExpireDate(String.valueOf(System.currentTimeMillis()));
            userRepository.save(user);
            return 4;
        }
    }

    @Override
    @Transactional
    public boolean isPasswordExpired(User user) {

        Setting setting = settingService.getSetting();
        Integer m = setting.getExpireDate();

        Long PASSWORD_EXPIRATION_TIME = user.getPasswordExpirationTime() * m;

        long currentTime = System.currentTimeMillis();
        long lastChangedTime = Long.valueOf(user.getTimeStampExpireDate());

        return currentTime > lastChangedTime + PASSWORD_EXPIRATION_TIME;

    }

    @Transactional
    @Override
    public boolean isEnabled(User user) {
        User newUser = userRepository.findByUserName(user.getUserName());
        return newUser.getEnabled();
    }

    @Override
    @Transactional
    public List<User> findUsersByRoles(Role role) {
        return userRepository.findUsersByRoles(role);
    }

    @Override
    @Transactional
    public List<User> findUsersByLastNameAndRoles(Role role, String lastName) {
        return userRepository.findUsersByRolesAndLastNameStartingWith(role, lastName);
    }

    @Override
    @Transactional
    public List<User> findUsersByLastName(String lastName) {
        return userRepository.findUsersByLastNameStartingWith(lastName);
    }


    @Override
    @Transactional
    public void increaseFailedAttempts(User user){
        Integer newFailAttempts = user.getFailedAttempt() + 1 ;
        userRepository.updateFailedAttempts(newFailAttempts,user.getUserName());
    }

    @Transactional
    @Override
    public void changeStateById(User user) {
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetFailedAttempts(String userName){
        userRepository.updateFailedAttempts(0,userName);
    }

    @Override
    @Transactional
    public void lock(User user){
        user.setAccountNonLocked(false);
        String time = String.valueOf(System.currentTimeMillis());
        user.setTimeStampLockTime(time);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean unlockWhenTimeExpired(User user){

        Long lockTimeInMillis = Long.valueOf(user.getTimeStampLockTime());
        Long currentTime = System.currentTimeMillis();

        Setting setting = settingService.getSetting();
        Long lockTimeDuration = Long.valueOf(setting.getLockTimeDuration()) * 60*60*1000;

        if(lockTimeInMillis + lockTimeDuration < currentTime){
            user.setAccountNonLocked(true);
            user.setTimeStampLockTime("0");
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");

        }

        return new SecurityUserDetails(user,mapRolesToAuthorities(user.getRoles()));

//        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
//                mapRolesToAuthorities(user.getRoles()));


    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


}
