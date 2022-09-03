package io.electrosalaf.fancystore.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import io.electrosalaf.fancystore.common.ApiResponse;
import io.electrosalaf.fancystore.dto.checkout.CheckoutItemDto;
import io.electrosalaf.fancystore.dto.checkout.StripeResponse;
import io.electrosalaf.fancystore.exceptions.AuthenticationFailException;
import io.electrosalaf.fancystore.model.Order;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.service.AuthenticationService;
import io.electrosalaf.fancystore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticationService authenticationService;

    @Autowired
    public OrderController(OrderService orderService, AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    // place order after checkout
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {

        authenticationService.authenticate(token);    // validate token
        User user = authenticationService.getUser(token); // retrieve user

        orderService.placeOrder(user, sessionId);
        return new ResponseEntity<>(new ApiResponse(true, "order placed successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        List<Order> orderDtoList = orderService.listOrders(user);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }
}
