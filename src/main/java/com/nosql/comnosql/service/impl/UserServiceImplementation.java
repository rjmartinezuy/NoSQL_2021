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

import java.util.*;
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
            error = new CustomError(101, "Usuario ya existe");
        }else{
            error = new CustomError(500, "Usuario no pudo agregarse. Reintente más tarde");

            Map<String, Object> docData = new HashMap<>();
            docData.put("name", user.getName());
            docData.put("lastname", user.getLastname());
            docData.put("email", user.getEmail());
            docData.put("password", user.getPassword());

            ApiFuture<WriteResult> writeResultApiFuture = getUserCollection().document(user.getEmail()).create(docData);

            try {
                if(writeResultApiFuture.get() != null){
                    error = new CustomError(200, "Usuario agregado");
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
                        roles = new ArrayList<String>();
                        for(String role: updateRole.getRoles()){
                            roles.add(role);
                        }
                    }
                    else if(!roles.contains(updateRole.getRoles())){
                        for(String role: updateRole.getRoles()){
                            roles.add(role);
                        }
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

    public CustomError deleteRole(String email, RoleUpdater userData){
        User user = this.find(email);

        if(user != null){
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
                return new CustomError(103, "No pudo eliminarse " + roles.get(index) + "ya que no está asociado al usuario");
            }
            for(String role: toDelete){
                currentRoles.remove(role);
            }
            DocumentReference doc = getUserCollection().document(email);
            ApiFuture<WriteResult> future = doc.update("roles", currentRoles);
            WriteResult result = null;
            try {
                result = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("Write result: " + result);
            return new CustomError(0, "Role added!");

        }
        /*User u = this.find(userData.getEmail());
        ArrayList<String> currentRoles = u.getRoles();
        ArrayList<String> toDelete = new ArrayList<>();

        for(String role : userData.getRoles()){
            if(currentRoles.contains(role)){
                toDelete.add(role);
            }
            else{
                return new CustomError(103, "No pudo eliminarse " + role + "ya que no está asociado al usuario");
            }
        }*/
        return new CustomError(0, "Test");
    }

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

    @Override
    public JSONObject validateUser(String email, String password){

        Query query = getUserCollection()
                .whereEqualTo("email", email).whereEqualTo("password", password);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        return new JSONObject(querySnapshot);
        /*
        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                if (document != null && document.exists()) {
                    return new JSONObject(Boolean.TRUE);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new JSONObject(Boolean.FALSE);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new JSONObject(Boolean.FALSE);
        }
        return new JSONObject(Boolean.FALSE);

         */
    }
}
