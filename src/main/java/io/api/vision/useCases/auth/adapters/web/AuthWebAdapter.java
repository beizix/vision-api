package io.api.vision.useCases.auth.adapters.web;

import io.api.vision.useCases.auth.adapters.web.model.RefreshReq;
import io.api.vision.useCases.auth.adapters.web.model.RefreshRes;
import io.api.vision.useCases.auth.adapters.web.model.ValidateReq;
import io.api.vision.useCases.auth.adapters.web.model.ValidateRes;
import io.api.vision.useCases.auth.application.JwtUseCase;
import io.api.vision.useCases.auth.application.model.AuthToken;
import io.api.vision.useCases.auth.application.model.RefreshTokenCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthWebAdapter {

    private final JwtUseCase jwtUseCase;

    @PostMapping("/refresh")
    public RefreshRes refresh(@RequestBody RefreshReq req) {
        AuthToken token = jwtUseCase.refreshToken(new RefreshTokenCmd(req.refreshToken()));
        return new RefreshRes(token.accessToken(), token.refreshToken());
    }

    @PostMapping("/validate")
    public ValidateRes validate(@RequestBody ValidateReq req) {
        boolean isValid = jwtUseCase.validateToken(req.token());
        return new ValidateRes(isValid);
    }
}
