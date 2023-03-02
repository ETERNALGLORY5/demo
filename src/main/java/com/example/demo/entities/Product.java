package com.example.demo.entities;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
 import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "products")
public class Product {

	@Id
	@Column
	private String productId;
	
	@Column(name = "product_title", length = 1000)
	private String title;
	
	@Column(name ="product_prescription", length =1000)
	private String productPrescription;
	
	@Column(name="price", length=60)
	private int price;
	
	@Column(name="quantity", length=60)
	private int quantity;
	
    @Column(name="mfg_date", length = 30)
	private Date mfgDate;
	
	@Column(name="exp_date", length=30)
	private Date expDate;
	
	@Column(name="")
	private boolean live;
	
	@Column(name="in stock",length = 10)
	private boolean stock;
	
	@Column(name="productImage")
	private String productImage;
	

	@Column(name = "file_name")
	private String fileName;
	
	 @ManyToOne(fetch= FetchType.EAGER)
	 @JoinColumn(name="category_id") // this name should be same as main table name
	private Category category;
	
	
}
