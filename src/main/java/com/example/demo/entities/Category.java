package com.example.demo.entities;

// import java.util.ArrayList;
// import java.util.List;

// import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import jakarta.persistence.OneToMany;
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
@Table(name = "categories")
public class Category {

	@Id
	@Column
	private String categoryId;
	
	@Column(name= "Category_Title", length = 60, nullable = false)
	private String categoryTitle;
	
	@Column(name = "Category_Desc",length = 50)
	private String description;
	
	@Column(name = "cover image")
	private String coverImage;
	
	
	// @OneToMany(mappedBy = "category",cascade= CascadeType.ALL, fetch = FetchType.LAZY)
	
	// private final List<Product> products = new ArrayList<>();
}