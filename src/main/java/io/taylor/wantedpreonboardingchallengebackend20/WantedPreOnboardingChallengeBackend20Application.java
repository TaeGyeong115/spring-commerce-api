package io.taylor.wantedpreonboardingchallengebackend20;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WantedPreOnboardingChallengeBackend20Application {

    public static void main(String[] args) {
        SpringApplication.run(WantedPreOnboardingChallengeBackend20Application.class, args);
    }
}
