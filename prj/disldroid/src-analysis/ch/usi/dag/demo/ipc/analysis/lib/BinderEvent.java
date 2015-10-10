package ch.usi.dag.demo.ipc.analysis.lib;

import ch.usi.dag.disldroidreserver.msg.ipc.NativeThread;
import ch.usi.dag.disldroidreserver.msg.ipc.TransactionInfo;

public class BinderEvent {
    public enum EventType{REQUEST_SENT,REQUEST_RECVD, RESPONSE_SENT, RESPONSE_RECVD};

    NativeThread client=null;
    NativeThread server=null;

    @Override
    public String toString(){
        if(server!=null) {
            return "from("+client.getPid ()+":"+client.getTid ()+")-("+info.getTransactionId ()+(info.isOneway ()?"oneway":"twoway")+type+") to("+server.getPid ()+":"+server.getTid ()+")";
        }else{
            return "from("+client.getPid ()+":"+client.getTid ()+")-("+info.getTransactionId ()+(info.isOneway ()?"oneway":"twoway")+type+")";
        }
    }

    public NativeThread getClient () {
        return client;
    }
    public void setClient (final NativeThread client) {
        this.client = client;
    }
    public NativeThread getServer () {
        return server;
    }
    public void setServer (final NativeThread server) {
        this.server = server;
    }
    TransactionInfo info;
    EventType type;

    public TransactionInfo getInfo () {
        return info;
    }
    public void setInfo (final TransactionInfo info) {
        this.info = info;
    }
    public EventType getType () {
        return type;
    }
    public void setType (final EventType type) {
        this.type = type;
    }
    public static class RequestSentEvent extends BinderEvent{
        public RequestSentEvent (final NativeThread client, final TransactionInfo info) {
            this.client=client;
            this.info=info;
            type=EventType.REQUEST_SENT;
        }
    }
    public static class RequestRecvdEvent extends BinderEvent{
        public RequestRecvdEvent (final NativeThread client, final NativeThread server, final TransactionInfo info) {
            this.client=client;
            this.server=server;
            this.info=info;
            type=EventType.REQUEST_RECVD;
        }
    }

    public static class ResponseSentEvent extends BinderEvent{
        public ResponseSentEvent (final NativeThread client, final NativeThread server, final TransactionInfo info) {
            this.client = client;
            this.server=server;
            this.info=info;
            type=EventType.RESPONSE_SENT;
        }
    }

    public static class ResponseRecvdEvent extends BinderEvent{
        public ResponseRecvdEvent (final NativeThread client,final NativeThread server, final TransactionInfo info) {
            this.client=client;
            this.server=server;
            this.info=info;
            type=EventType.RESPONSE_RECVD;
        }

    }

    public boolean isSameTransaction(final BinderEvent event){
        return (this.client.equals (event.client) && this.info.equals (event.info));
    }

}