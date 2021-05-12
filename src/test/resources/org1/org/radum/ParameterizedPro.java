package org.radum;

public class ParameterizedPro<T extends DataObject, R, V> {

    private T obj1;

    private R obj2;

    private V obj3;

    public void doSomething() {
        obj1.x = obj1.x;
        String test2 = obj2.toString() + obj3.toString();
    }

}