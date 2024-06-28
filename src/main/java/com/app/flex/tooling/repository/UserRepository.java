package com.app.flex.tooling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.flex.tooling.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

	Optional<UserModel> findByUsername(String email);

	UserModel findByUserId(Integer userId);
}
