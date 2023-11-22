package com.sevilay.repository;

import com.sevilay.repository.entity.Auth;
import com.sevilay.utility.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);

    List<Auth> findAllOptionalByRole(Role role);

}
