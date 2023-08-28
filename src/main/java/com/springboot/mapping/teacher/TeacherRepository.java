package com.springboot.mapping.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

//    @Query(value = "select * from teacher t where n.course = ? ", nativeQuery = true)
//    List<Teacher> findAll();
    List<Teacher> findAllByCourse(String course);
    List<Teacher> findByCourseContaining(String course);

}