package com.example.batch;

import com.example.batch.config.BatchConfiguration;
import com.example.batch.job.Listener;
import com.example.batch.job.Processor;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { BatchConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@TestPropertySource(properties = {
        "input.file=src/test/resources/Input.txt",
        "output.file=src/test/generated/Output.csv"
})
public class BatchIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    /*@Autowired
    private FlatFileItemReader<ProductTransaction> itemReader;*/

    @MockBean
    private Listener listener;

    @MockBean
    private Processor processor;

    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void givenBatchConfig_whenJobExecuted_thenSuccess() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
        assertThat(actualJobInstance.getJobName(), is("transactionProcessingJob"));
        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
    }

    /*@Test
    public void givenMockedStep_whenReaderCalled_thenSuccess() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            ProductTransaction productTransaction;
            int count=0;
            itemReader.open(stepExecution.getExecutionContext());
            while ((productTransaction = itemReader.read()) != null) {
                count++;
                assertThat(productTransaction.getRecordCode(), is("315"));
            }
            assertEquals(count,717);
            itemReader.close();
            return null;
        });
    }

    @Test(expected = ItemStreamException.class)
    public void givenMockedStep_whenReaderCalledWithoutFileResource_thenFail() throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        itemReader.setResource(new FileSystemResource("inputFile")); // file is not present
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            ProductTransaction productTransaction;
            int count=0;
            itemReader.open(stepExecution.getExecutionContext());
            while ((productTransaction = itemReader.read()) != null) {
                count++;
                assertThat(productTransaction.getRecordCode(), is("315"));
            }
            assertEquals(count,717);
            itemReader.close();
            return null;
        });
    }

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
    }*/
}
