package com.example.assignmentfull_server.model;

import java.util.List;

public class DataServer {
    private List<DataNasa> dataNasas;
    private String message;
    private int status;

    public DataServer() {
    }

    public DataServer(List<DataNasa> dataNasas, String message, int status) {
        this.dataNasas = dataNasas;
        this.message = message;
        this.status = status;
    }

    public List<DataNasa> getDataNasas() {
        return dataNasas;
    }

    public void setDataNasas(List<DataNasa> dataNasas) {
        this.dataNasas = dataNasas;
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
