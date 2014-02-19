package ch.usi.dag.dislreserver.msg.analyze.mtdispatch;

import ch.usi.dag.dislreserver.shadow.ShadowAddressSpace;

class ObjectFreeTask {

	protected boolean signalsEnd = false;
	protected long[] objFreeIDs;
	protected long closingEpoch;
	protected ShadowAddressSpace shadowAddressSpace;

	/**
	 * Constructed task signals end of the processing
	 */
	public ObjectFreeTask() {
		signalsEnd = true;
	}

	public ObjectFreeTask(final ShadowAddressSpace shadowAddressSpace, final long[] objFreeIDs, final long closingEpoch) {
		super();
		this.shadowAddressSpace = shadowAddressSpace;
		this.objFreeIDs = objFreeIDs;
		this.closingEpoch = closingEpoch;
	}

	public boolean isSignalingEnd() {
		return signalsEnd;
	}

    public ShadowAddressSpace getShadowAddressSpace () {
        return shadowAddressSpace;
    }

	public long[] getObjFreeIDs() {
		return objFreeIDs;
	}

	public long getClosingEpoch() {
		return closingEpoch;
	}
}
