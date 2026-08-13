package com.fatih.taskflow.service;

import com.fatih.taskflow.config.AuthenticatedUser;
import com.fatih.taskflow.exception.UnauthenticatedException;
import com.fatih.taskflow.model.User;
import com.fatih.taskflow.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return user.id();
        }

        throw new UnauthenticatedException();
    }

    public User getCurrentUserReference() {
        return userRepository.getReferenceById(getCurrentUserId());
    }
}
