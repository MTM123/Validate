package lv.mtm123.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Validate {

    ValidationType type() default ValidationType.NONE;

    String value() default "";

    boolean recursive() default false;

}
