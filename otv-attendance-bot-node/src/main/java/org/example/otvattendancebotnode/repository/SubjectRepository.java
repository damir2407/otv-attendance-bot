package org.example.otvattendancebotnode.repository;

import java.util.Optional;
import org.example.otvattendancebotnode.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByTitle(String name);

}
