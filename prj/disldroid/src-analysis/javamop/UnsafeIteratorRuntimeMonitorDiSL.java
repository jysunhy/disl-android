package javamop;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.lang.ref.*;
import com.runtimeverification.rvmonitor.java.rt.*;
import com.runtimeverification.rvmonitor.java.rt.ref.*;
import com.runtimeverification.rvmonitor.java.rt.table.*;
import com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractIndexingTree;
import com.runtimeverification.rvmonitor.java.rt.tablebase.SetEventDelegator;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple3;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IDisableHolder;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.DisableHolder;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TerminatedMonitorCleaner;
import java.util.concurrent.atomic.AtomicInteger;


final class UnsafeIteratorMonitor_SetDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<UnsafeIteratorMonitorDiSL> {

	UnsafeIteratorMonitor_SetDiSL(){
		this.size = 0;
		this.elements = new UnsafeIteratorMonitorDiSL[4];
	}
	final void event_create(Collection c, Iterator i) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_create(c, i);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
	final void event_updatesource(Collection c) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_updatesource(c);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
	final void event_next(Iterator i) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_next(i);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}
			}
		}
		for(int i_1 = numAlive; i_1 < this.size; i_1++){
			this.elements[i_1] = null;
		}
		size = numAlive;
	}
}

interface IUnsafeIteratorMonitorDiSL extends IMonitor, IDisableHolder {
}

