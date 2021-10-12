package com.ebanking.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebanking.common.model.User;
import com.ebanking.common.model.UserIBANMapping;


@Repository
public interface UserIBANMappingRepository extends JpaRepository<UserIBANMapping, Long>{

}
