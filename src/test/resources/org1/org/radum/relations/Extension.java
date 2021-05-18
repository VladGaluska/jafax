package org.radum.relations;

import org.radum.Client;
import org.radum.Provider;

public class Extension extends Base {

    public Extension() {

    }

    private void someMethod() {
        Client c = super.client;
        super.getTest();
        something(new Provider(3));
    }

}
