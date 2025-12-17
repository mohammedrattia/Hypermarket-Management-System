package com.hypermarket.entities;

import com.hypermarket.data.DataStore;
import com.hypermarket.modules.sales.MakeOrder;

public class Sales extends User {

    public Sales(String recordLine) {
        super(recordLine);
    }

    public Sales(String role, int id, String fName, String lName, String image, String phone,
            String email, String password,
            double salary) {
        super(role, id, fName, lName, image, phone, email, password, salary);
    }

    public Order MakeOrder() {
        Order newOrder = new Order(this);
        return newOrder;
    }

    public Return makeReturn(OrderItem orderItem, int quantityReturned) {
        Return newReturn = new Return(orderItem, quantityReturned);
        orderItem.setReturnedItems(orderItem.getReturnedItems() + quantityReturned);
        return newReturn;
    }
}
