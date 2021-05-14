package org.radum;

public class Parameterized<T extends DataObject> {

    private T obj;

    private ParameterizedPro<T, String, Long> parameterizedPro;

    public void doSomething(T smt) {
        parameterizedPro = new ParameterizedPro<>();
        smt.a();
        int test = obj.x + obj.x;
    }

}