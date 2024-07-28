package dev.ducku.testcontainers.repositories;

import dev.ducku.testcontainers.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
