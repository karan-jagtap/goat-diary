package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class GenericModel implements Serializable {
    private String srno, field;

    public GenericModel() {
    }

    public GenericModel(String srno, String field) {
        this.srno = srno;
        this.field = field;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
