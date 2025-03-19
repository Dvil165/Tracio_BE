package com.dvil.tracio.repository;

import com.dvil.tracio.entity.User;
import com.dvil.tracio.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    //boolean existsByUserIdAndRole(Integer id, RoleName role);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
