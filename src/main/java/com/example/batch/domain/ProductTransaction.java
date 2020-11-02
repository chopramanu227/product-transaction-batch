package com.example.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTransaction {
    private String recordCode;
    private String clientType;
    private String clientNumber;
    private String accountNumber;
    private String subAccountNumber;
    private String oppositePartyCode;
    private String productGroupCode;
    private String exchangeCode;
    private String symbol;
    private String expirationDate;
    private String currencyCode;
    private String movementCode;
    private String buySellCode;
    private String quantityLongSign;
    private BigInteger quantityLong;
    private String quantityShortSign;
    private BigInteger quantityShort;
    private BigDecimal exchangeBrokerFeeDec;
    private String exchangeBrokerFeeDc;
    private String exchangeBrokerFeeCurrencyCode;
    private BigDecimal clearingFeeDec;
    private String clearingFeeDc;
    private String clearingFeeCurrencyCode;
    private BigDecimal commission;
    private String commissionDc;
    private String commissionCurrencyCode;
    private String transactionDate;
    private String futureReference;
    private String ticketNumber;
    private String externalNumber;
    private BigDecimal transactionPriceDec;
    private String traderInitials;
    private String oppositeTraderId;
    private String openCloseCode;
    private String filler;
}
