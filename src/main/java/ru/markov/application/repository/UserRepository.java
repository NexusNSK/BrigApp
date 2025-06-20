package ru.markov.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.markov.application.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}