package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.validator.OptionalValidator;
import com.example.demo.validator.RequiredValidator;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("HelloTasklet")
    private Tasklet helloTasklet;

    @Autowired
    @Qualifier("HelloTasklet2")
    private Tasklet helloTasklet2;

    @Bean
    public JobParametersValidator defaultValidator() {
        final DefaultJobParametersValidator validator = new DefaultJobParametersValidator();

        // 必須入力
        final String [] requireKeys = new String[] {"run.id", "require1"};
        validator.setRequiredKeys(requireKeys);

        // オプション入力
        final String[] optionalKeys = new String[] {"option1"};
        validator.setOptionalKeys(optionalKeys);

        // Key名の重複がないかを確認
        validator.afterPropertiesSet();

        return validator;
    }

    @Bean
    public JobParametersValidator compsiteVaidator() {
        final List<JobParametersValidator> validators = new ArrayList<>();
        validators.add(defaultValidator());
        validators.add(new RequiredValidator());
        validators.add(new OptionalValidator());

        final CompositeJobParametersValidator compositeValidator = new CompositeJobParametersValidator();
        compositeValidator.setValidators(validators);

        return compositeValidator;
    }

    /**
     * TaskletのStepを生成
     */
    @Bean
    public Step taskletStep1() {
        return stepBuilderFactory.get("HelloTaskletStep1") // Builderの取得
                .tasklet(helloTasklet) // Taskletのセット
                .build(); // Stepの生成
    }

    @Bean
    public Step taskletStep2() {
        return stepBuilderFactory.get("HelloTaskletStep2") // Builderの取得
                .tasklet(helloTasklet2) // Taskletのセット
                .build(); // Stepの生成
    }

    /**
     * Jobを生成
     */
    @Bean
    public Job taskletJob() throws Exception {
        return jobBuilderFactory.get("HelloWorldTaskletJob") // Builderの取得
                .incrementer(new RunIdIncrementer())
                .start(taskletStep1()) // 最初のStep
                .next(taskletStep2())
                .validator(compsiteVaidator())
                .build(); // Jobを生成
    }
}
