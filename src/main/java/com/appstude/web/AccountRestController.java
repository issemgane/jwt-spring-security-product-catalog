package com.appstude.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.appstude.dao.UserRepository;
import com.appstude.entities.AppUser;
import com.appstude.service.AccountService;

@RestController
public class AccountRestController {

	@Autowired
	private AccountService accountService;
	
	
	//@Secured ({"ADMIN"})
	@PostMapping("/register")
	public AppUser save(@RequestBody RegisterForm userForm){
		if(!userForm.getPassword().equals(userForm.getConfirmpassword())){
			throw new RuntimeException("password not match !");
		}
		
		AppUser userr = accountService.findUserByUserName(userForm.getUsername());
		if(userr != null){
			throw new RuntimeException("Username already Exists !");
		}
		
		AppUser user = new AppUser();
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		
		 user = accountService.addUser(user);
		 accountService.addRoleToUser(user.getUsername(), "USER");
		 
		 return user;
	}
}
