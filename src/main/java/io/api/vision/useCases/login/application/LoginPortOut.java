package io.api.vision.useCases.login.application;

import io.api.vision.useCases.login.application.model.LoginUser;
import java.util.Optional;

public interface LoginPortOut {
    Optional<LoginUser> loadUser(String email);
}
