package com.dbs.loyalty.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.dbs.loyalty.batch.CustomerItem;
import com.dbs.loyalty.batch.CustomerItemProcessor;
import com.dbs.loyalty.batch.CustomerItemWriter;
import com.dbs.loyalty.batch.JobCompletionNotificationListener;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;
    
    private final ApplicationProperties applicationProperties;
  
    @Bean
    public FlatFileItemReader<CustomerItem> reader() {
        return new FlatFileItemReaderBuilder<CustomerItem>()
            .name("customerItemReader")
            .resource(new FileSystemResource(applicationProperties.getScheduler().getFilePath()))
            .delimited()
            .names(new String[]{"email","name","phone","customer_type","dob","password_hash"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<CustomerItem>() {{
                setTargetType(CustomerItem.class);
            }})
            .build();
    }

    @Bean(name = "customerJob")
    public Job customerJob(Step customerStep) {
        return jobBuilderFactory.get("customerJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener())
            .flow(customerStep)
            .end()
            .build();
    }

    @Bean
    public Step customerStep(CustomerRepository customerRepository) {
        return stepBuilderFactory.get("customerStep")
            .<CustomerItem, Customer> chunk(10)
            .reader(reader())
            .processor(customerItemProcessor(customerRepository))
            .writer(new CustomerItemWriter(customerRepository))
            .build();
    }

    @Bean
    public CustomerItemProcessor customerItemProcessor(CustomerRepository customerRepository) {
    	return new CustomerItemProcessor(customerRepository);
    }
    
    @Bean
    public JobCompletionNotificationListener listener() {
    	return new JobCompletionNotificationListener();
    }
    
}
