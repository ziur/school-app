package com.ziur.school.data.fake;

import com.ziur.school.core.Group;
import com.ziur.school.core.GroupRepository;
import com.ziur.school.core.Student;
import com.ziur.school.core.StudentNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Named
public class GroupFakeRepository implements GroupRepository {
    @Resource(name = "bucketService")
    private FakeBucketService bucketService;

    @Override
    public Optional<Group> get(String code) {
        return Optional.ofNullable(bucketService.getGroup(code));
    }

    @Override
    public Group create(Group group) {
        return bucketService.addGroup(group);
    }

    @Override
    public void update(Group param) {
        Group group = bucketService.getGroup(param.getCode());
        if (Objects.isNull(group)) {
            throw new StudentNotFoundException("Cannot find group=" + param.getCode());
        }
        group.update(param);
    }

    @Override
    public void delete(String code) {
        bucketService.removeGroup(code);
    }

    @Override
    public Page<Group> list(Pageable pageable) {
        List<Group> groups = bucketService.getGroups();
        int start = (int) pageable.getOffset();
        if (start > groups.size()) {
            throw new IllegalArgumentException("Invalid page");
        }
        int end = Math.min(start + pageable.getPageSize(), groups.size());
        return new PageImpl<>(groups.subList(start, end), pageable, groups.size());
    }

    @Override
    public void assignStudent(Group group, Student student) {
        group.addStudent(student);
        student.addGroup(group);
    }

    @Override
    public void unassignStudent(Group group, Student student) {
        group.removeStudent(student);
        student.removeGroup(group);
    }
}
