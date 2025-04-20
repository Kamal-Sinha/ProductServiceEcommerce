package com.example.productservice10april;

import com.example.productservice10april.models.Category;
import com.example.productservice10april.repositories.CategoryRepository;
import com.example.productservice10april.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductService10AprilApplicationTests {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;


    @Test
    void contextLoads() {
    }

    @Test
    void testQueries(){
        Category category = categoryRepository.findById(1L);

        System.out.println("Fetched a category object");

        
        category.getProducts(); //this will fetch the products associated with the category

    }



}
