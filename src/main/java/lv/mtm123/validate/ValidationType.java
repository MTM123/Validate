package lv.mtm123.validate;

import com.google.common.primitives.Primitives;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public enum ValidationType {

    NOT_NULL(false, (a, b) -> Objects.nonNull(a)),
    NOT_EMPTY(false, (a, b) -> {

        if(a != null) {
            if (a.getClass().isArray()) {

                Class component = a.getClass().getComponentType();
                if (component.isPrimitive()) {
                    return true;
                }

                Object[] objects = (Object[]) a;
                for (Object o : objects) {
                    if (o != null) {
                        return true;
                    }
                }

                return false;
            } else if (a instanceof Collection) {
                return !((Collection) a).isEmpty();
            }
        }

        throw new IllegalArgumentException("Comparison only applicable to arrays and collections! Found '"
                + (a == null ? null : a.getClass()) + "'");
    }),
    NOT_EQUAL(true, (a, b) -> {

        if (a != null && a.getClass().isPrimitive()){

        } else {

        }

        throw new IllegalArgumentException("");
    }),
    GREATER_THAN(true, (a, b) -> {
        if (!(a instanceof Number && b instanceof Number)) {
            throw new IllegalArgumentException("You can't compare those types: " + a + ", " + b);
        }

        BigDecimal n1 = BigDecimal.valueOf(((Number) a).doubleValue());
        BigDecimal n2 = BigDecimal.valueOf(((Number) b).doubleValue());

        return n1.compareTo(n2) > 0;
    }),
    LESS_THAN(true, (a, b) -> {
        if (!(a instanceof Number && b instanceof Number)) {
            throw new IllegalArgumentException("You can't compare those types: " + a + ", " + b);
        }

        BigDecimal n1 = BigDecimal.valueOf(((Number) a).doubleValue());
        BigDecimal n2 = BigDecimal.valueOf(((Number) b).doubleValue());

        return n1.compareTo(n2) < 0;
    }),
    NONE(false, (a, b) -> true);

    private static final Map<Class, MethodHandle> METHOD_HANDLES;

    static {
        METHOD_HANDLES = new HashMap<>();

        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

        try {

            MethodType mt = MethodType.methodType(int.class, String.class);
            METHOD_HANDLES.put(int.class, publicLookup.findStatic(Integer.class, "parseInt", mt));

            mt = MethodType.methodType(byte.class, String.class);
            METHOD_HANDLES.put(byte.class, publicLookup.findStatic(Byte.class, "parseByte", mt));

            mt = MethodType.methodType(short.class, String.class);
            METHOD_HANDLES.put(short.class, publicLookup.findStatic(Short.class, "parseShort", mt));

            mt = MethodType.methodType(long.class, String.class);
            METHOD_HANDLES.put(long.class, publicLookup.findStatic(Long.class, "parseLong", mt));

            mt = MethodType.methodType(float.class, String.class);
            METHOD_HANDLES.put(float.class, publicLookup.findStatic(Float.class, "parseFloat", mt));

            mt = MethodType.methodType(double.class, String.class);
            METHOD_HANDLES.put(double.class, publicLookup.findStatic(Double.class, "parseDouble", mt));

            mt = MethodType.methodType(boolean.class, String.class);
            METHOD_HANDLES.put(boolean.class, publicLookup.findStatic(Boolean.class, "parseBoolean", mt));

        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private final boolean needsValue;
    private final BiFunction<Object, Object, Boolean> function;

    ValidationType(boolean needsValue, BiFunction<Object, Object, Boolean> function) {
        this.needsValue = needsValue;
        this.function = function;
    }

    public boolean verify(Field f, Object a, String value) {

        Class<?> type = f.getType();

        switch (this){
            case NOT_EMPTY:
                if(!type.isAssignableFrom(Collection.class) && !type.isArray()){
                    throw new IllegalArgumentException("Comparison only applicable to arrays and collections! Found '"
                            + (type.getCanonicalName()) + "'");
                }
            case NOT_EQUAL:
                if(Primitives.isWrapperType(type)) {

                    MethodHandle handle = METHOD_HANDLES.get(Primitives.unwrap(type));

                    //Boolean.parseBoolean();

                    try {
                        handle.invoke(value);
                    } catch (Throwable throwable) {
                        if (throwable instanceof NumberFormatException){
                            throw new IllegalArgumentException("Incorrect value ");
                        } else {
                            throwable.printStackTrace();
                        }
                    }

                } else if(type.isAssignableFrom(String.class)) {

                } else {
                    throw new IllegalArgumentException("Comparison only applicable to primitive types and strings! Found'"
                            + (type.getCanonicalName()) + "'");
                }

            case GREATER_THAN:
            case LESS_THAN:
        }

        return true;

    }

/*    public <T> boolean verify(Class<T> type, T a) {

        if(needsValue){
            throw new IllegalArgumentException("You must a value provide to perform '" + name() + "' verification!");
        }

        return verify(a, null);

    }*/

}
