package com.ziur.school.data.fake;

import com.github.javafaker.Faker;
import com.ziur.school.core.Group;
import com.ziur.school.core.Student;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class FakeBucketService {
    private static final int MAX_STUDENTS = 50;
    private static final int MAX_GROUPS = 10;
    private static final int MAX_GROUP_ASSOCIATION = 5;
    private AtomicLong studentsIdHolder;
    private final Map<Long, Student> students;
    private final Map<String, Group> groups;

    public FakeBucketService() {
        studentsIdHolder = new AtomicLong();
        this.students = buildStudents();
        this.groups = buildGroups();
        List<String> keys = new ArrayList<>(groups.keySet());
        for (Student student : students.values()) {
            for (int i = 0; i < new Random().nextInt(MAX_GROUP_ASSOCIATION); i++) {
                String key = keys.get(new Random().nextInt(keys.size() - 1));
                student.addGroup(groups.get(key));
            }
        }
    }

    private Map<Long, Student> buildStudents() {
        Map<Long, Student> result = new LinkedHashMap<>(MAX_STUDENTS);
        Faker faker = new Faker();
        for (int i = 0; i < MAX_STUDENTS; i++) {
            Student student =  new Student(studentsIdHolder.getAndIncrement(), faker.name().firstName(), faker.name().lastName());
            result.put(student.getId(), student);
        }
        return result;
    }

    private Map<String, Group> buildGroups() {
        Map<String, Group> result = new LinkedHashMap<>(MAX_GROUPS);
        Faker faker = new Faker();
        for (int i = 0; i < MAX_GROUPS; i++) {
            Group group =  new Group(faker.code().asin(), faker.educator().course(), faker.lorem().characters(15, 30));
            result.put(group.getCode(), group);
        }
        return result;
    }

    public Student addStudent(Student student) {
        Student newStudent = student.create(studentsIdHolder.getAndIncrement());
        students.put(newStudent.getId(), newStudent);
        return newStudent;
    }

    public Group addGroup(Group group) {
        Group newGroup = group.create(new Faker().code().asin());
        groups.put(newGroup.getCode(), newGroup);
        return newGroup;
    }

    public Student getStudent(Long id) {
        return students.get(id);
    }

    public Group getGroup(String group) {
        return groups.get(group);
    }

    public void removeStudent(Long id) {
        Student removedStudent = students.remove(id);
        Iterator<Group> iterator = removedStudent.getGroups().iterator();
        while (iterator.hasNext()) {
            Group group = iterator.next();
            removedStudent.removeGroup(group);
            group.removeStudent(removedStudent);
        }
    }

    public void removeGroup(String group) {
        Group removedGroup = groups.remove(group);
        Iterator<Student> iterator = removedGroup.getStudents().iterator();
        while (iterator.hasNext()) {
            Student student = iterator.next();
            removedGroup.removeStudent(student);
            student.removeGroup(removedGroup);
        }
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Group> getGroups() {
        return new ArrayList<>(groups.values());
    }

}
