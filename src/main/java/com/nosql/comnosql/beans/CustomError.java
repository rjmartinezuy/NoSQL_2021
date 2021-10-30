package com.nosql.comnosql.beans;

public class ErrorNOSQL {
    Integer code;
    String description;

    public ErrorNOSQL(){

    }

    public ErrorNOSQL(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
