package com.example.productservice10april.services;

import com.example.productservice10april.models.Category;
import com.example.productservice10april.models.Product;
import com.example.productservice10april.repositories.CategoryRepository;
import com.example.productservice10april.repositories.ProductRepository;
import com.example.productservice10april.repositories.Projections.ProductProjection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("selfproductservice")
public  class OwnProductService implements ProductService{

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;


    //creating constructor for injecting the reposiotries

    public OwnProductService(CategoryRepository categoryRepository,
                             ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;

    }



    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();

    }


    public List<ProductProjection>getProductByCategory(String category_name){
        List<ProductProjection> prd = productRepository.findAllProductsByCategoryNameAndProductPrice("abc", 90);
        return productRepository.findAllByCategory_Title(category_name);
    }


    @Override
    public Product getSingleProduct(Long ProductId) {
        return productRepository.findByIdIs(ProductId);
    }

    @Override
    public Product createProduct(String image,
                                 String description,
                                 String title,
                                 double price,
                                 String category) {

        Product p = new Product();
        p.setTitle(title);
        p.setDescription(description);
        p.setPrice(price);
        p.setImageURL(image);

        //Assuming category is a simple string
        //we might need to create a category object

        Category categoryFromDatabase = categoryRepository.findByTitle(category);

        if (categoryFromDatabase == null){
            Category category2 = new Category();
            category2.setTitle(category);
            //categoryRepository.save(category2); //Persist as cascade type so not needed
            categoryFromDatabase = category2;
        }

        p.setCategory(categoryFromDatabase); // persist as cascade type so not needed

      Product savedProduct = productRepository.save(p);
        return savedProduct;


        
    }
}
