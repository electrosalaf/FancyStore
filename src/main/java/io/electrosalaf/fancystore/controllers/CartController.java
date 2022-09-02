package io.electrosalaf.fancystore.controllers;

import io.electrosalaf.fancystore.common.ApiResponse;
import io.electrosalaf.fancystore.dto.cart.AddToCartDto;
import io.electrosalaf.fancystore.dto.cart.CartDto;
import io.electrosalaf.fancystore.exceptions.AuthenticationFailException;
import io.electrosalaf.fancystore.exceptions.CartItemNotExistException;
import io.electrosalaf.fancystore.exceptions.ProductNotExistException;
import io.electrosalaf.fancystore.model.Product;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.service.AuthenticationService;
import io.electrosalaf.fancystore.service.CartService;
import io.electrosalaf.fancystore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    @Autowired
    public CartController(
            CartService cartService,
            ProductService productService, AuthenticationService authenticationService) {
        this.cartService = cartService;
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(
            @RequestBody AddToCartDto addToCartDto,
            @RequestParam("token") String token
    ) throws AuthenticationFailException, ProductNotExistException {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        Product product = productService.getProductById(addToCartDto.getProductId());

        cartService.addToCart(addToCartDto, product, user);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token)
            throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(
            @PathVariable("cartItemId") int cartItemId,
            @RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        cartService.deleteCartItem(cartItemId, user);

        return new ResponseEntity<>(new ApiResponse(true, "Item successfully deleted"), HttpStatus.OK);
    }
}
