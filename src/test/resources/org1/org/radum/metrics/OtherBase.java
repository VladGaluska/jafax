package org.radum.metrics;

import org.radum.DataObject;
import org.radum.Provider;

abstract class OtherBase {

    public static int x;

    public final String test = "";

    public String secondTest;

    OtherBase base;
    
    public OtherExtension otherExtension;
    
    protected int y;

    protected OtherBase() {
        
    }

    public void setBase(OtherBase base) {
        this.base = base;
    }

    public OtherBase getBase() {
        return base;
    }

    public static void setX(int smallX){
        x = smallX;
    }
    
    class Internal {
        private Provider p;
        
        public void doInternal() {
            DataObject dataObject = p.data;
        }
    }

    public void useInternal() {
        Provider p = new Internal().p;
    }
    
    abstract public void abstractMethod();
    
}
