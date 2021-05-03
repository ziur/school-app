package com.ziur.school.core;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Student {
    @JsonView(Views.Public.class)
    @Getter
    private Long id;

    @JsonView(Views.Public.class)
    @Getter
    private String firstName;

    @JsonView(Views.Public.class)
    @Getter
    private String lastName;

    @JsonView(Views.Internal.class)
    @Getter
    Set<Group> groups;

    public Student(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        groups = new HashSet<>();
    }

    public Student create(Long id) {
        return new Student(id, firstName, lastName);    }

    public void update(Student newValues) {
        this.firstName = newValues.firstName;
        this.lastName = newValues.lastName;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }
}
