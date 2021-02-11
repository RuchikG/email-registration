package com.ualr.scheduler.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails extends Registration implements UserDetails {

    public CustomUserDetails(final Registration registration){
        super(registration);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = super.getRoles();
        switch (role){
            case "STUDENT":
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            case "ADMIN":
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            case "ROOT":
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
