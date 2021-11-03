package com.nosql.comnosql.controllers;

import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.beans.RoleUpdater;
import com.nosql.comnosql.beans.User;
import com.nosql.comnosql.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.List;

@RestController
@RequestMapping(value="/v1/user")
public class UserController {

    @Autowired
    private UserService uservice;

    @PostMapping(value = "/add")
    public CustomError add(@RequestBody User user){
        return uservice.add(user);
        //return new ResponseEntity(uservice.add(user), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity list(){
        return new ResponseEntity(uservice.list(), HttpStatus.OK);
    }

    @PutMapping(value="/role/{email}")
    public CustomError addRole(@PathVariable(value="email") String email, @RequestBody RoleUpdater data){
        return uservice.addRole(email, data);
    }

    @GetMapping(value = "/auth")
    public String authorize(@RequestBody User user){
        return uservice.authorize(user.getEmail(), user.getPassword());
        //return new ResponseEntity(uservice.add(user), HttpStatus.OK);
    }

}
