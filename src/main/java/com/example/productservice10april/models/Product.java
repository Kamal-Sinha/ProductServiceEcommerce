package com.example.productservice10april.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends BaseModel{



    private String description;

    private String title;

    private String imageURL;

    private double price;

    @ManyToOne(cascade = {CascadeType.PERSIST})
     //m:1 relationship between product and category
    private Category category;



}
