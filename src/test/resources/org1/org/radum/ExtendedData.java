package org.radum;

public class ExtendedData extends DataObject {
    Provider providerObject;

    public ExtendedData() {
        this(2);
        providerObject = new Provider(3);
    }

    public ExtendedData(int x) {
        super();
        super.x = x;
    }

    public void setProvider(Provider _p) {
        providerObject = _p;
    }
}
