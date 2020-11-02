/*
package com.example.batch.job;

import com.example.batch.persistence.entity.ProductTransactionEntity;
import com.example.batch.persistence.repository.ProductTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Writer extends RepositoryItemWriter<ProductTransactionRepository> {

    private final ProductTransactionRepository productTransactionRepository;

    public Writer(ProductTransactionRepository productTransactionRepository) {
        this.productTransactionRepository = productTransactionRepository;
    }

    @Override
    public void write(List<? extends ProductTransactionRepository> items) throws Exception {
        super.write(items);
    }

    */
/*@Override
    public void write(List<? extends ProductTransactionEntity> list){
        log.debug("Adding {} product transactions record to database.", list.size());
        list.stream()
                .forEach(productTransactionEntity -> productTransactionRepository.save(productTransactionEntity));

    }*//*

}
*/
