package com.example.batch.persistence.entity;

import java.math.BigDecimal;

/**
 * Mapper interface to hold the result of product summary information stored in database.
 */
public interface ProductTransactionSummaryEntity {
    String getClientInformation();
    String getProductInformation();
    BigDecimal getTotalTransactionAmount();
}
