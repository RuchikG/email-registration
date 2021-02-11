package com.ualr.scheduler.service;

import com.ualr.scheduler.model.CustomUserDetails;
import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        if (registration != null){
            return new CustomUserDetails(registration);
        } else {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
    }
}
