package com.practice.repository;

import com.practice.model.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission,Long> {
}
