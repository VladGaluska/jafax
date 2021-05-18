package org.radum.relations;

import org.radum.*;

class Base {

    protected Client client;

    private ParameterizedPro<DataObject, Client, ParameterizedPro> test;

    public ParameterizedPro<DataObject, Client, ParameterizedPro> getTest() {
        return test;
    }

    protected <T extends Provider> T something(T t) {
        return t;
    }

    private Client container() {
        class Nested {
            Provider nested() {
                Provider p = new Provider(3);
                client.useObject(p);
                client.something(client);
                return p;
            }
        }
        new Nested().nested();
        return new Client();
    }

    class AlsoNested {
        AlsoNested() {
            new DataObject().setX(3);
        }
    }

}