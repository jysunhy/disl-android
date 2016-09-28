package ch.usi.dag.javamop.safeenum;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import ch.usi.dag.dislre.AREDispatch;

import com.runtimeverification.rvmonitor.java.rt.RuntimeOption;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfMap;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TerminatedMonitorCleaner;

final class SafeHashSetMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeHashSetMonitor> {

	SafeHashSetMonitor_Set(){
		this.size = 0;
		this.elements = new SafeHashSetMonitor[4];
	}
	final void event_add(final HashSet t, final Object o) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final SafeHashSetMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeHashSetMonitor monitorfinalMonitor = monitor;
				monitor.Prop_1_event_add(t, o);
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
	final void event_unsafe_contains(final HashSet t, final Object o) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final SafeHashSetMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeHashSetMonitor monitorfinalMonitor = monitor;
				monitor.Prop_1_event_unsafe_contains(t, o);
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
	final void event_remove(final HashSet t, final Object o) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final SafeHashSetMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeHashSetMonitor monitorfinalMonitor = monitor;
				monitor.Prop_1_event_remove(t, o);
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

class SafeHashSetMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractAtomicMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	@Override
    protected Object clone() {
		try {
			final SafeHashSetMonitor ret = (SafeHashSetMonitor) super.clone();
			return ret;
		}
		catch (final CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	int hashcode;

	Object involved;

	static final int Prop_1_transition_add[] = {2, 3, 3, 3};;
	static final int Prop_1_transition_unsafe_contains[] = {3, 1, 1, 3};;
	static final int Prop_1_transition_remove[] = {3, 3, 3, 3};;

	volatile boolean Prop_1_Category_match = false;

	private final AtomicInteger pairValue;

	SafeHashSetMonitor() {
		this.pairValue = new AtomicInteger(this.calculatePairValue(-1, 0) ) ;

	}

	@Override public final int getState() {
		return this.getState(this.pairValue.get() ) ;
	}
	@Override public final int getLastEvent() {
		return this.getLastEvent(this.pairValue.get() ) ;
	}
	private final int getState(final int pairValue) {
		return (pairValue & 3) ;
	}
	private final int getLastEvent(final int pairValue) {
		return (pairValue >> 2) ;
	}
	private final int calculatePairValue(final int lastEvent, final int state) {
		return (((lastEvent + 1) << 2) | state) ;
	}

	private final int handleEvent(final int eventId, final int[] table) {
		int nextstate;
		while (true) {
			final int oldpairvalue = this.pairValue.get() ;
			final int oldstate = this.getState(oldpairvalue) ;
			nextstate = table [ oldstate ];
			final int nextpairvalue = this.calculatePairValue(eventId, nextstate) ;
			if (this.pairValue.compareAndSet(oldpairvalue, nextpairvalue) ) {
				break;
			}
		}
		return nextstate;
	}

	final boolean Prop_1_event_add(final HashSet t, final Object o) {
		{
			hashcode = o.hashCode();
			involved = o;
		}

		final int nextstate = this.handleEvent(0, Prop_1_transition_add) ;
		this.Prop_1_Category_match = nextstate == 1;

		return true;
	}

	final boolean Prop_1_event_unsafe_contains(final HashSet t, final Object o) {
		{
			if ( ! (hashcode != o.hashCode()) ) {
				return false;
			}
			{
			}
		}

		final int nextstate = this.handleEvent(1, Prop_1_transition_unsafe_contains) ;
		this.Prop_1_Category_match = nextstate == 1;

		return true;
	}

	final boolean Prop_1_event_remove(final HashSet t, final Object o) {
		{
		}

		final int nextstate = this.handleEvent(2, Prop_1_transition_remove) ;
		this.Prop_1_Category_match = nextstate == 1;

		return true;
	}

	final void Prop_1_handler_match (){
		{
			System.err.println("HashCode changed for Object " + involved + " while being in a   Hashtable!");
			AREDispatch.NativeLog ("HashCode changed for Object " + involved + " while being in a   Hashtable!");
			System.exit(1);
		}

	}

	final void reset() {
		this.pairValue.set(this.calculatePairValue(-1, 0) ) ;

		Prop_1_Category_match = false;
	}

	// RVMRef_t was suppressed to reduce memory overhead
	// RVMRef_o was suppressed to reduce memory overhead

	//alive_parameters_0 = [HashSet t, Object o]
	boolean alive_parameters_0 = true;

	@Override
	protected final void terminateInternal(final int idnum) {
		final int lastEvent = this.getLastEvent();

		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			break;
		}
		switch(lastEvent) {
			case -1:
			return;
			case 0:
			//add
			//alive_t && alive_o
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//unsafe_contains
			//alive_t && alive_o
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//remove
			return;
		}
		return;
	}

	public static int getNumberOfEvents() {
		return 3;
	}

	public static int getNumberOfStates() {
		return 4;
	}

}

public class HashSetRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager HashSetMapManager;
	static {
		HashSetMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		HashSetMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock HashSet_RVMLock = new ReentrantLock();
	static final Condition HashSet_RVMLock_cond = HashSet_RVMLock.newCondition();

	private static boolean SafeHashSet_activated = false;

	// Declarations for Indexing Trees
	private static Object SafeHashSet_t_o_Map_cachekey_o;
	private static Object SafeHashSet_t_o_Map_cachekey_t;
	private static SafeHashSetMonitor SafeHashSet_t_o_Map_cachevalue;
	private static final MapOfMap<MapOfMonitor<SafeHashSetMonitor>> SafeHashSet_t_o_Map = new MapOfMap<MapOfMonitor<SafeHashSetMonitor>>(0) ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += SafeHashSet_t_o_Map.cleanUpUnnecessaryMappings();
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

	public static final void addEvent(final HashSet t, final Object o) {
		SafeHashSet_activated = true;
		while (!HashSet_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_t = null;
		CachedWeakReference wr_o = null;
		MapOfMonitor<SafeHashSetMonitor> matchedLastMap = null;
		SafeHashSetMonitor matchedEntry = null;
		boolean cachehit = false;
		if (((o == SafeHashSet_t_o_Map_cachekey_o) && (t == SafeHashSet_t_o_Map_cachekey_t) ) ) {
			matchedEntry = SafeHashSet_t_o_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_t = new CachedWeakReference(t) ;
			wr_o = new CachedWeakReference(o) ;
			{
				// FindOrCreateEntry
				MapOfMonitor<SafeHashSetMonitor> node_t = SafeHashSet_t_o_Map.getNodeEquivalent(wr_t) ;
				if ((node_t == null) ) {
					node_t = new MapOfMonitor<SafeHashSetMonitor>(1) ;
					SafeHashSet_t_o_Map.putNode(wr_t, node_t) ;
				}
				matchedLastMap = node_t;
				final SafeHashSetMonitor node_t_o = node_t.getNodeEquivalent(wr_o) ;
				matchedEntry = node_t_o;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_t == null) ) {
				wr_t = new CachedWeakReference(t) ;
			}
			if ((wr_o == null) ) {
				wr_o = new CachedWeakReference(o) ;
			}
			// D(X) main:4
			final SafeHashSetMonitor created = new SafeHashSetMonitor() ;
			matchedEntry = created;
			matchedLastMap.putNode(wr_o, created) ;
		}
		// D(X) main:8--9
		final SafeHashSetMonitor matchedEntryfinalMonitor = matchedEntry;
		matchedEntry.Prop_1_event_add(t, o);
		if(matchedEntryfinalMonitor.Prop_1_Category_match) {
			matchedEntryfinalMonitor.Prop_1_handler_match();
		}

		if ((cachehit == false) ) {
			SafeHashSet_t_o_Map_cachekey_o = o;
			SafeHashSet_t_o_Map_cachekey_t = t;
			SafeHashSet_t_o_Map_cachevalue = matchedEntry;
		}

		HashSet_RVMLock.unlock();
	}

	public static final void unsafe_containsEvent(final HashSet t, final Object o) {
		SafeHashSet_activated = true;
		while (!HashSet_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_t = null;
		CachedWeakReference wr_o = null;
		MapOfMonitor<SafeHashSetMonitor> matchedLastMap = null;
		SafeHashSetMonitor matchedEntry = null;
		boolean cachehit = false;
		if (((o == SafeHashSet_t_o_Map_cachekey_o) && (t == SafeHashSet_t_o_Map_cachekey_t) ) ) {
			matchedEntry = SafeHashSet_t_o_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_t = new CachedWeakReference(t) ;
			wr_o = new CachedWeakReference(o) ;
			{
				// FindOrCreateEntry
				MapOfMonitor<SafeHashSetMonitor> node_t = SafeHashSet_t_o_Map.getNodeEquivalent(wr_t) ;
				if ((node_t == null) ) {
					node_t = new MapOfMonitor<SafeHashSetMonitor>(1) ;
					SafeHashSet_t_o_Map.putNode(wr_t, node_t) ;
				}
				matchedLastMap = node_t;
				final SafeHashSetMonitor node_t_o = node_t.getNodeEquivalent(wr_o) ;
				matchedEntry = node_t_o;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_t == null) ) {
				wr_t = new CachedWeakReference(t) ;
			}
			if ((wr_o == null) ) {
				wr_o = new CachedWeakReference(o) ;
			}
			// D(X) main:4
			final SafeHashSetMonitor created = new SafeHashSetMonitor() ;
			matchedEntry = created;
			matchedLastMap.putNode(wr_o, created) ;
		}
		// D(X) main:8--9
		final SafeHashSetMonitor matchedEntryfinalMonitor = matchedEntry;
		matchedEntry.Prop_1_event_unsafe_contains(t, o);
		if(matchedEntryfinalMonitor.Prop_1_Category_match) {
			matchedEntryfinalMonitor.Prop_1_handler_match();
		}

		if ((cachehit == false) ) {
			SafeHashSet_t_o_Map_cachekey_o = o;
			SafeHashSet_t_o_Map_cachekey_t = t;
			SafeHashSet_t_o_Map_cachevalue = matchedEntry;
		}

		HashSet_RVMLock.unlock();
	}

	public static final void removeEvent(final HashSet t, final Object o) {
		SafeHashSet_activated = true;
		while (!HashSet_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_t = null;
		CachedWeakReference wr_o = null;
		MapOfMonitor<SafeHashSetMonitor> matchedLastMap = null;
		SafeHashSetMonitor matchedEntry = null;
		boolean cachehit = false;
		if (((o == SafeHashSet_t_o_Map_cachekey_o) && (t == SafeHashSet_t_o_Map_cachekey_t) ) ) {
			matchedEntry = SafeHashSet_t_o_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_t = new CachedWeakReference(t) ;
			wr_o = new CachedWeakReference(o) ;
			{
				// FindOrCreateEntry
				MapOfMonitor<SafeHashSetMonitor> node_t = SafeHashSet_t_o_Map.getNodeEquivalent(wr_t) ;
				if ((node_t == null) ) {
					node_t = new MapOfMonitor<SafeHashSetMonitor>(1) ;
					SafeHashSet_t_o_Map.putNode(wr_t, node_t) ;
				}
				matchedLastMap = node_t;
				final SafeHashSetMonitor node_t_o = node_t.getNodeEquivalent(wr_o) ;
				matchedEntry = node_t_o;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_t == null) ) {
				wr_t = new CachedWeakReference(t) ;
			}
			if ((wr_o == null) ) {
				wr_o = new CachedWeakReference(o) ;
			}
			// D(X) main:4
			final SafeHashSetMonitor created = new SafeHashSetMonitor() ;
			matchedEntry = created;
			matchedLastMap.putNode(wr_o, created) ;
		}
		// D(X) main:8--9
		final SafeHashSetMonitor matchedEntryfinalMonitor = matchedEntry;
		matchedEntry.Prop_1_event_remove(t, o);
		if(matchedEntryfinalMonitor.Prop_1_Category_match) {
			matchedEntryfinalMonitor.Prop_1_handler_match();
		}

		if ((cachehit == false) ) {
			SafeHashSet_t_o_Map_cachekey_o = o;
			SafeHashSet_t_o_Map_cachekey_t = t;
			SafeHashSet_t_o_Map_cachevalue = matchedEntry;
		}

		HashSet_RVMLock.unlock();
	}

}
