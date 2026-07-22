package com.hireflow.repository;

import com.hireflow.entity.User;
import com.hireflow.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole(Role role);

    Page<User> findAll(Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);

    @Query(value = """
            SELECT TO_CHAR(created_at, 'YYYY-MM') AS month,
                   COUNT(*) AS total
            FROM users
            GROUP BY TO_CHAR(created_at, 'YYYY-MM')
            ORDER BY month ASC
            """, nativeQuery = true)
    List<Object[]> countMonthlyRegistrations();
}