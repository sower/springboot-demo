package me.demo.aspect;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验器
 * @date 2023/02/04
 **/
public class AnyOfValidator implements ConstraintValidator<AnyOf, String> {

  private Set<String> values;
  private String msg = null;

  @Override
  public void initialize(AnyOf constraintAnnotation) {
    values = Arrays.stream(constraintAnnotation.values())
        .collect(Collectors.toSet());
    msg =
        "Value only is " + values.stream().collect(Collectors.joining(",", "[", "]"));
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (values.contains(value)) {
      return true;
    }

    if (context.getDefaultConstraintMessageTemplate().isEmpty()) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(this.msg).addConstraintViolation();
    }
    return false;
  }
}
