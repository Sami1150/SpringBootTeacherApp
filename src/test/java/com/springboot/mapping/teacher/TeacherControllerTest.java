package com.springboot.mapping.teacher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;
    @Test
    public void testFindAllTeachers() throws Exception {
        List<Teacher> teachers = Arrays.asList(
                new Teacher(1, "Science", 50000),
                new Teacher(2, "Math", 60000),
                new Teacher(3, "History", 45000)
        );

        when(teacherService.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/v1/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"id\":1,\"course\":\"Science\",\"salary\":50000},{\"id\":2,\"course\":\"Math\",\"salary\":60000},{\"id\":3,\"course\":\"History\",\"salary\":45000}]}"));

        verify(teacherService, times(1)).findAll();
    }

    // Test Get Teacher by ID:
    @Test
    public void testFindById() throws Exception {
        // Create a sample teacher
        Teacher teacher = new Teacher(1, "Science", 50000);

        // Mock the service method
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherService.findById(999L)).thenReturn(null); // Non-existent teacher

        // Test the controller method for existing teacher
        mockMvc.perform(get("/api/v1/teacher/1")
                        .with(testUser("reporter","REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.course", equalTo("Science")))
                .andExpect(jsonPath("$.salary", equalTo(50000)))
                .andReturn();

        // Test the controller method for non-existent teacher
        mockMvc.perform(get("/api/v1/teacher/999")
                .with(testUser("reporter","REPORTER"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    // Test to Verify Post Method
    @Test
    public void testPost() throws Exception {
        Teacher newTeacher = new Teacher(4, "Math", 60000); // Creating a new teacher object

        // Mock the service method
        when(teacherService.create(any(Teacher.class))).thenReturn(newTeacher);

        // Test the controller method for creating a new teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teacher/")
                        .with(testUser("reporter","REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"id\": 4, \"course\": \"Math\", \"salary\": 60000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(4)))
                .andExpect(jsonPath("$.course", equalTo("Math")))
                .andExpect(jsonPath("$.salary", equalTo(60000)))
                .andReturn();
    }



    // Test to verify PUT or UPDATE Method
    @Test
    public void testUpdate() throws Exception {
        Teacher updatedTeacher = new Teacher(4, "Biology", 55000); // Creating an updated teacher object

        // Mock the service method
        when(teacherService.update(eq(4L), any(Teacher.class))).thenReturn(updatedTeacher);

        // Test the controller method for updating a teacher
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/teacher/4/")
                        .with(testUser("reporter", "REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"id\": 4, \"course\": \"Biology\", \"salary\": 55000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(4)))
                .andExpect(jsonPath("$.course", equalTo("Biology")))
                .andExpect(jsonPath("$.salary", equalTo(55000)))
                .andReturn();
    }

    // Test to verify DELETE Method
    @Test
    public void testDeleteResource() throws Exception {
        long existingTeacherId = 4L;
        long missingTeacherId = 999L; // Non-existent teacher ID

        // Mock the service method for existing teacher
        when(teacherService.delete(existingTeacherId)).thenReturn(true);

        // Mock the service method for missing teacher
        when(teacherService.delete(missingTeacherId)).thenReturn(false);

        // Perform the delete request for existing teacher
        mockMvc.perform(delete("/api/v1/teacher/{id}", existingTeacherId)
                        .with(testUser("reporter", "REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Resource deleted successfully"));

        // Perform the delete request for missing teacher
        mockMvc.perform(delete("/api/v1/teacher/{id}", missingTeacherId)
                        .with(testUser("reporter", "REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found or could not be deleted"));

        // Verify that the delete method of teacherService is called with the correct IDs
        verify(teacherService, times(1)).delete(existingTeacherId);
        verify(teacherService, times(1)).delete(missingTeacherId);
    }


    private RequestPostProcessor testUser(String userName, String authoriy) {
        return SecurityMockMvcRequestPostProcessors.user(userName).authorities(new SimpleGrantedAuthority(authoriy));
    }
}
