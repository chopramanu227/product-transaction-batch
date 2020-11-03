package com.example.batch;

import com.example.batch.config.BatchConfiguration;
import com.example.batch.domain.ProductTransaction;
import com.example.batch.job.Listener;
import com.example.batch.job.Processor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.StepScopeTestUtils;
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
import static org.junit.Assert.assertEquals;
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
public class ReaderTest {

    @Autowired
    private FlatFileItemReader<ProductTransaction> itemReader;

    @MockBean
    private Listener listener;

    @MockBean
    private Processor processor;

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
}
