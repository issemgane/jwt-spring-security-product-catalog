package com.appstude.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appstude.entities.AppUser;

@Repository

public interface UserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByUsername(String username);
	public AppUser findByPassword(String username);
	
}
