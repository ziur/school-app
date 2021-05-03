package com.ziur.school.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentRepository {
    Optional<Student> get(Long id);

    Student create(Student student);

    void update(Student student);

    void delete(Long id);

    Page<Student> list(Pageable pageable);

    void assignGroup(Student student, Group group);

    void unassignGroup(Student student, Group group);
}
