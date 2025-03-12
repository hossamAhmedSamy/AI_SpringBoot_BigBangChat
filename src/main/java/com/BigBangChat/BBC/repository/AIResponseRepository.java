package com.BigBangChat.BBC.repository;

import com.BigBangChat.BBC.entities.AIResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIResponseRepository extends JpaRepository<AIResponseEntity,Integer> {
}
