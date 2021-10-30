package com.nosql.comnosql.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.nosql.comnosql.service.PostManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nosql.comnosql.beans.Student;
import com.nosql.comnosql.beans.StudentRegistration;

@Controller
public class StudentRetrieveController {

    @Autowired
    private PostManagementService service;

    @RequestMapping(method = RequestMethod.GET, value="/student/allstudent")

    @ResponseBody
    public ResponseEntity getAllStudents() {
        //return StudentRegistration.getInstance().getStudentRecords();
        return new ResponseEntity(service.getAllStudents(), HttpStatus.OK);
    }

}