package com.app.flex.tooling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.flex.tooling.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	Role findByRoleId(String string);

}
