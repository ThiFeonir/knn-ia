package com.example.thiago.knn_ia;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Scanner;

public class KMeans {
    private final int CONSTANTE_K = 3;
    private static ArrayList<OrderCluster> clustering;
    private static OrderCluster orderCluster;

    public KMeans() {
        clustering = new ArrayList<>();
        orderCluster = new OrderCluster();
    }

    public ArrayList<OrderCluster> getClustering() {
        return clustering;
    }

    public Boolean add(Order order, Boolean canAdd) {
        if (clustering.isEmpty()) {
            clustering.add(new OrderCluster(order.getCoordinators(), order));
            return true;
        }

        OrderCluster cluster = searchCluster(clustering, order);
        if (cluster != null) {
            cluster.setOrder(order);
            updateCenter(cluster);
            return true;
            // Chamar calcular ponto medio.
        } else if (canAdd) {
            clustering.add(new OrderCluster(order.getCoordinators(), order));
            return true;
        } else {
            return false;
        }
    }

    private void updateCenter(OrderCluster cluster) {
        double latitude = 0;
        double longitude = 0;
        for (int i = 0; i < cluster.getOrderCluster().size(); i++) {
            Order order = cluster.getOrderCluster().get(i);
            latitude += order.getCoordinators().getLatitude();
            longitude += order.getCoordinators().getLongitude();
        }
        cluster.setCenter(new Coordinators(latitude/cluster.getOrderCluster().size(),
                longitude/cluster.getOrderCluster().size()));

    }

    private OrderCluster searchCluster(ArrayList<OrderCluster> clustering, Order order) {
        for (int i = 0; i < clustering.size(); i++) {
            OrderCluster cluster = clustering.get(i);
            if (distance(
                    cluster.getCenter().getLatitude(),
                    cluster.getCenter().getLongitude(),
                    order.getCoordinators().getLatitude(),
                    order.getCoordinators().getLongitude())
                    <= CONSTANTE_K) return cluster;
        }
        return null;
    }

    public double distance(double origem_lat, double origem_lng, double destino_lat, double destino_lng)  {
        double c = 90 - (destino_lng);
        double b = 90 - (origem_lng);
        double a = destino_lat - origem_lat;
        double cos_a = Math.cos(b) * Math.cos(c) + Math.sin(c) * Math.sin(b) * Math.cos(a);
        double arc_cos = Math.acos(cos_a);
        return (40030 * arc_cos) / 360; //distancia
    }
}
