package com.example.batch.job;

import com.example.batch.domain.ProductTransaction;
import com.example.batch.persistence.entity.ProductTransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.trim;

@Component
@Slf4j
public class Processor implements ItemProcessor<ProductTransaction,ProductTransactionEntity> {

    @Override
    public ProductTransactionEntity process(ProductTransaction productTransaction)  {
        log.debug("Processor called for product {}, client {}",
                productTransaction.getProductGroupCode(), productTransaction.getClientNumber());
        return buildProductTransactionEntity(productTransaction);
    }

    private ProductTransactionEntity buildProductTransactionEntity(ProductTransaction productTransaction) {
        return ProductTransactionEntity.builder()
                .recordCode(trim(productTransaction.getRecordCode()))
                .clientType(trim(productTransaction.getClientType()))
                .clientNumber(trim(productTransaction.getClientNumber()))
                .accountNumber(trim(productTransaction.getAccountNumber()))
                .subAccountNumber(trim(productTransaction.getSubAccountNumber()))
                .oppositePartyCode(trim(productTransaction.getOppositePartyCode()))
                .productGroupCode(trim(productTransaction.getProductGroupCode()))
                .exchangeCode(trim(productTransaction.getExchangeCode()))
                .symbol(trim(productTransaction.getSymbol()))
                .expirationDate(trim(productTransaction.getExpirationDate()))
                .currencyCode(trim(productTransaction.getCurrencyCode()))
                .movementCode(trim(productTransaction.getMovementCode()))
                .buySellCode(trim(productTransaction.getBuySellCode()))
                .quantityLongSign(trim(productTransaction.getQuantityLongSign()))
                .quantityLong(productTransaction.getQuantityLong())
                .quantityShortSign(trim(productTransaction.getQuantityShortSign()))
                .quantityShort(productTransaction.getQuantityShort())
                .exchangeBrokerFeeDec(productTransaction.getExchangeBrokerFeeDec().movePointLeft(2))
                .exchangeBrokerFeeDc(trim(productTransaction.getExchangeBrokerFeeDc()))
                .exchangeBrokerFeeCurrencyCode(trim(productTransaction.getExchangeBrokerFeeCurrencyCode()))
                .clearingFeeDec(productTransaction.getClearingFeeDec().movePointLeft(2))
                .clearingFeeDc(trim(productTransaction.getClearingFeeDc()))
                .clearingFeeCurrencyCode(trim(productTransaction.getClearingFeeCurrencyCode()))
                .commission(productTransaction.getCommission().movePointLeft(2))
                .commissionDc(trim(productTransaction.getCommissionDc()))
                .commissionCurrencyCode(trim(productTransaction.getCommissionCurrencyCode()))
                .transactionDate(trim(productTransaction.getTransactionDate()))
                .futureReference(trim(productTransaction.getFutureReference()))
                .ticketNumber(trim(productTransaction.getTicketNumber()))
                .externalNumber(trim(productTransaction.getExternalNumber()))
                .transactionPriceDec(productTransaction.getTransactionPriceDec().movePointLeft(7))
                .traderInitials(trim(productTransaction.getTraderInitials()))
                .oppositeTraderId(trim(productTransaction.getOppositeTraderId()))
                .openCloseCode(trim(productTransaction.getOpenCloseCode()))
                .filler(trim(productTransaction.getFiller()))
                .build();
    }
}
