package ru.markov.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.markov.application.entity.RoleEntity;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("SELECT r.name FROM RoleEntity r JOIN UserRole ur ON r.id = ur.role.id WHERE ur.user.username = :userId")
    List<String> findRolesByUserId(String userId);

}