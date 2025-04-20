package com.example.productservice10april.services;

import com.example.productservice10april.models.Product;

import java.util.List;

public interface ProductService {



   List<Product> getAllProducts();


   Product getSingleProduct(Long ProductId);

   Product createProduct(String image,
                         String description,
                         String title,
                         double price,
                         String category);




}
