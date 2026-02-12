package io.vision.api;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VisionApplication {

  @Value("${app.timezone}")
  private String timezone;

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone(timezone));
  }

  public static void main(String[] args) {
    SpringApplication.run(VisionApplication.class, args);
  }
}
