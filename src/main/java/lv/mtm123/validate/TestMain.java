package lv.mtm123.validate;

import com.google.common.primitives.Primitives;

public class TestMain {

    public static void main(String[] args){
        test("343");
        test(422);
        test('c');

    }

    private static void test(Object stuff){

        System.out.println(stuff.getClass().isPrimitive());
        System.out.println(Primitives.isWrapperType(stuff.getClass()));
    }

}
