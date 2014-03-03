package ch.usi.dag.dislreserver.shadow;

public interface Replicable {

    Replicable replicate(ShadowAddressSpace shadowAddressSpace);

}
