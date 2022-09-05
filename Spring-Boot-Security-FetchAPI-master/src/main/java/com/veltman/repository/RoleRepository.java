package com.veltman.repository;

import org.springframework.data.repository.CrudRepository;
import com.veltman.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
