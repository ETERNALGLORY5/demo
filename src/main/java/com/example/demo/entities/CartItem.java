package com.example.demo.entities;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name="cart_item")
@Entity
public class CartItem{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int cartItemId;

    @OneToOne
    private Product product;
 
    private int quantity;

    private int totalPrice;


// mapping cart

}   
