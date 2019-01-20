package com.appstude.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.appstude.dao.UserRepository;
import com.appstude.entities.AppUser;
import com.appstude.service.AccountService;
import org.springframework.http.ResponseEntity;

@RestController
public class AccountRestController {

	@Autowired
	private AccountService accountService;
	
	
	//@Secured ({"ADMIN"})
	/*@PostMapping("/register")
	public AppUser save(@RequestBody RegisterForm userForm){

		return accountService.saveUser(userForm.getUsername(),userForm.getPassword(),userForm.getConfirmpassword());
	}
*/
	@PostMapping("/register")
	public ResponseEntity<?> save2(@RequestBody RegisterForm userForm){

		AppUser appUser = accountService.findUserByUserName(userForm.getUsername());

		if(appUser!=null) return new ResponseEntity<String>( "User already exists !", HttpStatus.FOUND);

		if(!userForm.getPassword().equals(userForm.getConfirmpassword())){
			return new ResponseEntity<String>( "Password not match", HttpStatus.CONFLICT);
		}

		AppUser appUserToSave = new AppUser();
		appUserToSave.setUsername(userForm.getUsername());
		appUserToSave.setActive(true);
		appUserToSave.setPassword(userForm.getPassword());

		appUserToSave = accountService.addUser(appUserToSave);
		accountService.addRoleToUser(appUserToSave.getUsername(),"USER");

		return new ResponseEntity<AppUser>( appUserToSave, HttpStatus.OK);


	}
}
