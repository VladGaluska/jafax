package org.radum;

public class ExtendedData extends DataObject {
    Provider providerObject;

    public ExtendedData() {
        providerObject = new Provider();
    }
    public void setProvider(Provider _p) {
        providerObject = _p;
    }
}
