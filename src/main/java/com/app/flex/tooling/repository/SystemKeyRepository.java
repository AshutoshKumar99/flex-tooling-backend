package com.app.flex.tooling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.flex.tooling.models.SystemKey;

@Repository
public interface SystemKeyRepository extends JpaRepository<SystemKey, Long> {

	SystemKey findByModule(String string);

}
