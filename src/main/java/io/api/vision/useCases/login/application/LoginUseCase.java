package io.api.vision.useCases.login.application;

import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.login.application.model.LoginCmd;

public interface LoginUseCase {
    AuthToken operate(LoginCmd cmd);
}
