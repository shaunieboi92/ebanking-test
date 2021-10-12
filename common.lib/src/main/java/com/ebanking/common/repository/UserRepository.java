package com.ebanking.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebanking.common.model.User;
import com.ebanking.common.model.UserIBANMapping;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{


	User findByLoginIdAndDeleted(String loginId, Short false1);

	@Query(value = "select u.ibanList from User u where u.uuid = :uuid")
	List<UserIBANMapping> findIbanListByUuid(String uuid);

	@Query(value = "select u from User u where u.uuid = :uuid")
	User findByUuid(String uuid);

}
