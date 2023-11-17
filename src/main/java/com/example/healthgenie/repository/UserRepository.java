package com.example.healthgenie.repository;

import com.example.healthgenie.domain.user.entity.AuthProvider;
import com.example.healthgenie.domain.user.entity.Role;
import com.example.healthgenie.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // test code 에서 사용
//    User findByEmailId(@Param("email") String email);

    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);
    User findByRoleAndId(Role role, Long Id);
    Optional<User> findByEmailAndName(String email,String name);
    boolean existsByIdAndAuthProvider(Long id, AuthProvider authProvider);

    boolean existsByEmailAndAuthProvider(String email, AuthProvider authProvider);

    boolean existsByEmail(String email);
}
