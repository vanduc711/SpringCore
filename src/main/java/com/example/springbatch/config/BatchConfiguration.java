package com.example.springbatch.config;

import com.example.springbatch.model.Infomation;
import com.example.springbatch.processor.InfoItemProcessor;
import com.example.springbatch.repository.InfomationRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfiguration {

    //Injection InfomationRepository để sử dụng để lưu dữ liệu vào CSDL
    @Autowired
    InfomationRepository infomationRepository;

    //Sử dụng phương thức FlatFileItemReader để đọc dữ liệu từ file csv
    @Bean
    public FlatFileItemReader<Infomation> reader() {
        return new FlatFileItemReaderBuilder<Infomation>()
                //chỉ định đường dẫn file
                .resource(new ClassPathResource("infomation-list.csv"))
                .name("InformationReader")
                //gọi phương thức LineMapper để ánh xạ các dòng dữ liệu sang Infomation
                .lineMapper(LineMapper())
                //bỏ qua dòng đầu tiên trong file csv
                .linesToSkip(1)
                .build();
    }

    //Phương thức này dùng để ghi dữ liệu vào csdl
    @Bean
    public RepositoryItemWriter<Infomation> writer() {
        RepositoryItemWriter<Infomation> writer = new RepositoryItemWriter<>();
                //set reponsitory lưu dữ liệu
               writer.setRepository(infomationRepository);
                //đặt tên method save của JpaReponsitory để lưu dữ liệu
               writer.setMethodName("save");
               return writer;
    }


    private LineMapper<Infomation> LineMapper() {
        //dùng để ánh xạ từ dòng dữ liệu sang đối tượng infomation
        DefaultLineMapper<Infomation> lineMapper = new DefaultLineMapper<>();
        //dùng để phân tách dữ liệu trong mỗi dòng
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        //set ký tự dùng để phân tách dữ liệu
        lineTokenizer.setDelimiter(",");
        //bỏ qua các dòng dữ liệu có cấu trúc khác
        lineTokenizer.setStrict(false);
        //đặt tên cho các trường dữ liệu và thứ tự trong file
        lineTokenizer.setNames("id", "name", "classes", "scores");

        //để ánh xạ giá trị từ FieldSet sang các trường của đối tượng Infomation
        BeanWrapperFieldSetMapper<Infomation> fileSetMapper = new BeanWrapperFieldSetMapper<Infomation>();
        fileSetMapper.setTargetType(Infomation.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fileSetMapper);
        return lineMapper;
    }

    //dùng để thực hiển xử lý dữ liệu trung gian trước khi được ghi vào csdl
    @Bean
    public InfoItemProcessor processor() {
        return new InfoItemProcessor();
    }

    //Job thiết lập công việc chung cần được thực hiện trong ứng dụng
    @Bean
    public Job importUserJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("importUserJob", jobRepository)
                //flow định nghĩa các step sẽ thực hiện trong job
                //nếu step thành công sẽ được truyền cho importUserJob
                .flow(step1(jobRepository, transactionManager))
                .end()
                .build();
    }

    //Step là một công việc cụ thể
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                //chunk để tăng hiệu xuất khi xử lý dữ liệu
                .<Infomation, Infomation>chunk(10, transactionManager)
                // Chỉ định ItemReader được sử dụng để đọc dữ liệu.
                .reader(reader())
                //Chỉ định ItemProcessor được sử dụng để thực hiện xử lý trung gian trên dữ liệu
                .processor(processor())
                //Chỉ định ItemWriter được sử dụng để ghi dữ liệu vào cơ sở dữ liệu
                .writer(writer())
                .build();
    }

}

