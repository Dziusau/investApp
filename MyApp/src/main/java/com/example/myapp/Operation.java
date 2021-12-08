package com.example.myapp;

import java.io.Serializable;

public class Operation implements Serializable {
    private String activeName;
    private String operationType;
    private String userName;
    private String date;
    private Integer amount;
    private String isSuccess;

    //region LifeCycle
    public Operation(String activeName, String userName, Integer amount, String operationType, String isSuccess, String date) {
        this.activeName = activeName;
        this.operationType = operationType;
        this.userName = userName;
        this.date = date;
        this.isSuccess = isSuccess;
        this.amount = amount;
    }
    //endregion

    //region getters
    public String getActiveName() {
        return activeName;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() { return date; }

    public Integer getAmount() { return amount; }

    public String getIsSuccess() {
        return isSuccess;
    }
    //endregion

    //region setters
    public void setActiveName (String activeName) {
        this.activeName = activeName;
    }

    public void setOperationType (String operationType) {
        this.operationType = operationType;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public void setDate (String date) { this.date = date; }

    public void setAmount(Integer amount) { this.amount = amount; }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }
    //endregion
}
