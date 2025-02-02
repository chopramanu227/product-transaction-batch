DROP TABLE IF EXISTS ProductTransaction;

CREATE TABLE ProductTransaction (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    recordCode VARCHAR(3),
    clientType VARCHAR(4),
    clientNumber VARCHAR(4),
    accountNumber VARCHAR(4),
    subAccountNumber VARCHAR(4),
    oppositePartyCode VARCHAR(6),
    productGroupCode VARCHAR(2),
    exchangeCode VARCHAR(4),
    symbol VARCHAR(6),
    expirationDate VARCHAR(8),
    currencyCode VARCHAR(3),
    movementCode VARCHAR(2),
    buySellCode VARCHAR(1),
    quantityLongSign VARCHAR(1),
    quantityLong INT(10),
    quantityShortSign VARCHAR(1),
    quantityShort INT(10),
    exchangeBrokerFeeDec DECIMAL (10,2),
    exchangeBrokerFeeDc VARCHAR(1),
    exchangeBrokerFeeCurrencyCode VARCHAR(3),
    clearingFeeDec DECIMAL(10,2),
    clearingFeeDc VARCHAR(1),
    clearingFeeCurrencyCode VARCHAR(3),
    commission DECIMAL(10,2),
    commissionDc VARCHAR(1),
    commissionCurrencyCode VARCHAR(3),
    transactionDate VARCHAR(8),
    futureReference VARCHAR(6),
    ticketNumber VARCHAR(6),
    externalNumber VARCHAR(6),
    transactionPriceDec DECIMAL(11,7),
    traderInitials VARCHAR(6),
    oppositeTraderId VARCHAR(7),
    openCloseCode VARCHAR(1),
    filler VARCHAR(127)
);