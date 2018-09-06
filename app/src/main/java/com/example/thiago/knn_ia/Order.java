package com.example.thiago.knn_ia;

public class Order {
    private String card_number;
    private Coordinators coordinators;

    public Order(String card_number, Coordinators coordinators) {
        this.card_number = card_number;
        this.coordinators = coordinators;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public Coordinators getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(Coordinators coordinators) {
        this.coordinators = coordinators;
    }
}
