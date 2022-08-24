package io.electrosalaf.fancystore.controllers;

import io.electrosalaf.fancystore.config.ApiResponse;
import io.electrosalaf.fancystore.dto.product.ProductDto;
import io.electrosalaf.fancystore.model.Category;
import io.electrosalaf.fancystore.service.CategoryService;
import io.electrosalaf.fancystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> productDtos = productService.listProducts();
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto) {
        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }

        Category category = optionalCategory.get();
        productService.addProduct(productDto, category);

        return new ResponseEntity<>(new ApiResponse(true, "Product added successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/update/{productID}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") Integer productID, @RequestBody @Valid ProductDto productDto) {
        Optional<Category> optionalCategory =
                categoryService.readCategory(productDto.getCategoryId());

        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }
        Category category = optionalCategory.get();
        productService.updateProduct(productID, productDto, category);

        return new ResponseEntity<>(
                new ApiResponse(true, "product updated successfully"), HttpStatus.OK);
    }
}
