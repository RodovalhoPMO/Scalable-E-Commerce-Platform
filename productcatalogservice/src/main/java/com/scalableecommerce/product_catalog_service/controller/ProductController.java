package com.scalableecommerce.product_catalog_service.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scalableecommerce.product_catalog_service.model.Product;
import com.scalableecommerce.product_catalog_service.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }      


    @Operation(summary = "Register a new product")
    @ApiResponse(responseCode = "201", description = "Product created")
    @PostMapping("/register")
    public ResponseEntity<Product> registerProduct(@Valid @RequestBody Product product) {
        Product newProduct = productService.registerProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Product> getProductByTitle(@PathVariable String title) {
    return productService.findByTitle(title)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
 @GetMapping("/id/{id}")
  public ResponseEntity<Product> findProductById(@PathVariable Long id) {
    try {
      Product product = productService.findById(id);
      return ResponseEntity.ok(product);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @ApiResponse(responseCode = "204", description = "Product deleted successfully!")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) { 
    try{
      productService.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (NoSuchElementException e) { 
      return ResponseEntity.notFound().build();
    }
  }
  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProductById(@PathVariable Long id, @Valid @RequestBody Product product) {
    try {
      Product updatedProduct = productService.updateProduct(id, product);
      return ResponseEntity.ok(updatedProduct);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
