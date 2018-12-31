package com.appstude.service;

import java.util.List;

import com.appstude.entities.*;

public interface AccountService {
	
   public AppUser addUser(AppUser user);
   public AppUser findUserByUserName(String username);
   public List<AppUser> getLisOfUsers();
   
   
   public AppRole addRole(AppRole role);
   public void addRoleToUser(String username, String rolename);
   public List<AppRole> getLisOfRoles();
   
   
}
