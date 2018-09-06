package com.example.thiago.knn_ia;

import java.util.ArrayList;

public class OrderCluster {
    private Coordinators center;
    private static ArrayList<Order> ordersCluster ;

    public OrderCluster() {
        ordersCluster = new ArrayList<>();
    }

    public OrderCluster(Coordinators center, Order order) {
        setCenter(center);
        setOrder(order);
    }

    public Coordinators getCenter() {
        return center;
    }

    public void setCenter(Coordinators center) {
        this.center = center;
    }

    public Order getOrder(int position) {
        return ordersCluster.get(position);
    }

    public ArrayList<Order> getOrderCluster() {
        return ordersCluster;
    }

    public void setOrder(Order order) {
        ordersCluster.add(order);
    }
}
