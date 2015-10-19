package ch.usi.dag.demo.utils;

public class DemoUtils {
    public static int indexOf(final byte[] outerArray, final byte[] smallerArray) {
        for(int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
               if (outerArray[i+j] != smallerArray[j]) {
                   found = false;
                   break;
               }
            }
            if (found) {
                return i;
            }
         }
       return -1;
    }
}
