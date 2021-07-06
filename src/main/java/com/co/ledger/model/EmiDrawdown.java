package com.co.ledger.model;

import lombok.Data;

import java.util.UUID;

@Data
public class EmiDrawdown {

    private String id;
    private Integer emiNo;
    private Double drawdownAmount;
    private Boolean settled;
    private Boolean paidExtra;
    private Double extraAmount;

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }
}
