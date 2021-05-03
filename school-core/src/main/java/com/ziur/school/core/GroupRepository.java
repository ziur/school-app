package com.ziur.school.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GroupRepository {
    Optional<Group> get(String code);

    Group create(Group group);

    void update(Group group);

    void delete(String code);

    Page<Group> list(Pageable pageable);

    void assignStudent(Group group, Student student);

    void unassignStudent(Group group, Student student);
}
