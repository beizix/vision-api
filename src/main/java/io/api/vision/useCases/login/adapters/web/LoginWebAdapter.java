package io.api.vision.useCases.login.adapters.web;

import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.login.adapters.web.model.LoginReq;
import io.api.vision.useCases.login.adapters.web.model.LoginRes;
import io.api.vision.useCases.login.application.LoginUseCase;
import io.api.vision.useCases.login.application.model.LoginCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginWebAdapter {

    private final LoginUseCase loginUseCase;

    @PostMapping("/api/v1/auth/login")
    public LoginRes login(@RequestBody LoginReq req) {
        AuthToken token = loginUseCase.operate(new LoginCmd(req.email(), req.password()));
        return new LoginRes(token.accessToken(), token.refreshToken());
    }
}
