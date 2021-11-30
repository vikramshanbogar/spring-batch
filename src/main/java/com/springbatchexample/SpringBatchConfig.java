package com.springbatchexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<User> itemReader,
                   ItemProcessor<User, User> itemProcessor,
                   ItemWriter<User> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<User, User>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();


        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

//    @Bean
//    public FlatFileItemReader<Order> itemReader() {
//
//        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
//        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/users.csv"));
//        flatFileItemReader.setName("CSV-Reader");
//        flatFileItemReader.setLinesToSkip(1);
//        flatFileItemReader.setLineMapper(lineMapper());
//        return flatFileItemReader;
//    }
//
    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "dept", "salary");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

//    @Bean
//    ItemReader<User> excelStudentReader() {
//        PoiItemReader<User> reader = new PoiItemReader<>();
//        reader.setLinesToSkip(1);
//        reader.setResource(new FileSystemResource("src/main/resources/users.xlsx"));
//        reader.setRowMapper(excelRowMapper());
//        return reader;
//    }

    @Bean
    ItemStreamReader<User> reader() {
        PoiItemReader reader = new PoiItemReader();
        reader.setResource(new ClassPathResource("users.xlsx"));
        reader.setRowMapper(excelRowMapper());
        reader.setLinesToSkip(1);
        return reader;
    }

//    @Bean
//    @StepScope
//    public PoiItemReader excelStudentReader(@Value("#{jobParameters['filename']}") String filename) throws FileNotFoundException {
//        PoiItemReader reader = new PoiItemReader();
//        reader.setLinesToSkip(1);
//        PushbackInputStream input = new PushbackInputStream(new FileInputStream(new File(filename)));
//        InputStreamResource resource = new InputStreamResource(input);
//        reader.setResource(resource);
//        reader.setRowMapper(excelRowMapper());
//        return reader;
//    }


    private RowMapper<User> excelRowMapper() {
        BeanWrapperRowMapper<User> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setDistanceLimit(0);
        rowMapper.setTargetType(User.class);
        return rowMapper;
    }

//    private RowMapper<User> excelRowMapper() {
//        return lineMapper();
//    }

}
