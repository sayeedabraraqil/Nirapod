package com.example

import com.example.data.model.CourierProvider
import com.example.data.model.PaymentProvider
import com.example.data.model.ProductType
import com.example.data.model.TransactionState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EscrowProtocolTest {

    @Test
    fun `test escrow protection fee calculation`() {
        val productPrice = 1000.0
        val deliveryFee = CourierProvider.STEADFAST.defaultCharge // 70.0
        val protectionFee = productPrice * 0.015 // 15.0
        val total = productPrice + deliveryFee + protectionFee

        assertEquals(70.0, deliveryFee, 0.01)
        assertEquals(15.0, protectionFee, 0.01)
        assertEquals(1085.0, total, 0.01)
    }

    @Test
    fun `test digital delivery charge is zero`() {
        val digitalCourier = CourierProvider.DIGITAL_INSTANT
        assertEquals(0.0, digitalCourier.defaultCharge, 0.001)
    }

    @Test
    fun `test transaction states progression`() {
        val stateInitial = TransactionState.FUNDS_HELD
        val stateShipped = TransactionState.SHIPPED
        val stateCompleted = TransactionState.COMPLETED

        assertTrue(stateInitial.displayNameBn.contains("এসক্রো"))
        assertTrue(stateShipped.displayNameBn.contains("কুরিয়ার"))
        assertTrue(stateCompleted.displayNameBn.contains("ফান্ড রিলিজ"))
    }
}
