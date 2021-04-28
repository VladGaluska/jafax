package org.radum;

public class Client {

    Provider dataMember = ProviderFactory.getProvider();

    ExtendedData extendedData = new ExtendedData();


    public void useObject(Provider dprovider) {
        System.err.println(dprovider.measureComplexity(2, 3));
        dprovider.getData().x++;
        dataMember.getData().y++;
    }

    public void anotherFunction(ExtendedData extendedData ) {
        dataMember.measureComplexity(1,2);
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

    public static void main(String[] args) {
    }
}

class Empty {

    class SubEmpty {

    }

}