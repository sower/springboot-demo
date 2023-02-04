package me.demo.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = AnyOfValidator.class)
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AnyOf {

  String message() default "";

  String[] values();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
