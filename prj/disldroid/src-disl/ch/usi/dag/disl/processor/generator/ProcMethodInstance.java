package ch.usi.dag.disl.processor.generator;

import ch.usi.dag.disl.processor.ProcArgType;
import ch.usi.dag.disl.processor.ProcCode;


public class ProcMethodInstance {

    private int argPos;
    private int argsCount;
    private ProcArgType argType;
    private String argTypeDesc;
    private ProcCode code;

    public ProcMethodInstance(int argPos, int argsCount, ProcArgType argType,
            String argTypeDesc, ProcCode code) {
        super();
        this.argPos = argPos;
        this.argsCount = argsCount;
        this.argType = argType;
        this.code = code;
        this.argTypeDesc = argTypeDesc;
    }

    public int getArgPos() {
        return argPos;
    }

    public int getArgsCount() {
        return argsCount;
    }

    public ProcArgType getArgType() {
        return argType;
    }

    public String getArgTypeDesc() {
        return argTypeDesc;
    }

    // Note: Code is NOT cloned for each instance of ProcMethodInstance.
    // If the weaver does not rely on this, we can reuse processor instances
    // which can save us some computation
    public ProcCode getCode() {
        return code;
    }
}
