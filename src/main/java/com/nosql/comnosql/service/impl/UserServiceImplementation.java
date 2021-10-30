package com.nosql.comnosql.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.beans.User;
import com.nosql.comnosql.firebase.FireBaseInitializer;
import com.nosql.comnosql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private FireBaseInitializer firebase;

    @Override
    public List<User> list(){
        List<User> list = new ArrayList<>();
        User user;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getUserCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()){
                user = doc.toObject(User.class);
                list.add(user);
            }
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
        return list;
    }

    @Override
    public CustomError add(User user){
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", user.getName());
        docData.put("lastname", user.getName());
        docData.put("email", user.getEmail());
        docData.put("password", user.getPassword());

        ApiFuture<WriteResult> writeResultApiFuture = getUserCollection().document(user.getEmail()).create(docData);


        try {
            if(writeResultApiFuture.get() != null){
                return new CustomError(200, "Usuario agregado");
            }
            return new CustomError(500, "Usuario no pudo agregarse. Reintente más tarde");
        } catch (ExecutionException | InterruptedException e) {
            return new CustomError(101, "Usuario no pudo agregarse. Reintente más tarde");
        }
    }

    public CollectionReference getUserCollection(){
        return firebase.getFirestore().collection("users");
    }
}
