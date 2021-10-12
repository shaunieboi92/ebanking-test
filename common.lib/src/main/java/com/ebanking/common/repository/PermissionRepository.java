package com.ebanking.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebanking.common.model.Permissions;
import com.ebanking.common.model.User;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Long> {

	Permissions findByPermission(String string);

}
