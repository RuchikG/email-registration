package com.ualr.scheduler.service;

import com.ualr.scheduler.model.Registration;
import com.ualr.scheduler.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
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
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("Test"));
            User user = new User("test","test",authorityList);
            user.getAuthorities().add(buildSimpleGrantedAuthorities(registration.getRoles()));
            UserDetails userDetails = (UserDetails)new User(registration.getUsername(),registration.getPassword(), user.getAuthorities());
            return userDetails;
        } else {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
    }

    private static SimpleGrantedAuthority buildSimpleGrantedAuthorities(String roles){
        switch (roles) {
            case ("STUDENT"):
                return new SimpleGrantedAuthority("STUDENT");
            case ("ADMIN"):
                return new SimpleGrantedAuthority("ADMIN");
            case ("ROOT"):
                return new SimpleGrantedAuthority("ROOT");
        }
        return null;
    }
}
