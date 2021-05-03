package com.ziur.school.core;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Data
public class Group {
    @JsonView(Views.Public.class)
    @Getter
    private String code;

    @JsonView(Views.Public.class)
    @Getter
    private String title;

    @JsonView(Views.Public.class)
    @Getter
    private String description;

    @JsonView(Views.Internal.class)
    @Getter
    private Set<Student> students;

    public Group(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
        students = new HashSet<>();
    }

    public Group create(String code) {
        return new Group(code, title, description);
    }

    public void update(Group newValues) {
        this.title = newValues.title;
        this.description = newValues.description;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }
}
