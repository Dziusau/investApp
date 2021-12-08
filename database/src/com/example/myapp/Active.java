package com.example.myapp;

import java.io.Serializable;

public class Active implements Serializable {

    private String activeName;
    private String activeType;
    private Integer price;
    private Integer amount;
    private String access;

    //region LifeCycle
    public Active(String activeName, String activeType, Integer price) {
        this.activeName = activeName;
        this.activeType = activeType;
        this.price = price;
        this.amount = 0;
    }

    public Active(String activeName, String activeType, Integer price, Integer amount) {
        this.activeName = activeName;
        this.activeType = activeType;
        this.price = price;
        this.amount = amount;
    }

    public Active(String activeName, String activeType, Integer price, String access) {
        this.activeName = activeName;
        this.activeType = activeType;
        this.price = price;
        this.access = access;
    }
    //endregion

    //region getters
    public String getActiveName() {
        return activeName;
    }

    public String getActiveType() {
        return activeType;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getAmount() { return amount; }

    public String getAccess() {
        return access;
    }
    //endregion

    //region setters
    public void setActiveName (String activeName) {
        this.activeName = activeName;
    }

    public void setActiveType (String activeType) {
        this.activeType = activeType;
    }

    public void setPrice (Integer price) {
        this.price = price;
    }

    public void setAmount(Integer amount) { this.amount = amount; }

    public void setAccess(String access) {
        this.access = access;
    }
    //endregion
}
