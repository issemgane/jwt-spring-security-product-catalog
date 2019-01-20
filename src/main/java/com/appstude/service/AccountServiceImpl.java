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


	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public AccountServiceImpl(UserRepository userRepository, RoleRepository roleRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;

	}


	
	@Override
	public AppUser addUser(AppUser user) {
		String hasPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(hasPassword);
		user = userRepository.save(user);
		return user;
	}

	@Override
	public AppUser saveUser(String username, String password, String confirmPassword) {
		AppUser appUser = userRepository.findByUsername(username);
		if(appUser!=null) throw new RuntimeException("User already exists !");
		if(!password.equals(confirmPassword)){
			throw new RuntimeException("Password not match");
		}
		AppUser appUserToSave = new AppUser();
		appUserToSave.setUsername(username);
		appUserToSave.setActive(true);
		appUserToSave.setPassword(bCryptPasswordEncoder.encode(password));

		appUserToSave = userRepository.save(appUserToSave);
		addRoleToUser(appUserToSave.getUsername(),"USER");
		return appUserToSave;
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
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppRole appRole = roleRepository.findByRoleName(rolename);
		AppUser appUser = userRepository.findByUsername(username);
		//pas de save car tous les methodes sont transactionnel apres chaqueoperation jpa declanche un commit
		//puisque la collection est change donc JPA va faire un update dans la BD.
		appUser.getRoles().add(appRole);
		//userRepository.save(appUser);
	}

	@Override
	public List<AppRole> getLisOfRoles() {
		return roleRepository.findAll();
	}

}
