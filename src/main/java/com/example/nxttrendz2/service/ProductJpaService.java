/*
 *
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */
package com.example.nxttrendz2.service;

import com.example.nxttrendz2.model.Category;
import com.example.nxttrendz2.model.Product;
import com.example.nxttrendz2.repository.ProductJpaRepository;
import com.example.nxttrendz2.repository.ProductRepository;
import com.example.nxttrendz2.service.CategoryJpaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class ProductJpaService implements ProductRepository {

	@Autowired
	private ProductJpaRepository productJpaRepository;

	@Autowired
	private CategoryJpaService categoryJpaService;

	@Override
	public ArrayList<Product> getProducts() {
		return (ArrayList<Product>) productJpaRepository.findAll();
	}

	@Override
	public Product getProductById(int productId) {
		try {
			return productJpaRepository.findById(productId).get();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Product addProduct(Product product) {
		int categoryId = product.getCategory().getId();
		try {
			Category newcategory = categoryJpaService.getCategoryById(categoryId);
			product.setCategory(newcategory);
			productJpaRepository.save(product);
			return product;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Product updateProduct(int productId, Product product) {
		try {
			Product newproduct = productJpaRepository.findById(productId).get();
			if (product.getName() != null) {
				newproduct.setName(product.getName());
			}
			if (product.getDescription() != null) {
				newproduct.setDescription(product.getDescription());
			}
			if (product.getPrice() != 0) {
				newproduct.setPrice(product.getPrice());
			}
			if (product.getCategory() != null) {
				int categoryId = product.getCategory().getId();
				Category category = categoryJpaService.getCategoryById(categoryId);
				newproduct.setCategory(category);
			}
			productJpaRepository.save(newproduct);
			return newproduct;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteProduct(int productId) {
		try {
			productJpaRepository.deleteById(productId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT);
	}

	@Override
	public Category getProductCategory(int productId) {
		try {
			Product product = productJpaRepository.findById(productId).get();
			return product.getCategory();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}