package com.example.demo.student;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    private final StudentService studentService;

    // @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/student")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @RequestMapping("/student/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/student")
    public Alert addStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
        return new Alert("POST successfully");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/student/{id}")
    public Alert updateStudent(@PathVariable Long id, @RequestParam(required = false) String name, @RequestParam(required = false) String email) {
        studentService.updateStudent(id, name, email);
        return new Alert("PUT successfully");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/student/{id}")
    public Alert deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new Alert("Delete successfully");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/student/deleteAll")
    public void deleteAllStudent() {
        studentService.deleteAllStudent();
    }
}
