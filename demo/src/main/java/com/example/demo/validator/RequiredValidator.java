package com.example.demo.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import io.micrometer.core.instrument.util.StringUtils;

public class RequiredValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        final String key = "require1";
        final String require1 = parameters.getString(key);
        if (StringUtils.isEmpty(require1)) {
            final String errorMessage = "Not enterd: " + key;
            throw new JobParametersInvalidException(errorMessage);
        }
    }

}
