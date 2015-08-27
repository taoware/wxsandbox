package com.irengine.sandbox.web.rest.util;

public class Filter {

    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE
    }

    private String field;
    private Operator operator;
    private String value;

    public Filter(String field, Operator operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
