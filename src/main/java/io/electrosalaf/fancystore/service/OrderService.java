package io.electrosalaf.fancystore.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import io.electrosalaf.fancystore.dto.cart.CartDto;
import io.electrosalaf.fancystore.dto.cart.CartItemDto;
import io.electrosalaf.fancystore.dto.checkout.CheckoutItemDto;
import io.electrosalaf.fancystore.model.Order;
import io.electrosalaf.fancystore.model.OrderItem;
import io.electrosalaf.fancystore.model.User;
import io.electrosalaf.fancystore.repository.OrderItemsRepository;
import io.electrosalaf.fancystore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.stripe.param.checkout.SessionCreateParams.*;


@Service
@Transactional
public class OrderService {

    private final CartService cartService;
    private final OrderItemsRepository orderItemsRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(
            CartService cartService,
            OrderItemsRepository orderItemsRepository,
            OrderRepository orderRepository
    ) {
        this.cartService = cartService;
        this.orderItemsRepository = orderItemsRepository;
        this.orderRepository = orderRepository;
    }

    @Value("{BASE_URL}")
    private String baseURL;

    @Value("{STRIPE_SECRET_KEY}")
    private String apiKey;

    // create total price and send productname as input
    PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
        return PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount( ((long) checkoutItemDto.getPrice()) * 100)
                .setProductData(
                        PriceData.ProductData.builder()
                                .setName(checkoutItemDto.getProductName())
                                .build())
                .build();
    }

    // build each product in the stripe checkout page
    LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
        return LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))    // set price for each product
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity()))) // set quantity for each product
                .build();
    }

    // create session from the list of checkout items
    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

        // supply success and failure url for stripe
        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failure";

        // set the private key
        Stripe.apiKey = apiKey;

        List<LineItem> sessionItemList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
            sessionItemList.add(createSessionLineItem(checkoutItemDto));
        }

        // build the session param
        SessionCreateParams params = builder()
                .addPaymentMethodType(PaymentMethodType.CARD)
                .setMode(Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemList)
                .setSuccessUrl(successURL)
                .build();

       return Session.create(params);
    }

    public void placeOrder(User user, String sessionId) {

        CartDto cartDto = cartService.listCartItems(user);

        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        // Create the order and save it
        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        orderRepository.save(newOrder);

        for (CartItemDto cartItemDto : cartItemDtoList) {
            // create orderItem and save each one
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);

            orderItemsRepository.save(orderItem);
        }

    }
}
