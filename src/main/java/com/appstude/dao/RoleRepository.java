package com.appstude.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appstude.entities.AppRole;
import com.appstude.entities.AppUser;


@Repository
public interface RoleRepository extends JpaRepository<AppRole, Long>{
	public AppRole findByRoleName(String rolename);
}
