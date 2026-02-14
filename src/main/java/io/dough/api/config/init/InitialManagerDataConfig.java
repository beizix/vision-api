package io.dough.api.config.init;

import io.dough.api.common.application.enums.Role;
import io.dough.api.useCases.auth.signup.application.SignupUseCase;
import io.dough.api.useCases.auth.signup.application.domain.model.SignupCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.init.data", havingValue = "true")
public class InitialManagerDataConfig implements CommandLineRunner {

  private final SignupUseCase signupUseCase;

  @Override
  public void run(String... args) {
    log.info("✦ Initializing manager data: Creating Super Manager account...");

    try {
      SignupCmd managerCmd =
          new SignupCmd("manager@dough.io", "manager1@#$", "SuperManager", Role.MANAGER);

      signupUseCase.operate(managerCmd);
      log.info("✦ Super Manager account created successfully.");
    } catch (Exception e) {
      log.warn("✦ Super Manager account initialization skipped: {}", e.getMessage());
    }
  }
}
