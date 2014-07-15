

public class TargetClass {

    public static int foo (final int i) {
        if (i >= 0) {
            switch (i) {
            case 0:
            case 1:
                return i;

            default:
                return foo (i - 1) + foo (i - 2);
            }
        } else {
            return -1;
        }
    }


    public static void main (final String [] args) throws InterruptedException {
        System.out.println (foo (1));
    }
}
