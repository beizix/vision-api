package io.vision.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "app.upload.path=path-to-somewhere"
})
class VisionApplicationTests {

  @Test
  void contextLoads() {}
}
