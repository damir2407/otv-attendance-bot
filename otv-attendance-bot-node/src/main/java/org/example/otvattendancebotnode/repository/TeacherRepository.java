package org.example.otvattendancebotnode.repository;

import java.util.Optional;
import org.example.otvattendancebotnode.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByTelegramUserId(Long telegramUserId);
}
