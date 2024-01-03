package org.example.otvattendancebotnode.repository;

import java.util.Optional;
import org.example.otvattendancebotnode.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
}
