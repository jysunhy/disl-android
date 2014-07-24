package ch.usi.dag.et2.tools.etracks.test;


/**
 * Represents a simple application.
 */
public final class HelloWorld {

    int primitiveField = 12345678;

    int [] primitiveArray = new int [11];

    Object [] referenceArray = new Object [11];

    HelloWorld self;

    private void execute () {
        primitiveField = 12345678;
        primitiveArray [0] = 87654321;
        referenceArray [0] = new Object ();

        self = this;

        System.out.println (new String ("Hello world!"));

        self = this;
    }

    public static void main (final String [] args) {
        new HelloWorld ().execute ();
    }
}
