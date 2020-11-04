package com.example.batch.config;

import com.example.batch.domain.ProductTransaction;
import com.example.batch.job.Listener;
import com.example.batch.job.Processor;
import com.example.batch.persistence.entity.ProductTransactionEntity;
import com.example.batch.persistence.repository.ProductTransactionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Configuration file to define Job executions details.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public static final String TRANSACTION_PROCESSING_JOB = "transactionProcessingJob";

    @Value("${input.file}")
    private String inputFile;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Listener listener;

    private final Processor processor;

    private final ProductTransactionRepository productTransactionRepository;

    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory,
                              Listener listener,Processor processor,ProductTransactionRepository productTransactionRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory=stepBuilderFactory;
        this.listener=listener;
        this.processor=processor;
        this.productTransactionRepository=productTransactionRepository;
    }

    /**
     * Method defines Job parameters including steps to execute, and post processing listeners.
     *
     * @param step
     * @return
     */
    @Bean
    public Job transactionProcessingJob(Step step){
        return jobBuilderFactory.get(TRANSACTION_PROCESSING_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    /**
     * Method defines Bean for steps of the batch job. It registers Reader, Processor and Writer required for data processing.
     *
     * @param fieldSetMapper
     * @return
     */
    @Bean
    public Step step(FieldSetMapper<ProductTransaction> fieldSetMapper){
        RepositoryItemWriter writer = new RepositoryItemWriter();
        writer.setRepository(productTransactionRepository);
        writer.setMethodName("save");

        return stepBuilderFactory.get("step")
                .<ProductTransaction, ProductTransactionEntity>chunk(100)
                .reader(reader(fieldSetMapper))
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skipLimit(10)
                .build();
    }

    /**
     * Method defines Bean of reader. It uses flat file based item reader for reading
     * input records from a flat file.
     *
     * @param fieldSetMapper - maps input file fields to domain objects.
     * @return
     */
    @Bean
    public FlatFileItemReader<ProductTransaction> reader(FieldSetMapper<ProductTransaction> fieldSetMapper) {
        DefaultLineMapper<ProductTransaction> lineMapper= new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer());
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.afterPropertiesSet();

        FlatFileItemReader<ProductTransaction> reader= new FlatFileItemReader<>();
        //Resource resource = new ClassPathResource("Input.txt");
        Resource resource = new FileSystemResource(inputFile);
        reader.setResource(resource);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    /**
     * Method defines mapper configuration for mapping input records to domain object.
     *
     * @return
     */
    @Bean
    public FieldSetMapper<ProductTransaction> fieldSetMapper(){
        BeanWrapperFieldSetMapper<ProductTransaction> mapper= new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(ProductTransaction.class);
        return mapper;
    }

    /**
     * Method defines tokenization approach to be used for reading flat files and mapping
     * to individual fields of the mapper class.
     *
     * @return
     */
    private FixedLengthTokenizer tokenizer() {
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("recordCode",
                "clientType",
                "clientNumber",
                "accountNumber",
                "subAccountNumber",
                "oppositePartyCode",
                "productGroupCode",
                "exchangeCode",
                "symbol",
                "expirationDate",
                "currencyCode",
                "movementCode",
                "buySellCode",
                "quantityLongSign",
                "quantityLong",
                "quantityShortSign",
                "quantityShort",
                "exchangeBrokerFeeDec",
                "exchangeBrokerFeeDc",
                "exchangeBrokerFeeCurrencyCode",
                "clearingFeeDec",
                "clearingFeeDc",
                "clearingFeeCurrencyCode",
                "commission",
                "commissionDc",
                "commissionCurrencyCode",
                "transactionDate",
                "futureReference",
                "ticketNumber",
                "externalNumber",
                "transactionPriceDec",
                "traderInitials",
                "oppositeTraderId",
                "openCloseCode",
                "filler");
        tokenizer.setColumns(new Range(1, 3),
                new Range(4, 7),
                new Range(8, 11),
                new Range(12, 15),
                new Range(16, 19),
                new Range(20, 25),
                new Range(26, 27),
                new Range(28, 31),
                new Range(32, 37),
                new Range(38, 45),
                new Range(46, 48),
                new Range(49, 50),
                new Range(51, 51),
                new Range(52, 52),
                new Range(53, 62),
                new Range(63, 63),
                new Range(64, 73),
                new Range(74, 85),
                new Range(86, 86),
                new Range(87, 89),
                new Range(90, 101),
                new Range(102, 102),
                new Range(103, 105),
                new Range(106, 117),
                new Range(118, 118),
                new Range(119, 121),
                new Range(122, 129),
                new Range(130, 135),
                new Range(136, 141),
                new Range(142, 147),
                new Range(148, 162),
                new Range(163, 168),
                new Range(169, 175),
                new Range(176, 176),
                new Range(177, 303));
        tokenizer.setStrict(false);
        return tokenizer;
    }


}
