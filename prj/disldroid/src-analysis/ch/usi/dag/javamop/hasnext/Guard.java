package ch.usi.dag.javamop.hasnext;

import ch.usi.dag.disl.annotation.GuardMethod;

public class Guard{
    static int hasNextCnt = 0;
    static int nextCnt = 0;
    public static class CounterHasNext{
        @GuardMethod
        public static boolean isApplicable () {
            hasNextCnt++;
            System.out.println("hasNext " + hasNextCnt);
            return true;
        }
    }
    public static class CounterNext{
        @GuardMethod
        public static boolean isApplicable () {
            nextCnt++;
            System.out.println("next "+nextCnt);
            return true;
        }
    }
}
