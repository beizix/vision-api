package io.vision.api.support;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ActiveProfiles("test")
public class SecurityConfigTestWebAdapter {
  @GetMapping("/api/v1/auth/test")
  public String publicEndpoint() {
    return "ok";
  }

  @GetMapping("/api/v1/user/test")
  public String userEndpoint() {
    return "ok";
  }

  @GetMapping("/api/v1/manager/test")
  public String managerEndpoint() {
    return "ok";
  }
}
