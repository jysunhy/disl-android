package ch.usi.dag.disldroidreserver.shadow;

public interface Forkable {

    void onFork(Context parent, Context child);

}
