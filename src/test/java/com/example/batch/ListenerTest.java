package com.example.batch;

import com.example.batch.config.BatchConfiguration;
import com.example.batch.domain.ProductTransactionSummaryEntityMock;
import com.example.batch.job.Listener;
import com.example.batch.job.Processor;
import com.example.batch.persistence.entity.ProductTransactionSummaryEntity;
import com.example.batch.persistence.repository.ProductTransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { BatchConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@TestPropertySource(properties = {
        "input.file=src/test/resources/Input.txt",
        "output.file=src/test/generated/Output.csv"
})
public class ListenerTest {

    @MockBean
    private Listener listener;

    @MockBean
    private Processor processor;

    @Test
    public void givenMockedJob_whenJobExecutionIsComplete_thenWriteCSVFileSuccess() throws Exception {
        ProductTransactionRepository productTransactionRepository = Mockito.mock(ProductTransactionRepository.class);
        Listener listener = new Listener(productTransactionRepository);
        String generatedFile = "src/test/generated/Output.csv";
        String actualFile = "src/test/resources/Output.csv";
        ReflectionTestUtils.setField(listener, "outputFile", generatedFile);

        FileSystemResource expectedResult = new FileSystemResource(actualFile);
        FileSystemResource actualResult = new FileSystemResource(generatedFile);
        JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
        Mockito.when(productTransactionRepository.getProductTransactionSummary()).thenReturn(getProdTxnSummaryEntities());
        listener.afterJob(jobExecution);
        AssertFile.assertLineCount(2,new FileSystemResource("src/test/generated/Output.csv")); // line count including headers
        AssertFile.assertFileEquals(expectedResult, actualResult);
    }

    private Collection<ProductTransactionSummaryEntity> getProdTxnSummaryEntities() {
        Collection<ProductTransactionSummaryEntity> productTransactionSummaryEntities= new ArrayList<>();
        ProductTransactionSummaryEntityMock prodTxnEntityMock = new ProductTransactionSummaryEntityMock(
                "CL123400020001", "SGXFUNK20100910", BigDecimal.ONE);
        productTransactionSummaryEntities.add(prodTxnEntityMock);
        return productTransactionSummaryEntities;
    }
}
