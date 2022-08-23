/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service.impl;

import com.atlas.crawler.dao.RoleRepository;
import com.atlas.crawler.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlas.crawler.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;


	@Override
	@Transactional
	public Role findRoleByName(String name) {
		return roleRepository.findRoleByName(name);
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

}
