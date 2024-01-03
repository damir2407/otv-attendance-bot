package org.example.otvattendancebotnode.repository;

import java.util.Optional;
import org.example.otvattendancebotnode.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByTelegramUserId(Long telegramUserId);
}
