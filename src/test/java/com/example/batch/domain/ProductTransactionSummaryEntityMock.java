package com.example.batch.domain;

import com.example.batch.persistence.entity.ProductTransactionSummaryEntity;

import java.math.BigDecimal;

public class ProductTransactionSummaryEntityMock implements ProductTransactionSummaryEntity {
    private String clientInformation;
    private String productInformation;
    private BigDecimal totalTransactionAmount;

    public ProductTransactionSummaryEntityMock(String clientInformation,
                                        String productInformation,
                                        BigDecimal totalTransactionAmount) {
        this.clientInformation = clientInformation;
        this.productInformation = productInformation;
        this.totalTransactionAmount = totalTransactionAmount;
    }

    @Override
    public String getClientInformation() {
        return clientInformation;
    }

    @Override
    public String getProductInformation() {
        return productInformation;
    }

    @Override
    public BigDecimal getTotalTransactionAmount() {
        return totalTransactionAmount;
    }
}
