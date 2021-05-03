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
@RequestMapping(StudentController.URI)
public class StudentController {
    protected static final String URI = "/students";
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentController(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @ApiPageable
    @RequestMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<Page<Student>> getStudents(@ApiIgnore @PageableDefault( size = 10, direction = Sort.Direction.DESC, sort = "firstName") Pageable pageable) {
        Page<Student> students = studentRepository.list(pageable);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @Operation(summary = "Get a Student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))
            }),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<Student> findById(@PathVariable Long id) {
        Student student = studentRepository.get(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find student=" + id));
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create new student")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody Student student, UriComponentsBuilder uriComponentsBuilder) {
        Student newStudent = studentRepository.create(student);
        UriComponents uriComponents = uriComponentsBuilder.path(URI + "/{studentId}").buildAndExpand(newStudent.getId());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping
    @Operation(summary = "Modify a student")
    public ResponseEntity<?> update(@RequestBody Student student, UriComponentsBuilder uriComponentsBuilder) {
        studentRepository.update(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping
    @Operation(summary = "Removes a student")
    public ResponseEntity<Void> deleteAccount(Long id) {
        studentRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/classes")
    @Operation(summary = "Assigned classes for a student")
    public ResponseEntity<Iterable<Group>> assignedClasses(@PathVariable Long id) {
        Student student = studentRepository.get(id).orElseThrow(() -> new ResourceNotFoundException("Cannot find student=" + id));
        return ResponseEntity.ok(student.getGroups());
    }

    @PatchMapping(value = "/{studentId}/classes/{classCode}")
    @Operation(summary = "Assigns a class to a student")
    public ResponseEntity<?> assignClass(@PathVariable Long studentId, @PathVariable String classCode) {
        Student student = studentRepository.get(studentId).orElseThrow(() -> new ResourceNotFoundException("Cannot find student=" + studentId));
        Group group = groupRepository.get(classCode).orElseThrow(() -> new ResourceNotFoundException("Cannot find group=" + classCode));

        studentRepository.assignGroup(student, group);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/{studentId}/classes/{classCode}")
    @Operation(summary = "Unassigns a class from a student")
    public ResponseEntity<?> unassignClass(@PathVariable Long studentId, @PathVariable String classCode) {
        Student student = studentRepository.get(studentId).orElseThrow(() -> new ResourceNotFoundException("Cannot find student=" + studentId));
        Group group = groupRepository.get(classCode).orElseThrow(() -> new ResourceNotFoundException("Cannot find group=" + classCode));

        studentRepository.unassignGroup(student, group);
        return ResponseEntity.accepted().build();
    }
}
