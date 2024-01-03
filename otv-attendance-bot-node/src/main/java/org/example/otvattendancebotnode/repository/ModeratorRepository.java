package org.example.otvattendancebotnode.repository;

import java.util.Optional;
import org.example.otvattendancebotnode.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {

    Optional<Moderator> findByTelegramUserId(Long telegramUserId);


}
