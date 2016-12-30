package ch.usi.dag.disldroidserver;

import java.util.HashMap;

import ch.usi.dag.disl.DiSL;

public class AndroidInstrumenter {
    public static HashMap<String, DiSL> dislMap = new HashMap <String, DiSL>();
    public static final boolean debug = Boolean
    .getBoolean (DiSLServer.PROP_DEBUG);

    public static void checkConfigXMLChange(){
        if(!DiSLConfig.parseXml ()){
            if(AndroidInstrumenter.debug) {
                System.out.println("Detect config change");
            }
            AndroidInstrumenter.dislMap = new HashMap <String, DiSL>();
        }
        return;
    }
}
