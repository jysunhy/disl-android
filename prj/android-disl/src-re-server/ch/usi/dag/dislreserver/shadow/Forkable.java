package ch.usi.dag.dislreserver.shadow;

public interface Forkable {

    void onFork(Context parent, Context child);

}
