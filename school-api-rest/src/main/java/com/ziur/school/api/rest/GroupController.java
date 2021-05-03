package com.ziur.school.api.rest;

import com.ziur.school.core.Group;
import com.ziur.school.core.GroupRepository;
import com.ziur.school.core.Student;
import com.ziur.school.core.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping(GroupController.URI)
public class GroupController {
    protected static final String URI = "/classes";
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public GroupController(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @ApiPageable
    @Operation(summary = "Get all classes")
    @RequestMapping
    public ResponseEntity<Page<Group>> getGroup(@ApiIgnore @PageableDefault( size = 10, direction = Sort.Direction.DESC, sort = "title") Pageable pageable) {
        Page<Group> groups = groupRepository.list(pageable);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @Operation(summary = "Get a class by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Group.class))
            }),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)})
    @GetMapping(value = "/{classCode}")
    public ResponseEntity<Group> findById(@PathVariable String classCode) {
        Group group = groupRepository.get(classCode).orElseThrow(() -> new ResourceNotFoundException("Cannot find classCode=" + classCode));
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create new class")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Group group, UriComponentsBuilder uriComponentsBuilder) {
        Group newGroup = groupRepository.create(group);
        UriComponents uriComponents = uriComponentsBuilder.path(URI + "/{studentId}").buildAndExpand(newGroup.getCode());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping
    @Operation(summary = "Modify a class")
    public ResponseEntity<?> update(@RequestBody Group group, UriComponentsBuilder uriComponentsBuilder) {
        groupRepository.update(group);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping
    @Operation(summary = "Removes a class")
    public ResponseEntity<Void> deleteAccount(String classCode) {
        groupRepository.delete(classCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{classCode}/students")
    @Operation(summary = "Assigned students in a class")
    public ResponseEntity<Iterable<Student>> assignedStudents(@PathVariable String classCode) {
        Group group = groupRepository.get(classCode).orElseThrow(() -> new ResourceNotFoundException("Cannot find code=" + classCode));
        return ResponseEntity.ok(group.getStudents());
    }

    @PatchMapping(value = "/{classCode}/students/{studentId}")
    @Operation(summary = "Assigns a student to a class")
    public ResponseEntity<?> assignStudent(@PathVariable String classCode, @PathVariable Long studentId) {
        Student student = studentRepository.get(studentId).orElseThrow(() -> new ResourceNotFoundException("Cannot find student=" + studentId));
        Group group = groupRepository.get(classCode).orElseThrow(() -> new ResourceNotFoundException("Cannot find group=" + classCode));

        groupRepository.assignStudent(group, student);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/{classCode}/students/{studentId}")
    @Operation(summary = "Unassigns a student from a class")
    public ResponseEntity<?> unassignStudent(@PathVariable String classCode, @PathVariable Long studentId) {
        Student student = studentRepository.get(studentId).orElseThrow(() -> new ResourceNotFoundException("Cannot find student=" + studentId));
        Group group = groupRepository.get(classCode).orElseThrow(() -> new ResourceNotFoundException("Cannot find group=" + classCode));

        groupRepository.unassignStudent(group, student);
        return ResponseEntity.accepted().build();
    }
}
