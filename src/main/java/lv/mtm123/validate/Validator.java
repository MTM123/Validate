package lv.mtm123.validate;

import java.lang.reflect.Field;
import java.util.Collection;

public class Validator {

    public static void verify(Object obj) throws IllegalAccessException {

        for (Field f : obj.getClass().getDeclaredFields()) {

            if (!f.isAnnotationPresent(Validate.class))
                continue;

            Validate v = f.getAnnotation(Validate.class);

            f.setAccessible(true);
            Object val = f.get(obj);
            f.setAccessible(false);

            if (v.type() == ValidationType.NONE) {

                if (!v.recursive()) {
                    continue;
                }

                if (!isValidForRecursiveAnalysis(f.getType())) {
                    continue;
                }

                verify(val);

            } else {
                //v.type().verify(f.getType(), val, v.value());
            }

        }

    }

    private static boolean isValidForRecursiveAnalysis(Class<?> type) {
        return !type.isAssignableFrom(Collection.class) && !type.isPrimitive() && !type.isArray();
    }

}
