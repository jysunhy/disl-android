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

final class UnsafeMapIteratorMonitor_SetDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<UnsafeMapIteratorMonitorDiSL> {

	UnsafeMapIteratorMonitor_SetDiSL(){
		this.size = 0;
		this.elements = new UnsafeMapIteratorMonitorDiSL[4];
	}
	final void event_createColl(Map map, Collection c) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeMapIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_createColl(map, c);
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
	final void event_createIter(Collection c, Iterator i) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeMapIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_createIter(c, i);
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
	final void event_useIter(Iterator i) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeMapIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_useIter(i);
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
	final void event_updateMap(Map map) {
		int numAlive = 0 ;
		for(int i_1 = 0; i_1 < this.size; i_1++){
			UnsafeMapIteratorMonitorDiSL monitor = this.elements[i_1];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final UnsafeMapIteratorMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_updateMap(map);
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

interface IUnsafeMapIteratorMonitorDiSL extends IMonitor, IDisableHolder {
}

class UnsafeMapIteratorDisableHolderDiSL extends DisableHolder implements IUnsafeMapIteratorMonitorDiSL {
	UnsafeMapIteratorDisableHolderDiSL(long tau) {
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

class UnsafeMapIteratorMonitorDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject, IUnsafeMapIteratorMonitorDiSL {
	protected Object clone() {
		try {
			UnsafeMapIteratorMonitorDiSL ret = (UnsafeMapIteratorMonitorDiSL) super.clone();
			ret.monitorInfo = (com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo)this.monitorInfo.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	WeakReference Ref_c = null;
	WeakReference Ref_i = null;
	WeakReference Ref_map = null;
	int Prop_1_state;
	static final int Prop_1_transition_createColl[] = {2, 5, 5, 5, 5, 5};;
	static final int Prop_1_transition_createIter[] = {5, 5, 1, 5, 5, 5};;
	static final int Prop_1_transition_useIter[] = {5, 1, 5, 4, 5, 5};;
	static final int Prop_1_transition_updateMap[] = {5, 3, 2, 3, 5, 5};;

	boolean Prop_1_Category_match = false;

	UnsafeMapIteratorMonitorDiSL(long tau, CachedWeakReference RVMRef_map, CachedWeakReference RVMRef_c, CachedWeakReference RVMRef_i) {
		this.tau = tau;
		Prop_1_state = 0;

		this.RVMRef_map = RVMRef_map;
		this.RVMRef_c = RVMRef_c;
		this.RVMRef_i = RVMRef_i;
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

	final boolean Prop_1_event_createColl(Map map, Collection c) {
		Iterator i = null;
		if(Ref_i != null){
			i = (Iterator)Ref_i.get();
		}
		{
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		if(Ref_map == null){
			Ref_map = new WeakReference(map);
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_createColl[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 4;
		}
		return true;
	}

	final boolean Prop_1_event_createIter(Collection c, Iterator i) {
		Map map = null;
		if(Ref_map != null){
			map = (Map)Ref_map.get();
		}
		{
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		if(Ref_i == null){
			Ref_i = new WeakReference(i);
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_createIter[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 4;
		}
		return true;
	}

	final boolean Prop_1_event_useIter(Iterator i) {
		Map map = null;
		if(Ref_map != null){
			map = (Map)Ref_map.get();
		}
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

		Prop_1_state = Prop_1_transition_useIter[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 4;
		}
		return true;
	}

	final boolean Prop_1_event_updateMap(Map map) {
		Collection c = null;
		if(Ref_c != null){
			c = (Collection)Ref_c.get();
		}
		Iterator i = null;
		if(Ref_i != null){
			i = (Iterator)Ref_i.get();
		}
		{
		}
		if(Ref_map == null){
			Ref_map = new WeakReference(map);
		}
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_updateMap[Prop_1_state];
		if(this.monitorInfo.isFullParam){
			Prop_1_Category_match = Prop_1_state == 4;
		}
		return true;
	}

	final void Prop_1_handler_match (){
		{
			CounterClassDiSL.countJoinPoints("UnsafeMapIterator");
			//System.out.println("unsafe iterator usage!");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	CachedWeakReference RVMRef_map;
	CachedWeakReference RVMRef_c;
	CachedWeakReference RVMRef_i;

	//alive_parameters_0 = [Map map, Collection c, Iterator i]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Map map, Iterator i]
	boolean alive_parameters_1 = true;
	//alive_parameters_2 = [Iterator i]
	boolean alive_parameters_2 = true;

	@Override
	protected final void terminateInternal(int idnum) {
		switch(idnum){
			case 0:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			break;
			case 1:
			alive_parameters_0 = false;
			break;
			case 2:
			alive_parameters_0 = false;
			alive_parameters_1 = false;
			alive_parameters_2 = false;
			break;
		}
		switch(RVM_lastevent) {
			case -1:
			return;
			case 0:
			//createColl
			//alive_map && alive_c && alive_i
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//createIter
			//alive_map && alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//useIter
			//alive_map && alive_i
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 3:
			//updateMap
			//alive_i
			if(!(alive_parameters_2)){
				RVM_terminated = true;
				return;
			}
			break;

		}
		return;
	}

	com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo monitorInfo;
	public static int getNumberOfEvents() {
		return 4;
	}

	public static int getNumberOfStates() {
		return 6;
	}

}

public class UnsafeMapIteratorRuntimeMonitorDiSL implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager UnsafeMapIteratorMapManager;
	static {
		UnsafeMapIteratorMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		UnsafeMapIteratorMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock UnsafeMapIterator_RVMLock = new ReentrantLock();
	static final Condition UnsafeMapIterator_RVMLock_cond = UnsafeMapIterator_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long UnsafeMapIterator_timestamp = 1;

	private static boolean UnsafeMapIterator_activated = false;

	// Declarations for Indexing Trees
	private static Object UnsafeMapIterator_c_i_Map_cachekey_c;
	private static Object UnsafeMapIterator_c_i_Map_cachekey_i;
	private static Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_c_i_Map_cachevalue;
	private static Object UnsafeMapIterator_i_Map_cachekey_i;
	private static Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_i_Map_cachevalue;
	private static Object UnsafeMapIterator_map_Map_cachekey_map;
	private static Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_map_Map_cachevalue;
	private static Object UnsafeMapIterator_map_c_Map_cachekey_c;
	private static Object UnsafeMapIterator_map_c_Map_cachekey_map;
	private static Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_map_c_Map_cachevalue;
	private static Object UnsafeMapIterator_map_c_i_Map_cachekey_c;
	private static Object UnsafeMapIterator_map_c_i_Map_cachekey_i;
	private static Object UnsafeMapIterator_map_c_i_Map_cachekey_map;
	private static IUnsafeMapIteratorMonitorDiSL UnsafeMapIterator_map_c_i_Map_cachevalue;
	private static final MapOfAll<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_map_c_i_Map = new MapOfAll<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(0) ;
	private static final MapOfMap<MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>> UnsafeMapIterator_c_i_Map = new MapOfMap<MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>>(1) ;
	private static final MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_i_Map = new MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(2) ;
	private static Object UnsafeMapIterator_c__To__map_c_Map_cachekey_c;
	private static Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_c__To__map_c_Map_cachevalue;
	private static final MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> UnsafeMapIterator_c__To__map_c_Map = new MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += UnsafeMapIterator_map_c_i_Map.cleanUpUnnecessaryMappings();
		collected += UnsafeMapIterator_c_i_Map.cleanUpUnnecessaryMappings();
		collected += UnsafeMapIterator_i_Map.cleanUpUnnecessaryMappings();
		collected += UnsafeMapIterator_c__To__map_c_Map.cleanUpUnnecessaryMappings();
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

	public static final void createCollEvent(Map map, Collection c) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_c = null;
		CachedWeakReference wr_map = null;
		Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if (((c == UnsafeMapIterator_map_c_Map_cachekey_c) && (map == UnsafeMapIterator_map_c_Map_cachekey_map) ) ) {
			matchedEntry = UnsafeMapIterator_map_c_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_map = new CachedWeakReference(map) ;
			wr_c = new CachedWeakReference(c) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
				if ((node_map == null) ) {
					node_map = new Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
					UnsafeMapIterator_map_c_i_Map.putNode(wr_map, node_map) ;
					node_map.setValue1(new MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ) ;
					node_map.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
				}
				Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map_c = node_map.getValue1() .getNodeEquivalent(wr_c) ;
				if ((node_map_c == null) ) {
					node_map_c = new Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
					node_map.getValue1() .putNode(wr_c, node_map_c) ;
					node_map_c.setValue1(new MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>(2) ) ;
					node_map_c.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_map_c;
			}
		}
		// D(X) main:1
		UnsafeMapIteratorMonitorDiSL matchedLeaf = matchedEntry.getValue3() ;
		if ((matchedLeaf == null) ) {
			if ((wr_map == null) ) {
				wr_map = new CachedWeakReference(map) ;
			}
			if ((wr_c == null) ) {
				wr_c = new CachedWeakReference(c) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				UnsafeMapIteratorMonitorDiSL created = new UnsafeMapIteratorMonitorDiSL(UnsafeMapIterator_timestamp++, wr_map, wr_c, null) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = false;

				matchedEntry.setValue3(created) ;
				UnsafeMapIteratorMonitor_SetDiSL enclosingSet = matchedEntry.getValue2() ;
				enclosingSet.add(created) ;
				// D(X) defineNew:5 for <map>
				{
					// InsertMonitor
					Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
					if ((node_map == null) ) {
						node_map = new Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
						UnsafeMapIterator_map_c_i_Map.putNode(wr_map, node_map) ;
						node_map.setValue1(new MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ) ;
						node_map.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
					}
					UnsafeMapIteratorMonitor_SetDiSL targetSet = node_map.getValue2() ;
					targetSet.add(created) ;
				}
				// D(X) defineNew:5 for <c-map, c>
				{
					// InsertMonitor
					Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c = UnsafeMapIterator_c__To__map_c_Map.getNodeEquivalent(wr_c) ;
					if ((node_c == null) ) {
						node_c = new Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
						UnsafeMapIterator_c__To__map_c_Map.putNode(wr_c, node_c) ;
						node_c.setValue1(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
					}
					UnsafeMapIteratorMonitor_SetDiSL targetSet = node_c.getValue1() ;
					targetSet.add(created) ;
				}
			}
			// D(X) main:6
			UnsafeMapIteratorMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue3() ;
			disableUpdatedLeaf.setDisable(UnsafeMapIterator_timestamp++) ;
		}
		// D(X) main:8--9
		UnsafeMapIteratorMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue2() ;
		stateTransitionedSet.event_createColl(map, c);

		if ((cachehit == false) ) {
			UnsafeMapIterator_map_c_Map_cachekey_c = c;
			UnsafeMapIterator_map_c_Map_cachekey_map = map;
			UnsafeMapIterator_map_c_Map_cachevalue = matchedEntry;
		}

		UnsafeMapIterator_RVMLock.unlock();
	}

	public static final void createIterEvent(Collection c, Iterator i) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_c = null;
		CachedWeakReference wr_i = null;
		Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if (((c == UnsafeMapIterator_c_i_Map_cachekey_c) && (i == UnsafeMapIterator_c_i_Map_cachekey_i) ) ) {
			matchedEntry = UnsafeMapIterator_c_i_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_c = new CachedWeakReference(c) ;
			wr_i = new CachedWeakReference(i) ;
			{
				// FindOrCreateEntry
				MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c = UnsafeMapIterator_c_i_Map.getNodeEquivalent(wr_c) ;
				if ((node_c == null) ) {
					node_c = new MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ;
					UnsafeMapIterator_c_i_Map.putNode(wr_c, node_c) ;
				}
				Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c_i = node_c.getNodeEquivalent(wr_i) ;
				if ((node_c_i == null) ) {
					node_c_i = new Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
					node_c.putNode(wr_i, node_c_i) ;
					node_c_i.setValue1(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_c_i;
			}
		}
		// D(X) main:1
		UnsafeMapIteratorMonitorDiSL matchedLeaf = matchedEntry.getValue2() ;
		if ((matchedLeaf == null) ) {
			if ((wr_c == null) ) {
				wr_c = new CachedWeakReference(c) ;
			}
			if ((wr_i == null) ) {
				wr_i = new CachedWeakReference(i) ;
			}
			{
				// D(X) createNewMonitorStates:4 when Dom(theta'') = <c>
				UnsafeMapIteratorMonitor_SetDiSL sourceSet = null;
				{
					// FindCode
					Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c = UnsafeMapIterator_c__To__map_c_Map.getNodeEquivalent(wr_c) ;
					if ((node_c != null) ) {
						UnsafeMapIteratorMonitor_SetDiSL itmdSet = node_c.getValue1() ;
						sourceSet = itmdSet;
					}
				}
				if ((sourceSet != null) ) {
					int numalive = 0;
					int setlen = sourceSet.getSize() ;
					for (int ielem = 0; (ielem < setlen) ;++ielem) {
						UnsafeMapIteratorMonitorDiSL sourceMonitor = sourceSet.get(ielem) ;
						if ((!sourceMonitor.isTerminated() && (sourceMonitor.RVMRef_map.get() != null) ) ) {
							sourceSet.set(numalive++, sourceMonitor) ;
							CachedWeakReference wr_map = sourceMonitor.RVMRef_map;
							MapOfMonitor<IUnsafeMapIteratorMonitorDiSL> destLastMap = null;
							IUnsafeMapIteratorMonitorDiSL destLeaf = null;
							{
								// FindOrCreate
								Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
								if ((node_map == null) ) {
									node_map = new Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
									UnsafeMapIterator_map_c_i_Map.putNode(wr_map, node_map) ;
									node_map.setValue1(new MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ) ;
									node_map.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
								}
								Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map_c = node_map.getValue1() .getNodeEquivalent(wr_c) ;
								if ((node_map_c == null) ) {
									node_map_c = new Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
									node_map.getValue1() .putNode(wr_c, node_map_c) ;
									node_map_c.setValue1(new MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>(2) ) ;
									node_map_c.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
								}
								MapOfMonitor<IUnsafeMapIteratorMonitorDiSL> itmdMap = node_map_c.getValue1() ;
								destLastMap = itmdMap;
								IUnsafeMapIteratorMonitorDiSL node_map_c_i = node_map_c.getValue1() .getNodeEquivalent(wr_i) ;
								destLeaf = node_map_c_i;
							}
							if (((destLeaf == null) || destLeaf instanceof UnsafeMapIteratorDisableHolderDiSL) ) {
								boolean definable = true;
								// D(X) defineTo:1--5 for <c, i>
								if (definable) {
									// FindCode
									MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c = UnsafeMapIterator_c_i_Map.getNodeEquivalent(wr_c) ;
									if ((node_c != null) ) {
										Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c_i = node_c.getNodeEquivalent(wr_i) ;
										if ((node_c_i != null) ) {
											UnsafeMapIteratorMonitorDiSL itmdLeaf = node_c_i.getValue2() ;
											if ((itmdLeaf != null) ) {
												if (((itmdLeaf.getDisable() > sourceMonitor.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceMonitor.getTau() ) ) ) ) {
													definable = false;
												}
											}
										}
									}
								}
								// D(X) defineTo:1--5 for <i>
								if (definable) {
									// FindCode
									Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_i = UnsafeMapIterator_i_Map.getNodeEquivalent(wr_i) ;
									if ((node_i != null) ) {
										UnsafeMapIteratorMonitorDiSL itmdLeaf = node_i.getValue2() ;
										if ((itmdLeaf != null) ) {
											if (((itmdLeaf.getDisable() > sourceMonitor.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceMonitor.getTau() ) ) ) ) {
												definable = false;
											}
										}
									}
								}
								// D(X) defineTo:1--5 for <map, c, i>
								if (definable) {
									// FindCode
									Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
									if ((node_map != null) ) {
										Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map_c = node_map.getValue1() .getNodeEquivalent(wr_c) ;
										if ((node_map_c != null) ) {
											IUnsafeMapIteratorMonitorDiSL node_map_c_i = node_map_c.getValue1() .getNodeEquivalent(wr_i) ;
											if ((node_map_c_i != null) ) {
												if (((node_map_c_i.getDisable() > sourceMonitor.getTau() ) || ((node_map_c_i.getTau() > 0) && (node_map_c_i.getTau() < sourceMonitor.getTau() ) ) ) ) {
													definable = false;
												}
											}
										}
									}
								}
								if (definable) {
									// D(X) defineTo:6
									UnsafeMapIteratorMonitorDiSL created = (UnsafeMapIteratorMonitorDiSL)sourceMonitor.clone() ;
									created.RVMRef_i = wr_i;
									created.monitorInfo.isFullParam = true;

									destLastMap.putNode(wr_i, created) ;
									// D(X) defineTo:7 for <c, i>
									{
										// InsertMonitor
										MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c = UnsafeMapIterator_c_i_Map.getNodeEquivalent(wr_c) ;
										if ((node_c == null) ) {
											node_c = new MapOfSetMonitor<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ;
											UnsafeMapIterator_c_i_Map.putNode(wr_c, node_c) ;
										}
										Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_c_i = node_c.getNodeEquivalent(wr_i) ;
										if ((node_c_i == null) ) {
											node_c_i = new Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
											node_c.putNode(wr_i, node_c_i) ;
											node_c_i.setValue1(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
										}
										UnsafeMapIteratorMonitor_SetDiSL targetSet = node_c_i.getValue1() ;
										targetSet.add(created) ;
									}
									// D(X) defineTo:7 for <i>
									{
										// InsertMonitor
										Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_i = UnsafeMapIterator_i_Map.getNodeEquivalent(wr_i) ;
										if ((node_i == null) ) {
											node_i = new Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
											UnsafeMapIterator_i_Map.putNode(wr_i, node_i) ;
											node_i.setValue1(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
										}
										UnsafeMapIteratorMonitor_SetDiSL targetSet = node_i.getValue1() ;
										targetSet.add(created) ;
									}
									// D(X) defineTo:7 for <map>
									{
										// InsertMonitor
										Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
										if ((node_map == null) ) {
											node_map = new Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
											UnsafeMapIterator_map_c_i_Map.putNode(wr_map, node_map) ;
											node_map.setValue1(new MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ) ;
											node_map.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
										}
										UnsafeMapIteratorMonitor_SetDiSL targetSet = node_map.getValue2() ;
										targetSet.add(created) ;
									}
									// D(X) defineTo:7 for <map, c>
									{
										// InsertMonitor
										Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
										if ((node_map == null) ) {
											node_map = new Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
											UnsafeMapIterator_map_c_i_Map.putNode(wr_map, node_map) ;
											node_map.setValue1(new MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ) ;
											node_map.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
										}
										Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map_c = node_map.getValue1() .getNodeEquivalent(wr_c) ;
										if ((node_map_c == null) ) {
											node_map_c = new Tuple3<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
											node_map.getValue1() .putNode(wr_c, node_map_c) ;
											node_map_c.setValue1(new MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>(2) ) ;
											node_map_c.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
										}
										UnsafeMapIteratorMonitor_SetDiSL targetSet = node_map_c.getValue2() ;
										targetSet.add(created) ;
									}
								}
							}
						}
					}
					sourceSet.eraseRange(numalive) ;
				}
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				UnsafeMapIteratorMonitorDiSL created = new UnsafeMapIteratorMonitorDiSL(UnsafeMapIterator_timestamp++, null, wr_c, wr_i) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = false;

				matchedEntry.setValue2(created) ;
				UnsafeMapIteratorMonitor_SetDiSL enclosingSet = matchedEntry.getValue1() ;
				enclosingSet.add(created) ;
				// D(X) defineNew:5 for <i>
				{
					// InsertMonitor
					Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_i = UnsafeMapIterator_i_Map.getNodeEquivalent(wr_i) ;
					if ((node_i == null) ) {
						node_i = new Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
						UnsafeMapIterator_i_Map.putNode(wr_i, node_i) ;
						node_i.setValue1(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
					}
					UnsafeMapIteratorMonitor_SetDiSL targetSet = node_i.getValue1() ;
					targetSet.add(created) ;
				}
			}
			// D(X) main:6
			UnsafeMapIteratorMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue2() ;
			disableUpdatedLeaf.setDisable(UnsafeMapIterator_timestamp++) ;
		}
		// D(X) main:8--9
		UnsafeMapIteratorMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
		stateTransitionedSet.event_createIter(c, i);

		if ((cachehit == false) ) {
			UnsafeMapIterator_c_i_Map_cachekey_c = c;
			UnsafeMapIterator_c_i_Map_cachekey_i = i;
			UnsafeMapIterator_c_i_Map_cachevalue = matchedEntry;
		}

		UnsafeMapIterator_RVMLock.unlock();
	}

	public static final void useIterEvent(Iterator i) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_i = null;
		Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if ((i == UnsafeMapIterator_i_Map_cachekey_i) ) {
			matchedEntry = UnsafeMapIterator_i_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_i = new CachedWeakReference(i) ;
			{
				// FindOrCreateEntry
				Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_i = UnsafeMapIterator_i_Map.getNodeEquivalent(wr_i) ;
				if ((node_i == null) ) {
					node_i = new Tuple2<UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
					UnsafeMapIterator_i_Map.putNode(wr_i, node_i) ;
					node_i.setValue1(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_i;
			}
		}
		// D(X) main:1
		UnsafeMapIteratorMonitorDiSL matchedLeaf = matchedEntry.getValue2() ;
		if ((matchedLeaf == null) ) {
			if ((wr_i == null) ) {
				wr_i = new CachedWeakReference(i) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				UnsafeMapIteratorMonitorDiSL created = new UnsafeMapIteratorMonitorDiSL(UnsafeMapIterator_timestamp++, null, null, wr_i) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = false;

				matchedEntry.setValue2(created) ;
				UnsafeMapIteratorMonitor_SetDiSL enclosingSet = matchedEntry.getValue1() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			UnsafeMapIteratorMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue2() ;
			disableUpdatedLeaf.setDisable(UnsafeMapIterator_timestamp++) ;
		}
		// D(X) main:8--9
		UnsafeMapIteratorMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
		stateTransitionedSet.event_useIter(i);

		if ((cachehit == false) ) {
			UnsafeMapIterator_i_Map_cachekey_i = i;
			UnsafeMapIterator_i_Map_cachevalue = matchedEntry;
		}

		UnsafeMapIterator_RVMLock.unlock();
	}

	public static final void updateMapEvent(Map map) {
		UnsafeMapIterator_activated = true;
		while (!UnsafeMapIterator_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_map = null;
		Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if ((map == UnsafeMapIterator_map_Map_cachekey_map) ) {
			matchedEntry = UnsafeMapIterator_map_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_map = new CachedWeakReference(map) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL> node_map = UnsafeMapIterator_map_c_i_Map.getNodeEquivalent(wr_map) ;
				if ((node_map == null) ) {
					node_map = new Tuple3<MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>() ;
					UnsafeMapIterator_map_c_i_Map.putNode(wr_map, node_map) ;
					node_map.setValue1(new MapOfAll<MapOfMonitor<IUnsafeMapIteratorMonitorDiSL>, UnsafeMapIteratorMonitor_SetDiSL, UnsafeMapIteratorMonitorDiSL>(1) ) ;
					node_map.setValue2(new UnsafeMapIteratorMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_map;
			}
		}
		// D(X) main:1
		UnsafeMapIteratorMonitorDiSL matchedLeaf = matchedEntry.getValue3() ;
		if ((matchedLeaf == null) ) {
			if ((wr_map == null) ) {
				wr_map = new CachedWeakReference(map) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				UnsafeMapIteratorMonitorDiSL created = new UnsafeMapIteratorMonitorDiSL(UnsafeMapIterator_timestamp++, wr_map, null, null) ;
				created.monitorInfo = new com.runtimeverification.rvmonitor.java.rt.RVMMonitorInfo();
				created.monitorInfo.isFullParam = false;

				matchedEntry.setValue3(created) ;
				UnsafeMapIteratorMonitor_SetDiSL enclosingSet = matchedEntry.getValue2() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			UnsafeMapIteratorMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue3() ;
			disableUpdatedLeaf.setDisable(UnsafeMapIterator_timestamp++) ;
		}
		// D(X) main:8--9
		UnsafeMapIteratorMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue2() ;
		stateTransitionedSet.event_updateMap(map);

		if ((cachehit == false) ) {
			UnsafeMapIterator_map_Map_cachekey_map = map;
			UnsafeMapIterator_map_Map_cachevalue = matchedEntry;
		}

		UnsafeMapIterator_RVMLock.unlock();
	}

}
