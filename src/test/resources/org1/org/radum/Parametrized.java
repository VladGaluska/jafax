package org.radum;

public class Parametrized<T extends DataObject> {

    private T obj;

    public void doSomething() {
        obj.x + obj.x;
    }

}