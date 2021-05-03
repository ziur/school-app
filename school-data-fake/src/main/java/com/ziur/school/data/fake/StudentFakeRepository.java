package com.ziur.school.data.fake;

import com.ziur.school.core.Group;
import com.ziur.school.core.Student;
import com.ziur.school.core.StudentNotFoundException;
import com.ziur.school.core.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Named
public class StudentFakeRepository implements StudentRepository {
    @Resource(name = "bucketService")
    private FakeBucketService bucketService;

    @Override
    public Optional<Student> get(Long id) {
        return Optional.ofNullable(bucketService.getStudent(id));
    }

    @Override
    public Student create(Student student) {
        return bucketService.addStudent(student);
    }

    @Override
    public void update(Student param) {
        Student student = bucketService.getStudent(param.getId());
        if (Objects.isNull(student)) {
            throw new StudentNotFoundException("Cannot find student=" + param.getId());
        }
        student.update(param);
    }

    @Override
    public void delete(Long id) {
        bucketService.removeStudent(id);
    }

    @Override
    public Page<Student> list(Pageable pageable) {
        List<Student> students = bucketService.getStudents();
        int start = (int) pageable.getOffset();
        if (start > students.size()) {
            throw new IllegalArgumentException("Invalid page");
        }
        int end = Math.min(start + pageable.getPageSize(), students.size());
        return new PageImpl<>(students.subList(start, end), pageable, students.size());
    }

    @Override
    public void assignGroup(Student student, Group group) {
        student.addGroup(group);
        group.addStudent(student);
    }

    @Override
    public void unassignGroup(Student student, Group group) {
        student.removeGroup(group);
        group.removeStudent(student);
    }
}
