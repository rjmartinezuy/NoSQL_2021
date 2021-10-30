package com.nosql.comnosql.controllers;

import java.util.List;

import com.nosql.comnosql.service.PostManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nosql.comnosql.beans.*;

@Controller
public class StudentRegistrationController {

    @Autowired
    private PostManagementService service;

    @RequestMapping(method = RequestMethod.POST, value="/register/student")

    @ResponseBody
    public ResponseEntity registerStudent(@RequestBody Student student) {
        /*System.out.println("In registerStudent");
        StudentRegistrationReply stdregreply = new StudentRegistrationReply();
        StudentRegistration.getInstance().add(student);
        //We are setting the below value just to reply a message back to the caller
        stdregreply.setName(student.getName());
        stdregreply.setAge(student.getAge());
        stdregreply.setRegistrationNumber(student.getRegistrationNumber());
        stdregreply.setRegistrationStatus("Successful");*/

        return new ResponseEntity(service.registerStudent(student), HttpStatus.OK);
    }

}