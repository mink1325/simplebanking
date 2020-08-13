package com.mkcode.simplebanking.service;

import com.mkcode.simplebanking.model.User;
import com.mkcode.simplebanking.repositories.UserRepository;
import com.mkcode.simplebanking.rest.security.AppUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class UsersService implements UserDetailsService {

    private final UserRepository repository;

    public UsersService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(format("User %s does not exists", username)));
    }

    public long getUserId(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .map(User::getUserId)
                .orElseThrow(() -> new UsernameNotFoundException(format("User %s does not exists", username)));
    }
}
