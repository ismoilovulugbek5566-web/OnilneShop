package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private List<Product> products;
    private double totalPrice;
    private LocalDateTime orderDate;
    private OrderStatus status;

    public Order() {}

    public Order(Long id, Long userId, List<Product> products, double totalPrice, LocalDateTime orderDate, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.products = products;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
}
