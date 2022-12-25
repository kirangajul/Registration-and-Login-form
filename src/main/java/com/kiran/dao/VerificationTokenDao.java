package com.kiran.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.entity.VerificationToken;

@Repository
public interface VerificationTokenDao extends JpaRepository<VerificationToken, Long>{

	VerificationToken findByToken(String token);
	

} 
