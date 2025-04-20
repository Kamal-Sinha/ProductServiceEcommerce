package com.example.productservice10april.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends BaseModel {

    private String title;

    private String description;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
//1:m relationship between category and product
    @JsonIgnore
    private List<Product> products;
}

