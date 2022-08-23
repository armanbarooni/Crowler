/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.dao;

import com.atlas.crawler.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {

    Role findRoleByName(String name);
}
