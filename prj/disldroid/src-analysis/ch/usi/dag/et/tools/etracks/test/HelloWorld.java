package ch.usi.dag.et.tools.etracks.test;


/**
 * Represents a simple application.
 */
public final class HelloWorld {

    int primitiveField = 12345678;

    int [] primitiveArray = new int [11];

    Object [] referenceArray = new Object [11];

    private void execute () {
        primitiveField = 12345678;
        primitiveArray [0] = 87654321;
        referenceArray [0] = new Object ();

        System.out.println (new String ("Hello world!"));
    }

    public static void main (final String [] args) {
        new HelloWorld ().execute ();
    }
}
