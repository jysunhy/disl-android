package javamop;

public class ConstrOSynInvocationMarker extends AbstractInvocationMarker{
    @Override
    public String getMethodDescription(){
        return "* java.util.Collections.synchr*(..)";
    }
}
