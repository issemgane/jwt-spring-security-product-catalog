package com.appstude.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.appstude.entities.AppUser;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByUsername(String username);

	
}
