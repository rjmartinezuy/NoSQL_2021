package com.nosql.comnosql.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.beans.RoleUpdater;
import com.nosql.comnosql.beans.User;
import com.nosql.comnosql.firebase.FireBaseInitializer;
import com.nosql.comnosql.service.CustomErrorService;
import com.nosql.comnosql.service.UserService;
import org.json.JSONObject;
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
    private CustomErrorService customErrorService;

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
        CustomError error = new CustomError();
        String email = user.getEmail();
        if (this.find(email) != null) {
            error.setCode(101);
            error.setDescription("Usuario ya existe");
        }else{
            error.setCode(500);
            error.setDescription("Usuario no pudo agregarse. Reintente más tarde");

            Map<String, Object> docData = new HashMap<>();
            docData.put("name", user.getName());
            docData.put("lastname", user.getLastname());
            docData.put("email", user.getEmail());
            docData.put("password", user.getPassword());

            ApiFuture<WriteResult> writeResultApiFuture = getUserCollection().document(user.getEmail()).create(docData);

            try {
                if(writeResultApiFuture.get() != null){
                    error.setCode(200);
                    error.setDescription("Usuario agregado");
                }
            } catch (Exception e) {
            }
        }
        return error;
    }

    public CustomError addRole(String email, RoleUpdater updateRole){
        Query query = getUserCollection().whereEqualTo("email", email);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document != null && document.exists()) {

                    User user = document.toObject(User.class);

                    if(!user.getPassword().equals(updateRole.getPassword())){
                        return new CustomError(104, "Password not correct");
                    }

                    ArrayList<String> roles = user.getRole();
                    if(roles == null){
                        System.out.println("Enter" + roles);
                        roles = new ArrayList<String>();
                        roles.add(updateRole.getRole());
                    }
                    else if(!roles.contains(updateRole.getRole())){
                        roles.add(updateRole.getRole());
                    }

                    DocumentReference doc = getUserCollection().document(document.getId());
                    ApiFuture<WriteResult> future = doc.update("roles", roles);
                    WriteResult result = future.get();
                    System.out.println("Write result: " + result);
                    return new CustomError(0, "Role added!");
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

    @Override
    public String authorize(String email, String password){
        if (this.validateUser(email, password) == null){
            return "{\"auth\": true}";
        } else {
            return "{\"auth\": false}";
        }
    }

    private User find(String email){
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture  = getUserCollection().document(email).get();

        try{
            DocumentSnapshot doc = documentSnapshotApiFuture.get();
            if (doc.exists()){
                return doc.toObject(User.class);
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }

    private CollectionReference getUserCollection(){
        return firebase.getFirestore().collection("users");
    }

    private CustomError validateUser(String email, String password){
        User usuario = this.find(email);

        if (usuario == null){
            return new CustomError(102, "Usuario no existe");
        }else {
            String uPassword = usuario.getPassword();
            if(uPassword.equals(password)) {
                return null;
            }
            return new CustomError(104, "Contraseña no válida");
        }
    }
}
