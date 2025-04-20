package com.example.productservice10april.controllers;

import com.example.productservice10april.dtos.RequestBodyProductdto;
import com.example.productservice10april.models.Product;
import com.example.productservice10april.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProductController {

    //Post http method is used to create a new resource

    ProductService productService;

    public ProductController(@Qualifier("selfproductservice") ProductService productService){
        this.productService = productService;
    }
    /*
    this Qualifier is used to check what dependency we want to inject
     */

    @PostMapping("/products")
    public Product createProduct(@RequestBody RequestBodyProductdto request) {
        // create a product

        return productService.createProduct(
                request.getDescription(),
                request.getTitle(),
                request.getImage(),
                request.getPrice(),
                request.getCategory());

    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        /*
        1--> directly give api call to the fakestore api
        2--> productService.getProductService();
         */
        return productService.getSingleProduct(id);
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
       return productService.getAllProducts();

    }




    @PostMapping("/products/{id}")
    public void updateProduct(@PathVariable String id) {
        // update a product
    }

}




// every api call at the end of the day is a method call inside a controller class