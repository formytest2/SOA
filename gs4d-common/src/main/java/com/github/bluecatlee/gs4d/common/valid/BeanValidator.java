package com.github.bluecatlee.gs4d.common.valid;

import javax.validation.Validation;
import javax.validation.Validator;

public class BeanValidator {
    public static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
}
