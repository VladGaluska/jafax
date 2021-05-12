package org.radum;

public class ParameterizedExtension extends Parameterized<ExtendedData> {

    public void doSomething(ExtendedData extension) {
        extension.x = extension.y;
    }

}