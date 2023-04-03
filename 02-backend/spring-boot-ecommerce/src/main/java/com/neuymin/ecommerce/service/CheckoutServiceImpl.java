package com.neuymin.ecommerce.service;

import com.neuymin.ecommerce.dao.CustomerRepository;
import com.neuymin.ecommerce.dto.PaymentInfo;
import com.neuymin.ecommerce.dto.Purchase;
import com.neuymin.ecommerce.dto.PurchaseResponse;
import com.neuymin.ecommerce.entity.Customer;
import com.neuymin.ecommerce.entity.Order;
import com.neuymin.ecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository, @Value("${stripe.key.secret}") String secretKey) {
        this.customerRepository  = customerRepository;

        //initialize Stripe API with the secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dtp
        Order order = purchase.getOrder();

        //generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        //populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        //populate order whith shipping and billing address
        order.setShippingAddress(purchase.getShippingAddress());
        order.setBillingAddress(purchase.getBillingAddress());

        //populate customer with order
        Customer customer = purchase.getCustomer();

        // check if this is an existing customer
        String theEmail = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if(customerFromDB != null) {
            customer = customerFromDB;
        }

        customer.add(order);

        //save to the db
        customerRepository.save(customer);

        //return a response
        return new PurchaseResponse(orderTrackingNumber);

    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "test description");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID (version 4)
        return UUID.randomUUID().toString();
    }
}
