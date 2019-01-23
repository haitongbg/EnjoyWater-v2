package com.enjoywater.listener;

import com.enjoywater.model.Order;

public interface OrderListener {
    void cancelOrder(Order order);
    void goOrderDetails(Order order);
}
