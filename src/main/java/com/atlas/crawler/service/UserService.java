/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service;

import com.atlas.crawler.entity.Role;
import com.atlas.crawler.entity.User;
import com.atlas.crawler.model.CrmUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{
	
	User findByUserName(String userName);
	
	User findById(Integer integer);
	
	List<User> findAll();
	
	void deleteById(Integer id);

	void save(CrmUser crmUser);

    List<User> findUsersByRoles(Role role);

    List<User> findUsersByLastNameAndRoles(Role role,String lastName);

	List<User> findUsersByLastName(String lastName);

    void update(CrmUser crmUser, Integer id);

    void updateLastLogin(String userName);

	Integer changePassword(String oldPassword, String newPassword, String newRepeatPassword,User user);

	boolean isPasswordExpired(User user);

	boolean isEnabled(User user);

	boolean unlockWhenTimeExpired(User user);

	void lock(User user);

	void resetFailedAttempts(String userName);

	void increaseFailedAttempts(User user);

    void changeStateById(User user);
}
