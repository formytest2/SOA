package com.github.bluecatlee.gs4d.common.model;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationResult {

    // 校验结果是否有错
    private boolean hasErrors;

    // 校验错误信息
    private Map<String, String> errorMsg;

}
