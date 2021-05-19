package org.radum.metrics;

public class SomeOtherBase {

    protected final static OtherBase base = null;

    public SomeOtherBase() {
        String x = base.test;
        String y = base.secondTest;
    }

    protected void doSth() {
        int x = base.x;
        String y = base.test;
        base.getBase();
        OtherBase.setX(x);
    }
}
