package com.example.productservice10april.repositories;

import com.example.productservice10april.models.Category;
import com.example.productservice10april.models.Product;
import com.example.productservice10april.repositories.Projections.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {




    @Query("Select p from Product p where p.title = :title")
    List<Product> findAllProductsWithAParticularName(@Param("title") String title);


    @Query("Select p from Product p where p.category.title = :category_name and p.price = :price")
    List<ProductProjection> findAllProductsByCategoryNameAndProductPrice(@Param("category") String category,
                                                                         @Param("price") long price);

    Product findByTitle(String title);

    Product save(Product product);

    Product findByIdIs(Long id);


    List<Product> findAllByCategoryAndId (Category category, Long Id);



    /*Spring will automatically run a query on the db for the corresponding method name*/

    @Override
    List<Product> findAll();



    List<Product>findAllByTitle(String title);


    List<Product> findAllByCategory(Category category);

    List<Product> category(Category category);

    List<ProductProjection> findAllByCategory_Title(String categoryTitle);
}
