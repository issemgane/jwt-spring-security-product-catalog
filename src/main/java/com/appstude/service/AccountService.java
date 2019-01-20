package com.appstude.service;

import java.util.List;

import com.appstude.entities.*;

public interface AccountService {

   //users
   public AppUser addUser(AppUser user);
   public AppUser saveUser(String username,String password,String confirmPassword);
   public AppUser findUserByUserName(String username);
   public List<AppUser> getLisOfUsers();
   

   //roles
   public AppRole addRole(AppRole role);
   public void addRoleToUser(String username, String rolename);
   public List<AppRole> getLisOfRoles();
   
   
}
