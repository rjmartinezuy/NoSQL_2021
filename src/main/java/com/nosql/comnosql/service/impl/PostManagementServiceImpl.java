package com.nosql.comnosql.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.nosql.comnosql.beans.Student;
import com.nosql.comnosql.beans.StudentRegistrationReply;
import com.nosql.comnosql.firebase.FireBaseInitializer;
import com.nosql.comnosql.service.PostManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PostManagementServiceImpl implements PostManagementService {

    @Autowired
    private FireBaseInitializer firebase;

    @Override
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        Student student;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()){
                student = doc.toObject(Student.class);
                student.setId(doc.getId());
                list.add(student);
            }
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
        return list;
    }

    @Override
    public StudentRegistrationReply registerStudent(Student student) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", student.getName());
        docData.put("age", student.getAge());
        docData.put("id", student.getId());
        docData.put("RegistrationNumber", student.getRegistrationNumber());

        CollectionReference students = firebase.getFirestore().collection("students");
        ApiFuture<WriteResult> writeResultApiFuture = students.document().create(docData);

        StudentRegistrationReply stdregreply = new StudentRegistrationReply();
        try {
            if(writeResultApiFuture.get() != null){
                stdregreply.setRegistrationStatus("Successful");
            }
            else stdregreply.setRegistrationStatus("Cueck");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return stdregreply;
    }

    public CollectionReference getCollection(){
        return firebase.getFirestore().collection("students");
    }
}
