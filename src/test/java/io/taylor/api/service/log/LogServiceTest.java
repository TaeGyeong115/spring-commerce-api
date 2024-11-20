package io.taylor.api.service.log;

import io.taylor.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class LogServiceTest extends IntegrationTestSupport {

    @AfterEach
    void tearDown() {
        logRepository.deleteAllInBatch();
    }

}
