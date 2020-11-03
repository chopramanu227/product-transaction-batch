package com.example.batch;

import com.example.batch.config.BatchConfiguration;
import com.example.batch.domain.ProductTransaction;
import com.example.batch.job.Listener;
import com.example.batch.job.Processor;
import com.example.batch.persistence.entity.ProductTransactionEntity;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.*;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.FileSystemResource;
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

    @Autowired
    private FlatFileItemReader<ProductTransaction> itemReader;

    @MockBean
    private Listener listener;

    @MockBean
    private Processor processor;

    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters defaultJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        /*paramsBuilder.addString("file.input", TEST_INPUT);
        paramsBuilder.addString("file.output", TEST_OUTPUT);*/

        return paramsBuilder.toJobParameters();
    }

    @Test
    public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
        // given
        /*FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);*/
        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // then
        assertThat(actualJobInstance.getJobName(), is("transactionProcessingJob"));
        assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
        //AssertFile.assertFileEquals(expectedResult, actualResult);
    }

    @Test
    public void givenMockedStep_whenReaderCalled_thenSuccess() throws Exception {
        // given
        StepExecution stepExecution = MetaDataInstanceFactory
                .createStepExecution(defaultJobParameters());
        // when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            ProductTransaction productTransaction;
            itemReader.open(stepExecution.getExecutionContext());
            while ((productTransaction = itemReader.read()) != null) {
                // then
                assertThat(productTransaction.getRecordCode(), is("315"));
            }
            itemReader.close();
            return null;
        });
    }
}
