package com.nosql.comnosql.service;

import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.beans.RoleUpdater;
import com.nosql.comnosql.beans.User;
import org.json.JSONObject;

import java.util.List;

public interface UserService {

    List<User> list();

    CustomError add(User user);

    CustomError addRole(String email, RoleUpdater request);

    JSONObject authorize(String email, String password);
}
