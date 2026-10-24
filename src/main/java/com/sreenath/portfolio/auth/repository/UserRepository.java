package com.sreenath.portfolio.auth.repository;

import com.sreenath.portfolio.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("""
            SELECT u
            FROM User u
            LEFT JOIN FETCH u.roles
            WHERE u.email=:email
            """)
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
