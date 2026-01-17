package io.api.vision.useCases.login.application;

import io.api.vision.useCases.auth.application.JwtUseCase;
import io.api.vision.useCases.auth.application.model.AuthCmd;
import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.login.application.model.LoginCmd;
import io.api.vision.useCases.login.application.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final LoginPortOut loginPortOut;
    private final JwtUseCase jwtUseCase;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthToken operate(LoginCmd cmd) {
        LoginUser user = loginPortOut.loadUser(cmd.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(cmd.password(), user.password())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return jwtUseCase.createToken(new AuthCmd(user.email()));
    }
}
