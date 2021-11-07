package com.nosql.comnosql.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.nosql.comnosql.beans.Authenticate;
import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.beans.RoleUpdater;
import com.nosql.comnosql.beans.User;
import com.nosql.comnosql.firebase.FireBaseInitializer;
import com.nosql.comnosql.service.CustomErrorService;
import com.nosql.comnosql.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImplementation implements UserService {

    private CustomError error_code = new CustomError();

    @Autowired
    private FireBaseInitializer firebase;
    @Autowired
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
        String email = user.getEmail();
        if (this.find(email) != null) {
            this.error_code = customErrorService.find(101);
        }else{
            error_code = customErrorService.find(500);
            Map<String, Object> docData = new HashMap<>();
            docData.put("name", user.getName());
            docData.put("lastname", user.getLastname());
            docData.put("email", user.getEmail());
            docData.put("password", user.getPassword());
            docData.put("roles", new ArrayList<String>());

            ApiFuture<WriteResult> writeResultApiFuture = getUserCollection().document(user.getEmail()).create(docData);

            try {
                if(writeResultApiFuture.get() != null){
                    this.error_code = customErrorService.find(200);
                }
            } catch (Exception e) {
            }
        }
        return this.error_code;
    }

    @Override
    public CustomError addRole(String email, RoleUpdater updateRole){
        if(!validateUser(email, updateRole.getPassword())){
            return this.error_code;
        }
        try {
            DocumentReference doc = getUserCollection().document(email);
            DocumentSnapshot document = doc.get().get();
            if (document.exists()) {
                User user = document.toObject(User.class);
                ArrayList<String> roles = user.getRole();
                boolean added = false;
                if(roles == null){
                    roles = new ArrayList<String>();
                    for(String role: updateRole.getRoles()){
                        roles.add(role);
                    }
                }
                else{
                    for(String role: updateRole.getRoles()){
                        if(!roles.contains(role)){
                            roles.add(role);
                            added = true;
                        }
                    }
                }

                if(added){
                    //DocumentReference doc = getUserCollection().document(document.getId());
                    ApiFuture<WriteResult> future = doc.update("roles", roles);
                    WriteResult result = future.get();
                    return customErrorService.find(0);
                }
                return null;
            }
            return customErrorService.find(102);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return customErrorService.find(100);
    }

    @Override
    public CustomError deleteRole(String email, RoleUpdater userData){
        if(!validateUser(email, userData.getPassword())){
            return this.error_code;
        }
        DocumentReference doc = getUserCollection().document(email);
        DocumentSnapshot document = null;
        try {
            document = doc.get().get();
            System.out.println(document.exists());
            if(document.exists()){
                User user = document.toObject(User.class);
                ArrayList<String> currentRoles = user.getRoles();
                ArrayList<String> toDelete = new ArrayList<>();
                ArrayList<String> roles = userData.getRoles();
                int index = 0;
                boolean must_delete = true;
                while (index < roles.size() && must_delete) {
                    String role = roles.get(index);
                    if(!currentRoles.contains(role)){
                        must_delete = false;
                    }
                    else{
                        index++;
                        toDelete.add(role);
                    }
                }
                if(!must_delete){
                    this.error_code = customErrorService.find(103);
                    String desc = String.format(this.error_code.getDescription(), roles.get(index));
                    this.error_code.setDescription(desc);
                    return this.error_code;
                }
                for(String role: toDelete){
                    currentRoles.remove(role);
                }
                //DocumentReference doc = getUserCollection().document(email);
                ApiFuture<WriteResult> future = doc.update("roles", currentRoles);
                WriteResult result = null;
                try {
                    result = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return customErrorService.find(0);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return customErrorService.find(102);
    }

    @Override
    public Authenticate authorize(String email, String password){
        return new Authenticate(this.validateUser(email, password));
    }

    @Override
    public User find(String email){
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

    public boolean validateUser(String email, String password){
        User user = this.find(email);
        if(user == null) {
            this.error_code = customErrorService.find(102);
            return false;
        }
        if(!user.getPassword().equals(password)){
            this.error_code = customErrorService.find(104);
            return false;
        }
        return true;
    }
}
