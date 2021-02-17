package com.ualr.scheduler.repository;

import com.ualr.scheduler.model.Registration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface RegistrationRepository extends CrudRepository<Registration, String> {
    Registration findByUsernameIgnoreCase(String username);
    Registration findByConfirmationToken(String confirmationToken);
    List<Registration> findAllByisEnabledAndRolesNot(Boolean enabled,String role);
}
