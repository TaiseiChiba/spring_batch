package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("HelloTasklet")
@StepScope
@Slf4j
public class HelloTasklet implements Tasklet {

    @Value("#{jobParameters['require1']}")
    private String require1;

    @Value("#{jobParameters['option1']}")
    private Integer option1;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Hello World");

        // ExecutionContextはjobとstepの実行結果などを持つクラス
        final ExecutionContext jobCotext = contribution.getStepExecution()
                .getJobExecution()
                .getExecutionContext();
        jobCotext.put("jobKey", "jobValue");

        final ExecutionContext stepContext = contribution.getStepExecution()
                .getExecutionContext();
        stepContext.put("stepKey", "stepJob");

        // jobParametersの確認
        log.info("requier1:{}", require1);
        log.info("option1:{}", option1);

        return RepeatStatus.FINISHED;
    }


}
