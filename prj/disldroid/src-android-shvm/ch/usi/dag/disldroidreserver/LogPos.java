package ch.usi.dag.disldroidreserver;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class LogPos {
    private final String id;
    private final ByteArrayOutputStream content = new ByteArrayOutputStream ();
    private int bytesRead = 0;
    private int bytesAnalyzed = 0;
    private final int cleaned = 0;

    public synchronized int getBytesRead () {
        return bytesRead;
    }
    public synchronized void setBytesRead (final int bytesRead) {
        this.bytesRead = bytesRead;
    }
    public synchronized int getBytesAnalyzed () {
        return bytesAnalyzed;
    }
    public synchronized void setBytesAnalyzed (final int bytesAnalyzed) {
        this.bytesAnalyzed = bytesAnalyzed;
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
    public LogPos(final String id){
        this.id = id;
    }
    public synchronized void write (final byte [] data, final int i, final int numOfBytesRead) {
//        if(bytesAnalyzed - cleaned> 1000000){
//            final ByteArrayOutputStream newContent = new ByteArrayOutputStream ();
//            newContent.write (content.toByteArray (), bytesAnalyzed - cleaned, bytesRead - bytesAnalyzed);
//            cleaned = bytesAnalyzed;
//            try {
//                content.close ();
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println("clean buffer every 1 MB");
//            content = newContent;
//        }
        content.write (data, i, numOfBytesRead);
        bytesRead += numOfBytesRead;
    }
}
