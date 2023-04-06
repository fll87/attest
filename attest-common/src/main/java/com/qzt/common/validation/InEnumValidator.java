package com.qzt.common.validation;

import com.qzt.common.core.domain.IntArrayValuable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InEnumValidator implements ConstraintValidator<InEnum, Object> {

    private List<Integer> enumValues;
    private String[] strValues;
    private int[] intValues;
    private Integer type;

    @Override
    public void initialize(InEnum annotation) {
        if(annotation.strValues().length > 0){
            this.type = 1;
            this.strValues = annotation.strValues();
        }else if(annotation.intValues().length > 0){
            this.type = 2;
            this.intValues = annotation.intValues();
        }else {
            this.type = 3;
            IntArrayValuable[] values = annotation.enumValues().getEnumConstants();
            if (values.length == 0) {
                this.enumValues = Collections.emptyList();
            } else {
                this.enumValues = Arrays.stream(values[0].array()).boxed().collect(Collectors.toList());
            }
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 为空时，默认不校验，即认为通过
        if (value == null) {
            return true;
        }
        String tip = "";
        if (this.type == 1) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
            tip = strValues.toString();
        } else if (this.type == 2) {
            for (int s : intValues) {
                if (s == ((Integer) value).intValue()) {
                    return true;
                }
            }
            tip = intValues.toString();
        }else{
            if (enumValues.contains(value)){
                return true;
            }
            tip = enumValues.toString();
        }
        // 校验不通过，自定义提示语句（因为，注解上的 value 是枚举类，无法获得枚举类的实际值）
        context.disableDefaultConstraintViolation(); // 禁用默认的 message 的值
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
                .replaceAll("\\{value}", tip)).addConstraintViolation(); // 重新添加错误提示语句
        return false;
    }

}

