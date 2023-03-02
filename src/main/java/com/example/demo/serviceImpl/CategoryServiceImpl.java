package com.example.demo.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
//import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.CategoryDto;
import com.example.demo.dtos.PageableResponse;
import com.example.demo.entities.Category;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.helper.Helper;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.service.CategoryService;



@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper mapper;

	 @Value("${categories.image.path}")
	private String imagePath ;
	  //= ("images/categories");

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Override
	public CategoryDto create(CategoryDto categoryDto) {

		Category category = mapper.map(categoryDto, Category.class);

		String categoryId = UUID.randomUUID().toString();
//        String categoryId = UUID.randomUUID().toString();
		category.setCategoryId(categoryId);
		Category savedCategory = categoryRepository.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto update(String categoryId, CategoryDto categoryDto) {

		// get category of given Id
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found by this Id"));

		// update category details
		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());

		// save Data
		Category updatedCategory = categoryRepository.save(category);

		return mapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) {
		// get category of given Id
		Category category = categoryRepository.findById(categoryId)
				                   .orElseThrow(() -> new ResourceNotFoundException(" Category not found with given Id"));
		// delete id
		categoryRepository.delete(category);
		// delete user Image
		// fullPath is images/users/abc.png
		
        String fullPath = imagePath + category.getCoverImage();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			logger.info("user image not found in folder");
			e.printStackTrace();
		}

	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> page = categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
		return response;
	}

	@Override
	public CategoryDto get(String categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found through this Id"));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public PageableResponse<CategoryDto> getSearchCategory(String key, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> page = categoryRepository.findByCategoryTitleContaining(key, pageable);
		PageableResponse<CategoryDto> response = Helper.getPageableResponse(page, CategoryDto.class);
		return response;
	}


}
