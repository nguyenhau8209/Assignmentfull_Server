package com.example.assignmentfull_server.model;

public class DataApi {
    private DataNasa dataNasa;
    private String message;
    private int status;

    public DataApi() {
    }

    public DataApi(DataNasa dataNasa, String message, int status) {
        this.dataNasa = dataNasa;
        this.message = message;
        this.status = status;
    }

    public DataNasa getDataNasa() {
        return dataNasa;
    }

    public void setDataNasa(DataNasa dataNasa) {
        this.dataNasa = dataNasa;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
