package dev.ducku.testcontainers.services;

import dev.ducku.testcontainers.entity.Course;
import dev.ducku.testcontainers.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course findById(int id) {
        return courseRepository.findById(id).orElseGet(null);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}
