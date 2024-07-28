package dev.ducku.testcontainers.controllers;

import dev.ducku.testcontainers.entity.Course;
import dev.ducku.testcontainers.services.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.save(course);
    }


    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }
}
