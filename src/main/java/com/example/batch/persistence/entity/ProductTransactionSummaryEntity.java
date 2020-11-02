package com.example.batch.persistence.entity;

import java.math.BigDecimal;

public interface ProductTransactionSummaryEntity {
    String getClientInformation();
    String getProductInformation();
    BigDecimal getTotalTransactionAmount();
}
