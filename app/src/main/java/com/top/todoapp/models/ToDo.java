package com.top.todoapp.models;

public class ToDo {
    private int id;
    private String note;
    private int status;

    public ToDo(String note, int status) {
        this.note = note;
        this.status = status;
    }

    public ToDo(int id, String note, int status) {
        this.id = id;
        this.note = note;
        this.status = status;
    }

    public ToDo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
