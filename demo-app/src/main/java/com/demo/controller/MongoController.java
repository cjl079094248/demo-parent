package com.demo.controller;

import com.demo.service.MongoDbService;
import com.demo.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    MongoDbService mongoDbService;

    @PostMapping("/save")
    public String saveObj(@RequestBody Student student) {
        return mongoDbService.saveObj(student);
    }

    @GetMapping("/findAll")
    public List<Student> findAll() {
        return mongoDbService.findAll();
    }

    @GetMapping("/findOne")
    public Student findOne(@RequestParam String no) {
        return mongoDbService.getStudentByNo(no);
    }

    @GetMapping("/findOneByName")
    public Student findOneByName(@RequestParam String name) {
        return mongoDbService.getStudentByName(name);
    }

    @PostMapping("/update")
    public String update(@RequestBody Student student) {
        return mongoDbService.updateStudent(student);
    }

    @PostMapping("/delOne")
    public String delOne(@RequestBody Student student) {
        return mongoDbService.deleteStudent(student);
    }

    @GetMapping("/delById")
    public String delById(@RequestParam String no) {
        return mongoDbService.deleteStudentByNo(no);
    }

    @GetMapping("/findlikes")
    public List<Student> findByLikes(@RequestParam String search) {
        return mongoDbService.findByLikes(search);
    }
}