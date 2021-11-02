package com.nosql.comnosql.controllers;

import com.nosql.comnosql.beans.CustomError;
import com.nosql.comnosql.service.CustomErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/customerror")
public class CustomErrorController {

    @Autowired
    private CustomErrorService service;

    @GetMapping(value = "/list")
    public ResponseEntity list(){
        return new ResponseEntity(service.list(), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody CustomError error){

        return new ResponseEntity(service.add(error), HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity edit(@RequestBody CustomError error){
        return new ResponseEntity(service.edit(error.getCode(), error), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{code}/delete")
    public ResponseEntity delete(@PathVariable(value = "code") int code){
        return new ResponseEntity(service.delete(code), HttpStatus.OK);
    }

}
