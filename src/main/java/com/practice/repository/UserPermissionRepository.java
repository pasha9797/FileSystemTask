package com.practice.repository;

import com.practice.model.UserPermission;
import org.springframework.data.repository.CrudRepository;

public interface UserPermissionRepository extends CrudRepository<UserPermission,Long> {
}
