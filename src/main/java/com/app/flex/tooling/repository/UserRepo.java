package com.app.flex.tooling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.flex.tooling.models.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Integer> {

}
