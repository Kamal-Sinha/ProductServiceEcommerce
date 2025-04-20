package com.example.productservice10april.repositories;

import com.example.productservice10april.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    Category findByTitle(String title);

    Category save(Category category);

    Category findById(long id);
}
