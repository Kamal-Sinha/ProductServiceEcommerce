package com.example.productservice10april.dtos;

import com.example.productservice10april.models.Category;
import com.example.productservice10april.models.Product;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FakeStoreProductdto {

    private long id;
    private String title;
    private String image;
    private String description;
    private double price;
    private String category;



    public Product toProduct(){

        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setImageURL(image);
        product.setDescription(description);
        product.setPrice(price);

        Category category1 = new Category();
        category1.setTitle(category);

        product.setCategory(category1);

        return product;


    }
}
