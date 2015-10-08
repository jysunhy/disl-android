package ch.usi.dag.disldroidreserver.msg.ipc;

public class TransactionInfo {
    int transactionId;
    boolean oneway;

    public boolean isOneway () {
        return oneway;
    }
    public void setOneway (final boolean oneway) {
        this.oneway = oneway;
    }
    public TransactionInfo(final int id, final boolean oneway){
        transactionId = id;
        this.oneway = oneway;
    }
    public int getTransactionId(){
        return transactionId;
    }
    public void setTransactionId(final int id){
        transactionId = id;
    }

    @Override
    public String toString(){
        return "("+transactionId+":"+(oneway?"oneway":"twoway")+")";
    }
    @Override
    public boolean equals(final Object info){
        if(info == null) {
            return false;
        }
        if(!(info instanceof TransactionInfo)){
            return false;
        }
        return this.transactionId == ((TransactionInfo)info).transactionId && this.oneway == ((TransactionInfo)info).oneway;
    }
}
