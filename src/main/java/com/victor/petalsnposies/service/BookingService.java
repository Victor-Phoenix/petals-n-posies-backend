package com.victor.petalsnposies.service;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final InventoryRepository inventoryRepository;
    private final String frontendUrl;
    public BookingService(BookingRepository bookingRepository, InventoryRepository inventoryRepository, @Value("${frontend.url}") String frontendUrl) {
        this.bookingRepository = bookingRepository;
        this.inventoryRepository = inventoryRepository;
        this.frontendUrl = frontendUrl;
    }
    @Transactional(noRollbackFor = BookingExpiredException.class)
    public String initiateBookingPayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found With Id: " + bookingId));
        User user = getCurrentUser(); // Assume this retrieves the authenticated user
        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking Does Not Belong To This User With Id: " + user.getId());
        }
        if (hasBookingExpired(booking.getCreatedAt())) {
            inventoryRepository.expireBooking(booking.getRoom().getId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getNumberOfRooms());
            booking.setBookingStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new BookingExpiredException("Booking Has Been Already Expired");
        }
        String sessionUrl = getCheckoutSession(booking, frontendUrl + "/payment/success", frontendUrl + "/payment/failure");
        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);
        return sessionUrl;
    }
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("Creating session for booking with Id: {}", booking.getId());
        User user = getCurrentUser();
        try {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            Customer customer = Customer.create(customerParams);
            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(Long.valueOf(booking.getNumberOfRooms()))
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName() + " : " + booking.getRoom().getType())
                                                                    .setDescription("Booking ID: " + booking.getId())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            Session session = Session.create(sessionParams);
            booking.setPaymentSessionId(session.getId());
            bookingRepository.save(booking);
            log.info("Session created successfully for booking with Id: {}", booking.getId());
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe session", e);
        }
    }
}