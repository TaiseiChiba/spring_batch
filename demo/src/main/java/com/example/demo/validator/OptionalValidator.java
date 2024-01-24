package com.example.demo.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import io.micrometer.core.instrument.util.StringUtils;

public class OptionalValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        final String option1 = "option1";
        final String key = parameters.getString(option1);

        if (StringUtils.isEmpty(key)) {
            return ;
        }

        try {
            Integer.parseInt(option1);
        } catch(final NumberFormatException e) {
            final String errorMessage = "Not Number: value = " + option1;
            throw new JobParametersInvalidException(errorMessage);
        }
    }

}
