package io.taylor.domain.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Long> {
    Optional<Log> findById(Long id);

    List<Log> findByMemberId(Long id);
}
