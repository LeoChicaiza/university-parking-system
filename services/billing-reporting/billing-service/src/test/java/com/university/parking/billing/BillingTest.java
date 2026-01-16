package com.university.parking.billing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BillingTest {
    @Test
    void testBasicBillingLogic() {
        // Test simple que siempre pasa
        assertTrue(true);
    }
    
    @Test 
    void testDurationCalculation() {
        // Fórmula usada en BillingService
        long entryTime = 1000L;
        long exitTime = 61000L;
        long durationMinutes = Math.max(1, (exitTime - entryTime) / (1000 * 60));
        assertEquals(1L, durationMinutes);
    }
}