package com.dbs.loyalty.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.dbs.loyalty.batch.CustomerItem;
import com.dbs.loyalty.batch.CustomerItemProcessor;
import com.dbs.loyalty.batch.JdbcBatchItemWriterBuilder;
import com.dbs.loyalty.batch.JobCompletionNotificationListener;
import com.dbs.loyalty.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;
  
    @Bean
    public FlatFileItemReader<CustomerItem> reader() {
        return new FlatFileItemReaderBuilder<CustomerItem>()
            .name("customerItemReader")
            .resource(new ClassPathResource("customers.csv"))
            .delimited()
            .names(new String[]{"email","name","phone","customer_type","dob","password_hash"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<CustomerItem>() {{
                setTargetType(CustomerItem.class);
            }})
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<CustomerItem> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerItem>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("insert into c_customer (id, email, name, phone, customer_type, dob, password_hash, activated, locked, created_by, created_date) "
            		+ "VALUES (:id, :email, :name, :phone, :customerType, :dob, :passwordHash, :activated, :locked, :createdBy, :createdDate)")
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public Job importCustomerJob(Step stepCustomer) {
        return jobBuilderFactory.get("importCustomerJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener())
            .flow(stepCustomer)
            .end()
            .build();
    }

    @Bean
    public Step stepCustomer(JdbcBatchItemWriter<CustomerItem> writer, CustomerRepository customerRepository) {
        return stepBuilderFactory.get("stepCustomer")
            .<CustomerItem, CustomerItem> chunk(10)
            .reader(reader())
            .processor(customerItemProcessor(customerRepository))
            .writer(writer)
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
