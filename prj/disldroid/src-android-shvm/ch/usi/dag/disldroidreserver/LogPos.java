package ch.usi.dag.disldroidreserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class LogPos {
    public final int pid;
    public final String pname;
    private ByteArrayOutputStream content = new ByteArrayOutputStream ();
    private int bytesRead = 0;
    private int bytesAnalyzed = 0;
    private int cleaned = 0;

    public synchronized int getBytesRead () {
        return bytesRead;
    }
    public synchronized void setBytesRead (final int bytesRead) {
        this.bytesRead = bytesRead;
    }
    public synchronized int getBytesAnalyzed () {
        return bytesAnalyzed;
    }
    public synchronized void addBytesAnalyzed (final int byteNum) {
        this.bytesAnalyzed += byteNum;
        if (bytesAnalyzed - cleaned > 1024 * 1024) {
            final ByteArrayOutputStream newContent = new ByteArrayOutputStream ();
            newContent.write (
                content.toByteArray (), bytesAnalyzed - cleaned, bytesRead
                    - bytesAnalyzed);
            cleaned = bytesAnalyzed;
            try {
                content.close ();
            } catch (final IOException e) {
                e.printStackTrace ();
            }

            System.out.println ("clean buffer every 1 MB");
            content = newContent;
        }
    }
    public synchronized boolean containUnAnalyzed(){
        return bytesAnalyzed < bytesRead;
    }
    public synchronized ByteBuffer leftToByteBuffer(){
        return ByteBuffer.wrap (
            content.toByteArray (),
            bytesAnalyzed-cleaned,
            bytesRead-bytesAnalyzed);
    }
    public LogPos(final int pid, final String pname){
        this.pid = pid;
        this.pname = pname;
    }
    public synchronized void write (final byte [] data, final int i, final int numOfBytesRead) {
        content.write (data, i, numOfBytesRead);
        bytesRead += numOfBytesRead;
    }
    static class IndexLog{
        public IndexLog(final int idx, final int size){
            index = idx;
            selfSize = size;
            sizeRead = 0;
        }
        int index;
        int selfSize;
        int sizeRead;
    }
    HashMap<Integer, IndexLog> indexInfo = new HashMap <Integer, IndexLog> ();
    public synchronized void updateIdxInfo (final int idx, final int size) {
        if(indexInfo.containsKey (idx)){
            if(indexInfo.get (idx).selfSize != size){
                System.err.println("Check why! "+size+" "+indexInfo.get (idx).selfSize );
            }
            return;
        }
        indexInfo.put (idx, new IndexLog (idx, size));
    }
    public synchronized int getSizeOfIdx(final int idx){
        if(!indexInfo.containsKey (idx)) {
            return 512*1024;
        }
        return indexInfo.get (idx).selfSize;
    }
    public synchronized int getCurrentIdx () {
//        if(!indexInfo.containsKey (0)) {
//            return 0;
//        }
        int res = 0;
        int i = 0;
        while(true) {
            if(!indexInfo.containsKey (i)) {
                return i;
            }
            if(res + indexInfo.get (i).selfSize <= bytesRead){
                res += indexInfo.get (i).selfSize;
                i++;
            }else {
                break;
            }
        }
        return i;
    }
    public synchronized int getBytesBefore (final int idx) {
        int res = 0;
        for(int i = 0; i < idx; i++){
            res += indexInfo.get (i).selfSize;
        }
        return res;
    }
}
