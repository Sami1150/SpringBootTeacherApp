package com.springboot.mapping.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {
    private final TeacherService service;
    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Teacher>>> findAll(@RequestParam(name = "course", required = false) String course) {
        List<Teacher> teachers;

        if (course != null && !course.isEmpty()) {
            teachers = service.findByCourseContaining(course);
        } else {
            teachers = service.findAll();
        }

        if (teachers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of("content", teachers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> findById(@PathVariable("id") Long id) {
        Teacher teacher = service.findById(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/")
    public ResponseEntity<Teacher> create(@RequestBody Teacher teacher) {
        Teacher created = service.create(teacher);
        if (created ==null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(created);
    }

    //PUT Mapping
    @PutMapping("/{id}/")
    public ResponseEntity<Teacher> update(@PathVariable long id, @RequestBody Teacher teacher) {
        Teacher updated = service.update(id, teacher);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable long id) {
//        boolean deleted = service.delete(id);
//        if (deleted) {
//            return ResponseEntity.noContent().build(); //204 No content
//        }
//        return ResponseEntity.notFound().build();  //404 Not Found
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Resource deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found or could not be deleted");
    }




}
