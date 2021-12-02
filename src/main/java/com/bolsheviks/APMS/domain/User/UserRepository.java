package com.bolsheviks.APMS.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    int countUserById(UUID id);

    Optional<User> findFirstByLoginAndPassword(String login, String password);

    Page<User> findAllByIdNotOrderByLastName(UUID uuid, Pageable pageable);

    int countAllBy();

    @Query("select u from User u " +
            "where u.lastName like concat(:arg1, '%') " +
            "or u.firstName like concat(:arg1, '%') " +
            "or u.patronymic like concat(:arg1, '%')")
    List<User> find(String arg1);

    @Query("select u from User u " +
            "where (u.lastName like concat(:arg1, '%') and u.firstName like concat(:arg2, '%')) " +
            "or (u.lastName like concat(:arg1, '%') and u.patronymic like concat(:arg2, '%')) " +
            "or (u.firstName like concat(:arg1, '%') and u.lastName like concat(:arg2, '%')) " +
            "or (u.firstName like concat(:arg1, '%') and u.patronymic like concat(:arg2, '%')) " +
            "or (u.patronymic like concat(:arg1, '%') and u.lastName like concat(:arg2, '%')) " +
            "or (u.patronymic like concat(:arg1, '%') and u.firstName like concat(:arg2, '%'))")
    List<User> find(String arg1, String arg2);

    @Query("select u from User u " +
            "where (u.lastName like concat(:arg1, '%') and u.firstName like concat(:arg2, '%') " +
                                                    "and u.patronymic like concat(:arg3, '%')) " +
            "or (u.lastName like concat(:arg1, '%') and u.patronymic like concat(:arg2, '%') " +
                                                    "and u.firstName like concat(:arg3, '%')) " +
            "or (u.firstName like concat(:arg1, '%') and u.lastName like concat(:arg2, '%') " +
                                                    "and u.patronymic like concat(:arg3, '%')) " +
            "or (u.firstName like concat(:arg1, '%') and u.patronymic like concat(:arg2, '%') " +
                                                    "and u.lastName like concat(:arg3, '%')) " +
            "or (u.patronymic like concat(:arg1, '%') and u.lastName like concat(:arg2, '%') " +
                                                    "and u.firstName like concat(:arg3, '%')) " +
            "or (u.patronymic like concat(:arg1, '%') and u.firstName like concat(:arg2, '%') " +
                                                    "and u.lastName like concat(:arg3, '%'))")
    List<User> find(String arg1, String arg2, String arg3);

    int countByRole(Role role);
}