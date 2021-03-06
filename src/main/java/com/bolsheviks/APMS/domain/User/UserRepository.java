package com.bolsheviks.APMS.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    int countUserById(UUID id);
    Optional<User> findFirstByLoginAndPassword(String login, String password);

    Page<User> findAllByIdNotOrderByLastName(UUID uuid, Pageable pageable);
}