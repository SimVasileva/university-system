package com.university.universityproject.service.impl;

import com.university.universityproject.model.UserEntity;
import com.university.universityproject.model.UserRoleEntity;
import com.university.universityproject.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository
                .findByEmail(email)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found."));
    }

    private UserDetails map(UserEntity userEntity) {

//
//        return  User.builder().username( userEntity.getEmail()).password( userEntity.getPassword())
//                .roles(userEntity.getRoles().stream().map(userRoleEntity -> userRoleEntity.getRole().name()).toList().toArray(new String[0]))
//                .build();
        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                asGrantedAuthorities(userEntity.getRoles())
        );
    }

    private List<GrantedAuthority> asGrantedAuthorities(List<UserRoleEntity> roleEntities) {
        return roleEntities
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name()))
                .collect(Collectors.toList());
    }
}