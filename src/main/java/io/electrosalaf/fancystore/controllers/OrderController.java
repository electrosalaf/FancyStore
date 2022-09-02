package io.electrosalaf.fancystore.controllers;

import io.electrosalaf.fancystore.common.ApiResponse;
import io.electrosalaf.fancystore.exceptions.AuthenticationFailException;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.service.AuthenticationService;
import io.electrosalaf.fancystore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // place order after checkout
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {

        authenticationService.authenticate(token);    // validate token
        User user = authenticationService.getUser(token); // retrieve user

        orderService.placeOrder(user, sessionId);
        return new ResponseEntity<>(new ApiResponse(true, "order placed successfully"), HttpStatus.CREATED);
    }
}
