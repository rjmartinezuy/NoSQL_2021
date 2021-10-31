package com.nosql.comnosql.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.beans.RoleUpdater;
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

    public CustomError addRole(String email, RoleUpdater updateRole){
        System.out.println(email + ' ' + updateRole.getRole() + " " + updateRole.getPassword());
        Query query = getUserCollection().whereEqualTo("email", email);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {

            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document != null && document.exists()) {
                    System.out.println(document.getId());
                    User user = document.toObject(User.class);
                    /*if(!validateUser(email, updateRole.getPassword())){
                        return new CustomError(104, "Password not correct");
                    }*/
                    if(!user.getPassword().equals(updateRole.getPassword())){
                        return new CustomError(104, "Password not correct");
                    }
                    /*ArrayList<String> roles = user.getRole();
                    if(roles.isEmpty() || !roles.contains(updateRole.getRole())){
                        roles.add(updateRole.getRole());
                        return new CustomError(0, "Role added!");
                    }*/
                    return new CustomError(100, "Test exists");
                }
                return new CustomError(102, "User Not exists");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new CustomError(100, "Test");
    }

    private CollectionReference getUserCollection(){
        return firebase.getFirestore().collection("users");
    }

    public Boolean validateUser(String email, String password){
        Query query = getUserCollection()
                .whereEqualTo("email", email).whereEqualTo("password", password);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document != null && document.exists()) {
                    return Boolean.TRUE;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }
}