class UnsafeIteratorDisableHolder extends DisableHolder implements IUnsafeIteratorMonitorDiSL {
	UnsafeIteratorDisableHolder(long tau) {
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

class UnsafeIteratorMonitorDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject, IUnsafeIteratorMonitorDiSL {
	protected Object clone() {
		try {
			UnsafeIteratorMonitorDiSL ret = (UnsafeIteratorMonitorDiSL) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	WeakReference Ref_c = null;
	WeakReference Ref_i = null;
	int Prop_1_state;
	static final int Prop_1_transition_create[] = {1, 4, 4, 4, 4};;
	static final int Prop_1_transition_updatesource[] = {4, 2, 2, 4, 4};;
	static final int Prop_1_transition_next[] = {4, 1, 3, 4, 4};;

	boolean Prop_1_Category_match = false;

	UnsafeIteratorMonitorDiSL(long tau) {
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
	public final void setDisable(long value) {
		this.disable = value;
	}

	final boolean Prop_1_event_create(Collection c, Iterator i) {
		{
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		if(Ref_i == null){
			Ref_i = new WeakReference(i);
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_create[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_updatesource(Collection c) {
		Iterator i = null;
		if(Ref_i != null){
			i = (Iterator)Ref_i.get();
		}
		{
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_updatesource[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_next(Iterator i) {
		Collection c = null;
		if(Ref_c != null){
			c = (Collection)Ref_c.get();
		}
		{
		}
		if(Ref_i == null){
			Ref_i = new WeakReference(i);
		}
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_next[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final void Prop_1_handler_match (){
		{
			//System.out.println("improper iterator usage");
			CounterClassDiSL.countJoinPoints("UnsafeIterator");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	// RVMRef_c was suppressed to reduce memory overhead
	// RVMRef_i was suppressed to reduce memory overhead

	//alive_parameters_0 = [Collection c, Iterator i]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Iterator i]
	boolean alive_parameters_1 = true;

	@Override
	protected final void terminateInternal(int idnum) {
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
			//alive_c && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//updatesource
			//alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//next
			//alive_c && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	public static int getNumberOfEvents() {
		return 3;
	}

	public static int getNumberOfStates() {
		return 5;
	}

}

public class UnsafeIteratorRuntimeMonitorDiSL implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager UnsafeIteratorMapManager;
	static {
		UnsafeIteratorMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		UnsafeIteratorMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock UnsafeIterator_RVMLock = new ReentrantLock();
	static final Condition UnsafeIterator_RVMLock_cond = UnsafeIterator_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long UnsafeIterator_timestamp = 1;

	private static boolean UnsafeIterator_activated = false;

	// Declarations for Indexing Trees
	private static Object UnsafeIterator_c_Map_cachekey_c;
	private static Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> UnsafeIterator_c_Map_cachevalue;
	private static Object UnsafeIterator_c_i_Map_cachekey_c;
	private static Object UnsafeIterator_c_i_Map_cachekey_i;
	private static UnsafeIteratorMonitorDiSL UnsafeIterator_c_i_Map_cachevalue;
	private static Object UnsafeIterator_i_Map_cachekey_i;
	private static Tuple2<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> UnsafeIterator_i_Map_cachevalue;
	private static final MapOfSetMonitor<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> UnsafeIterator_i_Map = new MapOfSetMonitor<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>(1) ;
	private static final MapOfAll<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> UnsafeIterator_c_i_Map = new MapOfAll<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>(0) ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += UnsafeIterator_i_Map.cleanUpUnnecessaryMappings();
		collected += UnsafeIterator_c_i_Map.cleanUpUnnecessaryMappings();
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

	public static final void createEvent(Collection c, Iterator i) {
		UnsafeIterator_activated = true;
		while (!UnsafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_c = null;
		CachedWeakReference wr_i = null;
		MapOfMonitor<UnsafeIteratorMonitorDiSL> matchedLastMap = null;
		UnsafeIteratorMonitorDiSL matchedEntry = null;
		boolean cachehit = false;
		if (((c == UnsafeIterator_c_i_Map_cachekey_c) && (i == UnsafeIterator_c_i_Map_cachekey_i) ) ) {
			matchedEntry = UnsafeIterator_c_i_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_c = new CachedWeakReference(c) ;
			wr_i = new CachedWeakReference(i) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> node_c = UnsafeIterator_c_i_Map.getNodeEquivalent(wr_c) ;
				if ((node_c == null) ) {
					node_c = new Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>() ;
					UnsafeIterator_c_i_Map.putNode(wr_c, node_c) ;
					node_c.setValue1(new MapOfMonitor<UnsafeIteratorMonitorDiSL>(1) ) ;
					node_c.setValue2(new UnsafeIteratorMonitor_SetDiSL() ) ;
				}
				MapOfMonitor<UnsafeIteratorMonitorDiSL> itmdMap = node_c.getValue1() ;
				matchedLastMap = itmdMap;
				UnsafeIteratorMonitorDiSL node_c_i = node_c.getValue1() .getNodeEquivalent(wr_i) ;
				matchedEntry = node_c_i;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_c == null) ) {
				wr_c = new CachedWeakReference(c) ;
			}
			if ((wr_i == null) ) {
				wr_i = new CachedWeakReference(i) ;
			}
			if ((matchedEntry == null) ) {
				// D(X) main:4
				UnsafeIteratorMonitorDiSL created = new UnsafeIteratorMonitorDiSL(UnsafeIterator_timestamp++) ;
				matchedEntry = created;
				matchedLastMap.putNode(wr_i, created) ;
				// D(X) defineNew:5 for <c>
				{
					// InsertMonitor
					Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> node_c = UnsafeIterator_c_i_Map.getNodeEquivalent(wr_c) ;
					if ((node_c == null) ) {
						node_c = new Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>() ;
						UnsafeIterator_c_i_Map.putNode(wr_c, node_c) ;
						node_c.setValue1(new MapOfMonitor<UnsafeIteratorMonitorDiSL>(1) ) ;
						node_c.setValue2(new UnsafeIteratorMonitor_SetDiSL() ) ;
					}
					UnsafeIteratorMonitor_SetDiSL targetSet = node_c.getValue2() ;
					targetSet.add(created) ;
				}
				// D(X) defineNew:5 for <i>
				{
					// InsertMonitor
					Tuple2<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> node_i = UnsafeIterator_i_Map.getNodeEquivalent(wr_i) ;
					if ((node_i == null) ) {
						node_i = new Tuple2<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>() ;
						UnsafeIterator_i_Map.putNode(wr_i, node_i) ;
						node_i.setValue1(new UnsafeIteratorMonitor_SetDiSL() ) ;
					}
					UnsafeIteratorMonitor_SetDiSL targetSet = node_i.getValue1() ;
					targetSet.add(created) ;
				}
			}
			// D(X) main:6
			matchedEntry.setDisable(UnsafeIterator_timestamp++) ;
		}
		// D(X) main:8--9
		final UnsafeIteratorMonitorDiSL matchedEntryfinalMonitor = matchedEntry;
		matchedEntry.Prop_1_event_create(c, i);
		if(matchedEntryfinalMonitor.Prop_1_Category_match) {
			matchedEntryfinalMonitor.Prop_1_handler_match();
		}

		if ((cachehit == false) ) {
			UnsafeIterator_c_i_Map_cachekey_c = c;
			UnsafeIterator_c_i_Map_cachekey_i = i;
			UnsafeIterator_c_i_Map_cachevalue = matchedEntry;
		}

		UnsafeIterator_RVMLock.unlock();
	}

	public static final void updatesourceEvent(Collection c) {
		UnsafeIterator_activated = true;
		while (!UnsafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_c = null;
		Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if ((c == UnsafeIterator_c_Map_cachekey_c) ) {
			matchedEntry = UnsafeIterator_c_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_c = new CachedWeakReference(c) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> node_c = UnsafeIterator_c_i_Map.getNodeEquivalent(wr_c) ;
				if ((node_c == null) ) {
					node_c = new Tuple3<MapOfMonitor<UnsafeIteratorMonitorDiSL>, UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>() ;
					UnsafeIterator_c_i_Map.putNode(wr_c, node_c) ;
					node_c.setValue1(new MapOfMonitor<UnsafeIteratorMonitorDiSL>(1) ) ;
					node_c.setValue2(new UnsafeIteratorMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_c;
			}
		}
		// D(X) main:1
		UnsafeIteratorMonitorDiSL matchedLeaf = matchedEntry.getValue3() ;
		if ((matchedLeaf == null) ) {
			if ((wr_c == null) ) {
				wr_c = new CachedWeakReference(c) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				UnsafeIteratorMonitorDiSL created = new UnsafeIteratorMonitorDiSL(UnsafeIterator_timestamp++) ;
				matchedEntry.setValue3(created) ;
				UnsafeIteratorMonitor_SetDiSL enclosingSet = matchedEntry.getValue2() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			UnsafeIteratorMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue3() ;
			disableUpdatedLeaf.setDisable(UnsafeIterator_timestamp++) ;
		}
		// D(X) main:8--9
		UnsafeIteratorMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue2() ;
		stateTransitionedSet.event_updatesource(c);

		if ((cachehit == false) ) {
			UnsafeIterator_c_Map_cachekey_c = c;
			UnsafeIterator_c_Map_cachevalue = matchedEntry;
		}

		UnsafeIterator_RVMLock.unlock();
	}

	public static final void nextEvent(Iterator i) {
		UnsafeIterator_activated = true;
		while (!UnsafeIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_i = null;
		Tuple2<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if ((i == UnsafeIterator_i_Map_cachekey_i) ) {
			matchedEntry = UnsafeIterator_i_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_i = new CachedWeakReference(i) ;
			{
				// FindOrCreateEntry
				Tuple2<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL> node_i = UnsafeIterator_i_Map.getNodeEquivalent(wr_i) ;
				if ((node_i == null) ) {
					node_i = new Tuple2<UnsafeIteratorMonitor_SetDiSL, UnsafeIteratorMonitorDiSL>() ;
					UnsafeIterator_i_Map.putNode(wr_i, node_i) ;
					node_i.setValue1(new UnsafeIteratorMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_i;
			}
		}
		// D(X) main:1
		UnsafeIteratorMonitorDiSL matchedLeaf = matchedEntry.getValue2() ;
		if ((matchedLeaf == null) ) {
			if ((wr_i == null) ) {
				wr_i = new CachedWeakReference(i) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				UnsafeIteratorMonitorDiSL created = new UnsafeIteratorMonitorDiSL(UnsafeIterator_timestamp++) ;
				matchedEntry.setValue2(created) ;
				UnsafeIteratorMonitor_SetDiSL enclosingSet = matchedEntry.getValue1() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			UnsafeIteratorMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue2() ;
			disableUpdatedLeaf.setDisable(UnsafeIterator_timestamp++) ;
		}
		// D(X) main:8--9
		UnsafeIteratorMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
		stateTransitionedSet.event_next(i);

		if ((cachehit == false) ) {
			UnsafeIterator_i_Map_cachekey_i = i;
			UnsafeIterator_i_Map_cachevalue = matchedEntry;
		}

		UnsafeIterator_RVMLock.unlock();
	}

}

