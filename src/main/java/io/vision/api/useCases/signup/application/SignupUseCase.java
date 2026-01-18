package io.vision.api.useCases.signup.application;

import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.signup.application.model.SignupCmd;

public interface SignupUseCase {
  AuthToken operate(SignupCmd cmd);
}
