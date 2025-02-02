package com.example.batch.job;

import com.example.batch.persistence.entity.ProductTransactionSummaryEntity;
import com.example.batch.persistence.repository.ProductTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Consumer;

/**
 *  Listener class which gets triggered after the job has been executed.
 */
@Component
@Slf4j
public class Listener extends JobExecutionListenerSupport {

    @Value("${output.file}")
    private String outputFile;

    private static final String[] HEADERS = {"Client_Information","Product_Information","Total_Transaction_Amount"};

    private final ProductTransactionRepository productTransactionRepository;

    public Listener(ProductTransactionRepository productTransactionRepository) {
        this.productTransactionRepository = productTransactionRepository;
    }

    /**
     * Method gets triggered once the job execution is completed.
     * <br/>
     * Method will generate a product summary report in a Output.csv file.
     * <br/>
     * Report is generated based on the data recorded in the database after reading the input file.
     *
     * @param jobExecution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.debug("Job processing completed....");
        log.debug("Generating CSV extract of the product summary at {}.", outputFile);
        Collection<ProductTransactionSummaryEntity> prodTxnSummaryEntities = productTransactionRepository.getProductTransactionSummary();

        try(CSVPrinter csvPrinter= new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(HEADERS))){
            prodTxnSummaryEntities.forEach(printProdTxnSummary(csvPrinter));
        }catch (IOException e){
            log.error("Exception occured while creating CSV file" + e);
            //can throw exception based on business requirements
        }
    }

    private Consumer<? super ProductTransactionSummaryEntity> printProdTxnSummary(CSVPrinter csvPrinter) {
        return prodTxnSummaryEntity -> {
            try {
                String clientInformation = prodTxnSummaryEntity.getClientInformation();
                String productInformation = prodTxnSummaryEntity.getProductInformation();
                BigDecimal totalTransactionAmount = prodTxnSummaryEntity.getTotalTransactionAmount();
                csvPrinter.printRecord(clientInformation,productInformation,totalTransactionAmount);
                log.debug("successfully written record to CSV file with client information : {}, product information : {}, total amount : {} ",
                        clientInformation,productInformation,totalTransactionAmount);
            } catch (Exception e) {
                log.error("Exception occured while writing records to CSV file" + e);
                //can throw exception based on business requirements
            }
        };
    }
}
