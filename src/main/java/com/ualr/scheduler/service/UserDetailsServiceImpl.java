package com.ualr.scheduler.service;

import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Registration registration = registrationRepository.findByUsernameIgnoreCase(username);
        if (registration != null){
            return new User(registration.getUsername(),registration.getPassword(), buildSimpleGrantedAuthorities(registration.getRoles()));
        } else {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
    }

    private static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(String roles){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        switch (roles) {
            case ("STUDENT"):
                authorities.add(new SimpleGrantedAuthority("STUDENT"));
                break;
            case ("ADMIN"):
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                break;
            case ("ROOT"):
                authorities.add(new SimpleGrantedAuthority("ROOT"));
                break;
        }
        return authorities;
    }
}
