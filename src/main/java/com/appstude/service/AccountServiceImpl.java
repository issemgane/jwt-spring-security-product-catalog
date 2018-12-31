package com.appstude.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appstude.dao.RoleRepository;
import com.appstude.dao.UserRepository;
import com.appstude.entities.AppRole;
import com.appstude.entities.AppUser;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public AppUser addUser(AppUser user) {
		String hasPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(hasPassword);
		user = userRepository.save(user);
		return user;
	}

	@Override
	public AppUser findUserByUserName(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> getLisOfUsers() {
		
		return userRepository.findAll();
	}

	@Override
	public AppRole addRole(AppRole role) {
		// TODO Auto-generated method stub
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppRole appRole = roleRepository.findByRoleName(rolename);
		AppUser appUser = userRepository.findByUsername(username);
		appUser.getRoles().add(appRole);
		//userRepository.save(appUser);
	}

	@Override
	public List<AppRole> getLisOfRoles() {
		return roleRepository.findAll();
	}

}
