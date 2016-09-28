package ch.usi.dag.javamop.safeenum;

import java.lang.ref.WeakReference;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import ch.usi.dag.dislre.AREDispatch;

import com.runtimeverification.rvmonitor.java.rt.RuntimeOption;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfAll;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfMonitor;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfSetMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.DisableHolder;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IDisableHolder;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple3;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TerminatedMonitorCleaner;


final class SafeEnumMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeEnumMonitor> {

	SafeEnumMonitor_Set(){
		this.size = 0;
		this.elements = new SafeEnumMonitor[4];
	}
	final void event_create(final Vector v, final Enumeration e) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final SafeEnumMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeEnumMonitor monitorfinalMonitor = monitor;
				monitor.Prop_1_event_create(v, e);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
	final void event_updatesource(final Vector v) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final SafeEnumMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeEnumMonitor monitorfinalMonitor = monitor;
				monitor.Prop_1_event_updatesource(v);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
	final void event_next(final Enumeration e) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final SafeEnumMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeEnumMonitor monitorfinalMonitor = monitor;
				monitor.Prop_1_event_next(e);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
}

interface ISafeEnumMonitor extends IMonitor, IDisableHolder {
}

class SafeEnumDisableHolder extends DisableHolder implements ISafeEnumMonitor {
	SafeEnumDisableHolder(final long tau) {
		super(tau);
	}

	@Override
	public final boolean isTerminated() {
		return false;
	}

	@Override
	public int getLastEvent() {
		return -1;
	}

	@Override
	public int getState() {
		return -1;
	}

}

class SafeEnumMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject, ISafeEnumMonitor {
	@Override
    protected Object clone() {
		try {
			final SafeEnumMonitor ret = (SafeEnumMonitor) super.clone();
			ret.monitorInfo = (com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (final CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	WeakReference Ref_e = null;
	WeakReference Ref_v = null;
	int Prop_1_state;
	static final int Prop_1_transition_create[] = {1, 4, 4, 4, 4};;
	static final int Prop_1_transition_updatesource[] = {4, 2, 2, 4, 4};;
	static final int Prop_1_transition_next[] = {4, 1, 3, 4, 4};;

	boolean Prop_1_Category_match = false;

	SafeEnumMonitor(final long tau) {
		this.tau = tau;
		Prop_1_state = 0;

	}

	@Override
	public final int getState() {
		return Prop_1_state;
	}

	private final long tau;
	private long disable = -1;

	@Override
	public final long getTau() {
		return this.tau;
	}

	@Override
	public final long getDisable() {
		return this.disable;
	}

	@Override
	public final void setDisable(final long value) {
		this.disable = value;
	}

	final boolean Prop_1_event_create(final Vector v, final Enumeration e) {
		{
		}
		if(Ref_e == null){
			Ref_e = new WeakReference(e);
		}
		if(Ref_v == null){
			Ref_v = new WeakReference(v);
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_create[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 3;
		}
		return true;
	}

	final boolean Prop_1_event_updatesource(final Vector v) {
		Enumeration e = null;
		if(Ref_e != null){
			e = (Enumeration)Ref_e.get();
		}
		{
		}
		if(Ref_v == null){
			Ref_v = new WeakReference(v);
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_updatesource[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 3;
		}
		return true;
	}

	final boolean Prop_1_event_next(final Enumeration e) {
		Vector v = null;
		if(Ref_v != null){
			v = (Vector)Ref_v.get();
		}
		{
		}
		if(Ref_e == null){
			Ref_e = new WeakReference(e);
		}
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 3;
		}
		return true;
	}

	final void Prop_1_handler_match (){
		{
			System.out.println("improper enumeration usage at " + com.runtimeverification.rvmonitor.java.rt.ViolationRecorder.getLineOfCode());
			AREDispatch.NativeLog("improper enumeration usage at " + com.runtimeverification.rvmonitor.java.rt.ViolationRecorder.getLineOfCode());
			this.reset();
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	// RVMRef_v was suppressed to reduce memory overhead
	// RVMRef_e was suppressed to reduce memory overhead

	//alive_parameters_0 = [Vector v, Enumeration e]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Enumeration e]
	boolean alive_parameters_1 = true;

	@Override
	protected final void terminateInternal(final int idnum) {
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
		}
		switch(RVM_lastevent) {
			case -1:
			return;
			case 0:
			//create
			//alive_v && alive_e
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//updatesource
			//alive_e
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//next
			//alive_v && alive_e
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo monitorInfo;
	public static int getNumberOfEvents() {
		return 3;
	}

	public static int getNumberOfStates() {
		return 5;
	}

}

public class SafeEnumRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeEnumMapManager;
	static {
		SafeEnumMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeEnumMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock SafeEnum_RVMLock = new ReentrantLock();
	static final Condition SafeEnum_RVMLock_cond = SafeEnum_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeEnum_timestamp = 1;

	private static boolean SafeEnum_activated = false;

	// Declarations for Indexing Trees
	private static Object SafeEnum_e_Map_cachekey_e;
	private static Tuple2<SafeEnumMonitor_Set, SafeEnumMonitor> SafeEnum_e_Map_cachevalue;
	private static Object SafeEnum_v_Map_cachekey_v;
	private static Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> SafeEnum_v_Map_cachevalue;
	private static Object SafeEnum_v_e_Map_cachekey_e;
	private static Object SafeEnum_v_e_Map_cachekey_v;
	private static SafeEnumMonitor SafeEnum_v_e_Map_cachevalue;
	private static final MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor> SafeEnum_e_Map = new MapOfSetMonitor<SafeEnumMonitor_Set, SafeEnumMonitor>(1) ;
	private static final MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> SafeEnum_v_e_Map = new MapOfAll<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor>(0) ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += SafeEnum_e_Map.cleanUpUnnecessaryMappings();
		collected += SafeEnum_v_e_Map.cleanUpUnnecessaryMappings();
		return collected;
	}

	// Removing terminated monitors from partitioned sets
	static {
		TerminatedMonitorCleaner.start() ;
	}
	// Setting the behavior of the runtime library according to the compile-time option
	static {
		RuntimeOption.enableFineGrainedLock(false) ;
	}

	public static final void createEvent(final Vector v, final Enumeration e) {
		SafeEnum_activated = true;
		while (!SafeEnum_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_e = null;
		CachedWeakReference wr_v = null;
		MapOfMonitor<SafeEnumMonitor> matchedLastMap = null;
		SafeEnumMonitor matchedEntry = null;
		boolean cachehit = false;
		if (((e == SafeEnum_v_e_Map_cachekey_e) && (v == SafeEnum_v_e_Map_cachekey_v) ) ) {
			matchedEntry = SafeEnum_v_e_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_v = new CachedWeakReference(v) ;
			wr_e = new CachedWeakReference(e) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> node_v = SafeEnum_v_e_Map.getNodeEquivalent(wr_v) ;
				if ((node_v == null) ) {
					node_v = new Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor>() ;
					SafeEnum_v_e_Map.putNode(wr_v, node_v) ;
					node_v.setValue1(new MapOfMonitor<SafeEnumMonitor>(1) ) ;
					node_v.setValue2(new SafeEnumMonitor_Set() ) ;
				}
				final MapOfMonitor<SafeEnumMonitor> itmdMap = node_v.getValue1() ;
				matchedLastMap = itmdMap;
				final SafeEnumMonitor node_v_e = node_v.getValue1() .getNodeEquivalent(wr_e) ;
				matchedEntry = node_v_e;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_v == null) ) {
				wr_v = new CachedWeakReference(v) ;
			}
			if ((wr_e == null) ) {
				wr_e = new CachedWeakReference(e) ;
			}
			if ((matchedEntry == null) ) {
				// D(X) main:4
				final SafeEnumMonitor created = new SafeEnumMonitor(SafeEnum_timestamp++) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = true;

				matchedEntry = created;
				matchedLastMap.putNode(wr_e, created) ;
				// D(X) defineNew:5 for <e>
				{
					// InsertMonitor
					Tuple2<SafeEnumMonitor_Set, SafeEnumMonitor> node_e = SafeEnum_e_Map.getNodeEquivalent(wr_e) ;
					if ((node_e == null) ) {
						node_e = new Tuple2<SafeEnumMonitor_Set, SafeEnumMonitor>() ;
						SafeEnum_e_Map.putNode(wr_e, node_e) ;
						node_e.setValue1(new SafeEnumMonitor_Set() ) ;
					}
					final SafeEnumMonitor_Set targetSet = node_e.getValue1() ;
					targetSet.add(created) ;
				}
				// D(X) defineNew:5 for <v>
				{
					// InsertMonitor
					Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> node_v = SafeEnum_v_e_Map.getNodeEquivalent(wr_v) ;
					if ((node_v == null) ) {
						node_v = new Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor>() ;
						SafeEnum_v_e_Map.putNode(wr_v, node_v) ;
						node_v.setValue1(new MapOfMonitor<SafeEnumMonitor>(1) ) ;
						node_v.setValue2(new SafeEnumMonitor_Set() ) ;
					}
					final SafeEnumMonitor_Set targetSet = node_v.getValue2() ;
					targetSet.add(created) ;
				}
			}
			// D(X) main:6
			matchedEntry.setDisable(SafeEnum_timestamp++) ;
		}
		// D(X) main:8--9
		final SafeEnumMonitor matchedEntryfinalMonitor = matchedEntry;
		matchedEntry.Prop_1_event_create(v, e);
		if(matchedEntryfinalMonitor.Prop_1_Category_match) {
			matchedEntryfinalMonitor.Prop_1_handler_match();
		}

		if ((cachehit == false) ) {
			SafeEnum_v_e_Map_cachekey_e = e;
			SafeEnum_v_e_Map_cachekey_v = v;
			SafeEnum_v_e_Map_cachevalue = matchedEntry;
		}

		SafeEnum_RVMLock.unlock();
	}

	public static final void updatesourceEvent(final Vector v) {
		SafeEnum_activated = true;
		while (!SafeEnum_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_v = null;
		Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> matchedEntry = null;
		boolean cachehit = false;
		if ((v == SafeEnum_v_Map_cachekey_v) ) {
			matchedEntry = SafeEnum_v_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_v = new CachedWeakReference(v) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor> node_v = SafeEnum_v_e_Map.getNodeEquivalent(wr_v) ;
				if ((node_v == null) ) {
					node_v = new Tuple3<MapOfMonitor<SafeEnumMonitor>, SafeEnumMonitor_Set, SafeEnumMonitor>() ;
					SafeEnum_v_e_Map.putNode(wr_v, node_v) ;
					node_v.setValue1(new MapOfMonitor<SafeEnumMonitor>(1) ) ;
					node_v.setValue2(new SafeEnumMonitor_Set() ) ;
				}
				matchedEntry = node_v;
			}
		}
		// D(X) main:1
		final SafeEnumMonitor matchedLeaf = matchedEntry.getValue3() ;
		if ((matchedLeaf == null) ) {
			if ((wr_v == null) ) {
				wr_v = new CachedWeakReference(v) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				final SafeEnumMonitor created = new SafeEnumMonitor(SafeEnum_timestamp++) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = false;

				matchedEntry.setValue3(created) ;
				final SafeEnumMonitor_Set enclosingSet = matchedEntry.getValue2() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			final SafeEnumMonitor disableUpdatedLeaf = matchedEntry.getValue3() ;
			disableUpdatedLeaf.setDisable(SafeEnum_timestamp++) ;
		}
		// D(X) main:8--9
		final SafeEnumMonitor_Set stateTransitionedSet = matchedEntry.getValue2() ;
		stateTransitionedSet.event_updatesource(v);

		if ((cachehit == false) ) {
			SafeEnum_v_Map_cachekey_v = v;
			SafeEnum_v_Map_cachevalue = matchedEntry;
		}

		SafeEnum_RVMLock.unlock();
	}

	public static final void nextEvent(final Enumeration e) {
		SafeEnum_activated = true;
		while (!SafeEnum_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_e = null;
		Tuple2<SafeEnumMonitor_Set, SafeEnumMonitor> matchedEntry = null;
		boolean cachehit = false;
		if ((e == SafeEnum_e_Map_cachekey_e) ) {
			matchedEntry = SafeEnum_e_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_e = new CachedWeakReference(e) ;
			{
				// FindOrCreateEntry
				Tuple2<SafeEnumMonitor_Set, SafeEnumMonitor> node_e = SafeEnum_e_Map.getNodeEquivalent(wr_e) ;
				if ((node_e == null) ) {
					node_e = new Tuple2<SafeEnumMonitor_Set, SafeEnumMonitor>() ;
					SafeEnum_e_Map.putNode(wr_e, node_e) ;
					node_e.setValue1(new SafeEnumMonitor_Set() ) ;
				}
				matchedEntry = node_e;
			}
		}
		// D(X) main:1
		final SafeEnumMonitor matchedLeaf = matchedEntry.getValue2() ;
		if ((matchedLeaf == null) ) {
			if ((wr_e == null) ) {
				wr_e = new CachedWeakReference(e) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				final SafeEnumMonitor created = new SafeEnumMonitor(SafeEnum_timestamp++) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = false;

				matchedEntry.setValue2(created) ;
				final SafeEnumMonitor_Set enclosingSet = matchedEntry.getValue1() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			final SafeEnumMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
			disableUpdatedLeaf.setDisable(SafeEnum_timestamp++) ;
		}
		// D(X) main:8--9
		final SafeEnumMonitor_Set stateTransitionedSet = matchedEntry.getValue1() ;
		stateTransitionedSet.event_next(e);

		if ((cachehit == false) ) {
			SafeEnum_e_Map_cachekey_e = e;
			SafeEnum_e_Map_cachevalue = matchedEntry;
		}

		SafeEnum_RVMLock.unlock();
	}

}

