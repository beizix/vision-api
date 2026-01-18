package io.vision.api.useCases.signup.application.model;

import io.vision.api.common.application.enums.Role;

public record SignupCmd(String email, String password, String displayName, Role role) {}
