package com.nosql.comnosql.service;

import com.nosql.comnosql.beans.CustomError;

import java.util.List;

public interface CustomErrorService {

    List<CustomError> list();

    Boolean edit(int code, CustomError error);

    Boolean delete(int code);

    CustomError find(int code);

    Boolean initialize();
}
