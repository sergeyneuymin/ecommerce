package com.neuymin.ecommerce.service;

import com.neuymin.ecommerce.dto.PaymentInfo;
import com.neuymin.ecommerce.dto.Purchase;
import com.neuymin.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;

}
