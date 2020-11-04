package com.example.batch.persistence.repository;

import com.example.batch.persistence.entity.ProductTransactionEntity;
import com.example.batch.persistence.entity.ProductTransactionSummaryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository class for doing various DB operation against <code>ProductTransactionEntity</code>.
 */
@Repository
public interface ProductTransactionRepository extends CrudRepository<ProductTransactionEntity,Long> {
    /**
     * Custom query to retrieve records for Product summary report.
     * <br/>
     * Query groups record by product and client information.
     * @return
     */
    @Query("SELECT DISTINCT concat(clientType,clientNumber,accountNumber,subAccountNumber) AS clientInformation, " +
            "concat(exchangeCode,productGroupCode,symbol,expirationDate) as productInformation, " +
            "SUM (quantityLong-quantityShort) as totalTransactionAmount " +
            "from ProductTransactionEntity group by clientInformation,productInformation")
    Collection<ProductTransactionSummaryEntity> getProductTransactionSummary();
}
