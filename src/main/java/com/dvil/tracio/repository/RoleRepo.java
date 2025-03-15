package com.dvil.tracio.repository;

import com.dvil.tracio.enums.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends CrudRepository<RoleName, Integer> {
    Optional<RoleName> findByName(RoleName roleName);
}
