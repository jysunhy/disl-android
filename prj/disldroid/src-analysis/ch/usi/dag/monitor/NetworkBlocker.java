package ch.usi.dag.monitor;

import java.net.URL;

import ch.usi.dag.dislre.AREDispatch;

public class NetworkBlocker {
    public URL urlFilter(String url) {
        URL rec = null;
        String[] lst = ProviderUtil.getUrlBlockList ();
        for (String element : lst) {
            if(url.contains (element)) {
                return rec;
            }
        }
        try {
            rec = new URL(url);
        } catch (Exception e) {
            AREDispatch.NativeLog(e.toString());
        }
        return rec;
    }
    
    public URL urlRedirect(String url) {
        URL rec = null;
        String[] lst = ProviderUtil.getUrlRedirectList ();
        for (String element : lst) {
            if(url.contains (element)) {
                return rec;
            }
        }
        try {
            rec = new URL(url);
        } catch (Exception e) {
            AREDispatch.NativeLog(e.toString());
        }
        return rec;
    }
}