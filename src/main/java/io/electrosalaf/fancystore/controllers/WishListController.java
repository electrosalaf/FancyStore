package io.electrosalaf.fancystore.controllers;

import io.electrosalaf.fancystore.common.ApiResponse;
import io.electrosalaf.fancystore.dto.product.ProductDto;
import io.electrosalaf.fancystore.exceptions.AuthenticationFailException;
import io.electrosalaf.fancystore.model.Product;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.model.WishList;
import io.electrosalaf.fancystore.repository.ProductRepository;
import io.electrosalaf.fancystore.service.AuthenticationService;
import io.electrosalaf.fancystore.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final AuthenticationService authenticationService;
    private final ProductRepository productRepository;

    @Autowired
    public WishListController(
            WishListService wishListService,
            AuthenticationService authenticationService,
            ProductRepository productRepository
    ) {
        this.wishListService = wishListService;
        this.authenticationService = authenticationService;
        this.productRepository = productRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(
            @RequestBody ProductDto productDto,
            @RequestParam("tokens") String token ) throws AuthenticationFailException {
        authenticationService.authenticate(token);  // authenticate if token is valid
        User user = authenticationService.getUser(token);   // fetch user linked to the token

        Product product = productRepository.findById(productDto.getId()).get(); // get product from the repo

        WishList wishList = new WishList(user, product); // save wish list
        wishListService.createWishList(wishList);

        return new ResponseEntity<>(new ApiResponse(true, "Successfully added to wishlist"), HttpStatus.CREATED);
    }

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) throws AuthenticationFailException {

        authenticationService.authenticate(token); // authenticate token if valid
        User user = authenticationService.getUser(token); // fetch user linked to the token

        List<WishList> wishLists = wishListService.readWishList(user); // retrieve wishlist items

        List<ProductDto> products = new ArrayList<>();

        for (WishList wishList : wishLists) {
            products.add(new ProductDto(wishList.getProduct())); // change each product to product DTO
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
