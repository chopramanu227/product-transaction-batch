package com.example.batch.persistence.repository;

import com.example.batch.persistence.entity.ProductTransactionEntity;
import com.example.batch.persistence.entity.ProductTransactionSummaryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ProductTransactionRepository extends CrudRepository<ProductTransactionEntity,Long> {

    @Query("SELECT DISTINCT concat(clientType,clientNumber,accountNumber,subAccountNumber) AS clientInformation, " +
            "concat(exchangeCode,productGroupCode,symbol,expirationDate) as productInformation, " +
            "SUM (quantityLong-quantityShort) as totalTransactionAmount " +
            "from ProductTransactionEntity group by clientInformation,productInformation")
    Collection<ProductTransactionSummaryEntity> getProductTransactionSummary();
}
