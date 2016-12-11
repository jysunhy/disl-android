package javamop;

import ch.usi.dag.disl.annotation.GuardMethod;

public class Guard{
    static int arr[] = new int[7];
    public static void printCounters(){
        System.out.println (
            (FileClose.counter-arr[0])
            + " " + (HasNext.counter-arr[1])
            + " " + (SafeEnum.counter-arr[2])
            + " " + (SafeFile.counter-arr[3])
            + " " + (SafeSyncMap.counter-arr[4])
            + " " + (UnsafeIterator.counter-arr[5])
            + " " + (UnsafeMap.counter-arr[6]));
        arr[0] = FileClose.counter;
        arr[1] = HasNext.counter;
        arr[2] = SafeEnum.counter;
        arr[3] = SafeFile.counter;
        arr[4] = SafeSyncMap.counter;
        arr[5] = UnsafeIterator.counter;
        arr[6] = UnsafeMap.counter;
    }
    public static class FileClose{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("FileClose " + counter);
            return true;
        }
    }
    public static class HasNext{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("HasNext " + counter);
            return true;
        }
    }
    public static class SafeEnum{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("SafeEnum " + counter);
            return true;
        }
    }
    public static class SafeFile{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("SafeFile " + counter);
            return true;
        }
    }
    public static class SafeSyncMap{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("SafeSyncMap " + counter);
            return true;
        }
    }
    public static class UnsafeIterator{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("UnsafeIterator " + counter);
            return true;
        }
    }
    public static class UnsafeMap{
        static int counter;
        @GuardMethod
        public static boolean isApplicable () {
            counter++;
            //System.out.println("UnsafeMap " + counter);
            return true;
        }
    }
}
