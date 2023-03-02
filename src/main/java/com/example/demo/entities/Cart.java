package com.example.demo.entities;

import java.util.Date;

import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class Cart {

    @Id
    private int cartId;

    private Date createdAt;

    @OneToOne
    private User user;


// mapping cart_items

}
