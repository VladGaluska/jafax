package org.radum.metrics;

public class SomeOtherOtherBase {
    
    private static OtherBase base;
    
    public static void other() {
        int x = base.x;
        String y = base.test;
        base.getBase();
        OtherBase.setX(3);
    }
    
}
