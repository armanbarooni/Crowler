/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service;

import com.atlas.crawler.entity.Role;

import java.util.List;

public interface RoleService {

	Role findRoleByName(String name);
	List<Role> findAll();
}
