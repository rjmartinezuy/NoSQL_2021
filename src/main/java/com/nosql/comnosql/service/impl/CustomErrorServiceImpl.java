package com.nosql.comnosql.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.firebase.FireBaseInitializer;
import com.nosql.comnosql.service.CustomErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomErrorServiceImpl implements CustomErrorService {
    @Autowired
    private FireBaseInitializer firebase;

    @Override
    public List<CustomError> list() {
        List<CustomError> errores = new ArrayList<>();
        CustomError error;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try{
            for (DocumentSnapshot doc: querySnapshotApiFuture.get().getDocuments()){
                error = doc.toObject(CustomError.class);
                error.setCode(Integer.valueOf(doc.getId()));
                errores.add(error);
            }
            return errores;
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public Boolean add(CustomError error) {
        Map<String, Object> docErrores = getDocData(error);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(String.valueOf(error.getCode())).create(error);
        return getaBoolean(writeResultApiFuture);

    }

    @Override
    public Boolean edit(int code, CustomError error) {
        Map<String, Object> docErrores = getDocData(error);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(String.valueOf(code)).set(docErrores);
        return getaBoolean(writeResultApiFuture);
    }

    @Override
    public Boolean delete(int code) {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(String.valueOf(code)).delete();

        return getaBoolean(writeResultApiFuture);
    }

    @Override
    public CustomError find(int code) {
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture  = getCollection().document(String.valueOf(code)).get();

        try{
            DocumentSnapshot doc = documentSnapshotApiFuture.get();
            if (doc.exists()){
                System.out.println("Encontré mensaje con código " + code);
                return doc.toObject(CustomError.class);
            }
        }catch(Exception e){
            System.out.println("Excepción buscando código " + code);
            return null;
        }
        System.out.println("No existe mensaje con código " + code);
        return null;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("customerror");
    }

    private Map<String, Object> getDocData(CustomError error) {
        Map<String, Object> docErrores = new HashMap<>();
        docErrores.put("code", error.getCode());
        docErrores.put("description", error.getDescription());
        return docErrores;
    }

    private Boolean getaBoolean(ApiFuture<WriteResult> writeResultApiFuture) {
        try {
            if (null != writeResultApiFuture.get()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
