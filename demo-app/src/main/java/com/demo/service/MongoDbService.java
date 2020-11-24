package com.demo.service;

import com.demo.vo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class MongoDbService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存对象
     * @param student
     * @return
     */
    public String saveObj(Student student) {
        mongoTemplate.save(student);
        return "添加成功";
    }

    /**
     * 查询所有
     * @return
     */
    public List<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    /***
     * 根据no查询
     * @param no
     * @return
     */
    public Student getStudentByNo(String no) {
        Query query = new Query(Criteria.where("no").is(no));
        return mongoTemplate.findOne(query, Student.class);
    }

    /**
     * 根据名称查询
     *
     * @param name
     * @return
     */
    public Student getStudentByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Student.class);
    }

    /**
     * 更新对象
     *
     * @param student
     * @return
     */
    public String updateStudent(Student student) {
        Query query = new Query(Criteria.where("no").is(student.getNo()));
        Update update = new Update().set("age", student.getAge());
        // updateFirst 更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, Student.class);
        // updateMulti 更新查询返回结果集的全部
        // mongoTemplate.updateMulti(query,update,Student.class);
        // upsert 更新对象不存在则去添加
        // mongoTemplate.upsert(query,update,Student.class);
        return "success";
    }

    /***
     * 删除对象
     * @param student
     * @return
     */
    public String deleteStudent(Student student) {
        mongoTemplate.remove(student);
        return "success";
    }

    /**
     * 根据no删除
     *
     * @param no
     * @return
     */
    public String deleteStudentByNo(String no) {
        // findOne
        Student student = getStudentByNo(no);
        // delete
        deleteStudent(student);
        return "success";
    }

    /**
     * 模糊查询
     * @param search
     * @return
     */
    public List<Student> findByLikes(String search){
        Query query = new Query();
        Criteria criteria = new Criteria();
        //criteria.where("name").regex(search);
        Pattern pattern = Pattern.compile("^.*" + search + ".*$" , Pattern.CASE_INSENSITIVE);
        criteria.where("name").regex(pattern);
        List<Student> lists = mongoTemplate.findAllAndRemove(query, Student.class);
        return lists;
    }
}
