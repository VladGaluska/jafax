package org.radum;

import java.io.Serializable;

public class Provider implements IProvider, Serializable {

    public Provider(int a) {
        int x = 2;
    }

    public int measureComplexity(int i, int j) {
        int result;
        if(i % 2 == 0) {
            result = i + j;
        }
        else {
            if(j % 2 == 0) {
                result = i * j;
            }
            else {
                result = i - j;
            }
        }
        return result;
    }

    public int anotherFunction() {
        DataObject localData = data;

        localData.x = 10;


        return data.x + data.y;
    }

    public DataObject data;
    public ExtendedData extendedData;

    IProvider anonymousProvider = new IProvider() {

        public int anotherFunction() {
            return data.x + data.y;
        }

    };

    public DataObject getData() {
        Parameterized<ExtendedData> parameterized = new Parameterized<>();
        ParameterizedPro<DataObject, Integer, String> value = new ParameterizedPro<>();
        parameterized.doSomething((ExtendedData) data);
        return data;
    }

}

class ProviderFactory {

    public static Provider getProvider() {
        return new Provider(2);
    }

}
