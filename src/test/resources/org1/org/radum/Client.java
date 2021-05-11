package org.radum;

import java.util.ArrayList;
import java.util.List;

public class Client {

    Provider dataMember = ProviderFactory.getProvider();

    Provider[] providers;

    ExtendedData extendedData = new ExtendedData();


    public void useObject(Provider dprovider) {
        System.err.println(dprovider.measureComplexity(2, 3));
        dprovider.getData().x++;
        dataMember.getData().y++;
    }

    public void anotherFunction(final ExtendedData extendedData ) {
        dataMember.measureComplexity(1,2);
        List<String> values = new ArrayList<>();
        values.add("asd");
        values.add("sda");
        values.stream()
              .forEach(z -> z + extendedData.x);
    }

    public void client() {
        int unused;
        unused = 3;
        int result = dataMember.anotherFunction();

        if(result > 2) System.err.println("high");
        else {
            if (result < 0) System.err.println("negative");
            else System.err.println("low");
        }
    }

    public <R extends Client> R something(R r) {
        r.client();
        return r;
    }

    public <? extends Client> something2;

    public static void main(String[] args) {
    }
}

class Empty {

    class SubEmpty {

    }

}