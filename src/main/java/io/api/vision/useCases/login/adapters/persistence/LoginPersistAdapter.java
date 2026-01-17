package io.api.vision.useCases.login.adapters.persistence;

import io.api.vision.common.adapters.persistence.entity.UserEntity;
import io.api.vision.common.adapters.persistence.repository.UserRepository;
import io.api.vision.useCases.login.application.LoginPortOut;
import io.api.vision.useCases.login.application.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginPersistAdapter implements LoginPortOut {

    private final UserRepository userRepository;

    @Override
    public Optional<LoginUser> loadUser(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> new LoginUser(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getPassword(),
                        entity.getRole()
                ));
    }
}
