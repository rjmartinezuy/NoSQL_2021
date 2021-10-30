package com.nosql.comnosql.service;

import com.google.cloud.firestore.CollectionReference;
import com.nosql.comnosql.beans.Student;
import com.nosql.comnosql.beans.StudentRegistrationReply;

import java.util.List;

public interface PostManagementService {

    List<Student> getAllStudents();

    StudentRegistrationReply registerStudent(Student student);

    CollectionReference getCollection();
}