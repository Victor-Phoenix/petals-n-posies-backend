package com.victor.petalsnposies.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<BookingPaymentInitResponseDto> initiateBookingPayment(@PathVariable Long bookingId) {
        BookingPaymentInitResponseDto response = new BookingPaymentInitResponseDto(bookingService.initiateBookingPayment(bookingId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}