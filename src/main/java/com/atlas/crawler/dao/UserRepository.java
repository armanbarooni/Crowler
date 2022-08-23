/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.dao;

import com.atlas.crawler.entity.Role;
import com.atlas.crawler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUserName(String userName);

    List<User> findUsersByRoles(Role role);

    List<User> findUsersByRolesAndLastNameStartingWith(Role role,String lastName);

    List<User> findUsersByLastNameStartingWith(String lastName);

    @Query("UPDATE User u set u.failedAttempt=?1 where u.userName=?2")
    @Modifying
    void updateFailedAttempts(Integer failAttempts, String userName);

    List<User> findUsersByUserNameIn(String[] userName);
    List<User> findUsersByUserNameIn(Collection<String> userName);
    List<User> findUsersByUserNameIn(List<String> userName);

}
