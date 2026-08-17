package com.gpn.users;

import com.gpn.auth.security.AuthDetailsHolder;
import com.gpn.loghistory.service.LogHistoryService;
import com.gpn.users.exception.UserNotFoundException;
import com.gpn.users.model.UserEntity;
import com.gpn.users.model.dto.AddUserRequest;
import com.gpn.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LogHistoryService logHistoryService;

    @Transactional
    public void addUser(final AddUserRequest addUserRequest) {
        final var userEntity = new UserEntity();
        userEntity.setEmail(addUserRequest.email());
        final var saved = userRepository.save(userEntity);
        final var currentUser = AuthDetailsHolder.getCurrentUser();
        logHistoryService.saveAddUserLog(currentUser.id(), saved.getEmail());
    }

    public void getUserDetails() {

    }

    public void updateUserDetails() {
        final var userId = AuthDetailsHolder.getCurrentUser().id();
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id '%s' cannot be found".formatted(userId)));

    }

}
