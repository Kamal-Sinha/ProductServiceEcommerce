package com.example.productservice10april.services;

import com.example.productservice10april.dtos.FakeStoreProductdto;
import com.example.productservice10april.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode; 
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakestore")
public class FakeStoreProductService implements ProductService{


    private  RestTemplate restTemplate;


    private int productId;


    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long ProductId) {

        if (productId == 0){
            throw new IllegalArgumentException("Invalid Product Id, Please try out other Product Id");
        }

        ResponseEntity<FakeStoreProductdto> fakeStoreProductdtoResponse = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + ProductId, FakeStoreProductdto.class);

        return fakeStoreProductdtoResponse.getBody().toProduct();

        /*
        Make  a call to the fakestore api
         */
    }



    @Override
    public List<Product> getAllProducts() {
            List<Product> products = new ArrayList<>();

            //instead of using a List the same can be obtained by using an array for the same purpose
        FakeStoreProductdto[] fakeStoreProductdtos = restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductdto[].class);

                    for(FakeStoreProductdto fakeStoreProductdto : fakeStoreProductdtos){
                            products.add(fakeStoreProductdto.toProduct());
                    }
                    return products;
    }


    @Override
    public Product createProduct(String title,
            String description,
            String category,
            double price,
            String image) {

        FakeStoreProductdto fakeStoreProductdto = new FakeStoreProductdto();

        fakeStoreProductdto.setDescription(description);
        fakeStoreProductdto.setTitle(title);
        fakeStoreProductdto.setCategory(category);
        fakeStoreProductdto.setPrice(price);
        fakeStoreProductdto.setImage(image);
        



        FakeStoreProductdto fakeStoreProductdto1 = restTemplate.postForObject("https://fakestoreapi.com/products",
                fakeStoreProductdto,
                FakeStoreProductdto.class);
        return fakeStoreProductdto1.toProduct();

    }
}

/*
Rest template -- It is used to help make http request to external api's and get responses

Inversion of control -->
 */

// this is used to call the attribute for the methods
