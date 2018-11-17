package com.enjoywater.listener;

import com.enjoywater.model.Product;

public interface ProductListener {
    void selectProduct(Product product);
    void updateProduct(Product product);
}
