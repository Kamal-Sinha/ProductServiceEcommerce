package com.example.productservice10april.dtos;



import lombok.Getter;


import lombok.Setter;

@Getter
@Setter

public class RequestBodyProductdto {

    private String title;
    private double price;
    private String image;
    private String description;

    private String category;


}


/*
DTO from each product so that in future if the request
needs additional params, u can easily do it without mufffch impact
 */