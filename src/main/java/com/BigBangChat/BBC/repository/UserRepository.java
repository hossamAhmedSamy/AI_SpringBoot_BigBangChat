package com.BigBangChat.BBC.repository;

import com.BigBangChat.BBC.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "users") // This explicitly sets the endpoint
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}

