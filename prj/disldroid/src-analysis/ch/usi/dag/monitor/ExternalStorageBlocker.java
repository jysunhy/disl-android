package ch.usi.dag.monitor;

import java.io.File;

public class ExternalStorageBlocker {
    public File filenameFilter(String filePath) {
        File rec = null;
        String[] lst = ProviderUtil.getFileBlockList ();
        for (String element : lst) {
            if(filePath.contains (element)) {
                return rec;
            }
        }
        rec = new File(filePath);
        return rec;
    }
}
