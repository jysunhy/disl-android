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


final class SafeSyncMapMonitor_SetDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeSyncMapMonitorDiSL> {

	SafeSyncMapMonitor_SetDiSL(){
		this.size = 0;
		this.elements = new SafeSyncMapMonitorDiSL[4];
	}
	final void event_sync(Map syncMap) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncMapMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_sync(syncMap);
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
	final void event_createSet(Map syncMap, Set mapSet) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncMapMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_createSet(syncMap, mapSet);
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
	final void event_syncCreateIter(Set mapSet, Iterator iter) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncMapMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_syncCreateIter(mapSet, iter);
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
	final void event_asyncCreateIter(Set mapSet, Iterator iter) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncMapMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_asyncCreateIter(mapSet, iter);
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
	final void event_accessIter(Iterator iter) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncMapMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncMapMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_accessIter(iter);
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

interface ISafeSyncMapMonitor extends IMonitor, IDisableHolder {
}

class SafeSyncMapDisableHolderDiSL extends DisableHolder implements ISafeSyncMapMonitor {
	SafeSyncMapDisableHolderDiSL(long tau) {
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

class SafeSyncMapMonitorDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject, ISafeSyncMapMonitor {
	protected Object clone() {
		try {
			SafeSyncMapMonitorDiSL ret = (SafeSyncMapMonitorDiSL) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	Map c;

	WeakReference Ref_syncMap = null;
	WeakReference Ref_iter = null;
	WeakReference Ref_mapSet = null;
	int Prop_1_state;
	static final int Prop_1_transition_sync[] = {2, 5, 5, 5, 5, 5};;
	static final int Prop_1_transition_createSet[] = {5, 5, 3, 5, 5, 5};;
	static final int Prop_1_transition_syncCreateIter[] = {5, 5, 5, 1, 5, 5};;
	static final int Prop_1_transition_asyncCreateIter[] = {5, 5, 5, 4, 5, 5};;
	static final int Prop_1_transition_accessIter[] = {5, 4, 5, 5, 5, 5};;

	boolean Prop_1_Category_match = false;

	SafeSyncMapMonitorDiSL(long tau, CachedWeakReference RVMRef_syncMap) {
		this.tau = tau;
		Prop_1_state = 0;

		this.RVMRef_syncMap = RVMRef_syncMap;
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

	final boolean Prop_1_event_sync(Map syncMap) {
		Set mapSet = null;
		if(Ref_mapSet != null){
			mapSet = (Set)Ref_mapSet.get();
		}
		Iterator iter = null;
		if(Ref_iter != null){
			iter = (Iterator)Ref_iter.get();
		}
		{
			this.c = syncMap;
		}
		if(Ref_syncMap == null){
			Ref_syncMap = new WeakReference(syncMap);
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_sync[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 4;
		return true;
	}

	final boolean Prop_1_event_createSet(Map syncMap, Set mapSet) {
		Iterator iter = null;
		if(Ref_iter != null){
			iter = (Iterator)Ref_iter.get();
		}
		{
		}
		if(Ref_syncMap == null){
			Ref_syncMap = new WeakReference(syncMap);
		}
		if(Ref_mapSet == null){
			Ref_mapSet = new WeakReference(mapSet);
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_createSet[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 4;
		return true;
	}

	final boolean Prop_1_event_syncCreateIter(Set mapSet, Iterator iter) {
		Map syncMap = null;
		if(Ref_syncMap != null){
			syncMap = (Map)Ref_syncMap.get();
		}
		{
			if ( ! (Thread.holdsLock(c)) ) {
				return false;
			}
			{
			}
		}
		if(Ref_iter == null){
			Ref_iter = new WeakReference(iter);
		}
		if(Ref_mapSet == null){
			Ref_mapSet = new WeakReference(mapSet);
		}
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_syncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 4;
		return true;
	}

	final boolean Prop_1_event_asyncCreateIter(Set mapSet, Iterator iter) {
		Map syncMap = null;
		if(Ref_syncMap != null){
			syncMap = (Map)Ref_syncMap.get();
		}
		{
			if ( ! (!Thread.holdsLock(c)) ) {
				return false;
			}
			{
			}
		}
		if(Ref_iter == null){
			Ref_iter = new WeakReference(iter);
		}
		if(Ref_mapSet == null){
			Ref_mapSet = new WeakReference(mapSet);
		}
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_asyncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 4;
		return true;
	}

	final boolean Prop_1_event_accessIter(Iterator iter) {
		Map syncMap = null;
		if(Ref_syncMap != null){
			syncMap = (Map)Ref_syncMap.get();
		}
		Set mapSet = null;
		if(Ref_mapSet != null){
			mapSet = (Set)Ref_mapSet.get();
		}
		{
			if ( ! (!Thread.holdsLock(c)) ) {
				return false;
			}
			{
			}
		}
		if(Ref_iter == null){
			Ref_iter = new WeakReference(iter);
		}
		RVM_lastevent = 4;

		Prop_1_state = Prop_1_transition_accessIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 4;
		return true;
	}

	final void Prop_1_handler_match (){
		{
			//System.out.println("synchronized collection accessed in non threadsafe manner!");
			CounterClassDiSL.countJoinPoints("SafeSyncMap");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	final CachedWeakReference RVMRef_syncMap;
	// RVMRef_mapSet was suppressed to reduce memory overhead
	// RVMRef_iter was suppressed to reduce memory overhead

	//alive_parameters_0 = [Map syncMap, Set mapSet, Iterator iter]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Set mapSet, Iterator iter]
	boolean alive_parameters_1 = true;
	//alive_parameters_2 = [Iterator iter]
	boolean alive_parameters_2 = true;

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
			//sync
			//alive_syncMap && alive_mapSet && alive_iter
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//createSet
			//alive_mapSet && alive_iter
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//syncCreateIter
			//alive_iter
			if(!(alive_parameters_2)){
				RVM_terminated = true;
				return;
			}
			break;

			case 3:
			//asyncCreateIter
			return;
			case 4:
			//accessIter
			return;
		}
		return;
	}

	public static int getNumberOfEvents() {
		return 5;
	}

	public static int getNumberOfStates() {
		return 6;
	}

}

public class SafeSyncMapRuntimeMonitorDiSL implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeSyncMapMapManager;
	static {
		SafeSyncMapMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeSyncMapMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock SafeSyncMap_RVMLock = new ReentrantLock();
	static final Condition SafeSyncMap_RVMLock_cond = SafeSyncMap_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeSyncMap_timestamp = 1;

	private static boolean SafeSyncMap_activated = false;

	// Declarations for Indexing Trees
	private static Object SafeSyncMap_iter_Map_cachekey_iter;
	private static Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> SafeSyncMap_iter_Map_cachevalue;
	private static Object SafeSyncMap_mapSet_iter_Map_cachekey_iter;
	private static Object SafeSyncMap_mapSet_iter_Map_cachekey_mapSet;
	private static Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> SafeSyncMap_mapSet_iter_Map_cachevalue;
	private static Object SafeSyncMap_syncMap_Map_cachekey_syncMap;
	private static Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> SafeSyncMap_syncMap_Map_cachevalue;
	private static Object SafeSyncMap_syncMap_mapSet_Map_cachekey_mapSet;
	private static Object SafeSyncMap_syncMap_mapSet_Map_cachekey_syncMap;
	private static Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> SafeSyncMap_syncMap_mapSet_Map_cachevalue;
	private static Object SafeSyncMap_syncMap_mapSet_iter_Map_cachekey_iter;
	private static Object SafeSyncMap_syncMap_mapSet_iter_Map_cachekey_mapSet;
	private static Object SafeSyncMap_syncMap_mapSet_iter_Map_cachekey_syncMap;
	private static ISafeSyncMapMonitor SafeSyncMap_syncMap_mapSet_iter_Map_cachevalue;
	private static final MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> SafeSyncMap_iter_Map = new MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(2) ;
	private static final MapOfMap<MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>> SafeSyncMap_mapSet_iter_Map = new MapOfMap<MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>>(1) ;
	private static final MapOfAll<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> SafeSyncMap_syncMap_mapSet_iter_Map = new MapOfAll<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>(0) ;
	private static Object SafeSyncMap_mapSet__To__syncMap_mapSet_Map_cachekey_mapSet;
	private static Tuple2<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> SafeSyncMap_mapSet__To__syncMap_mapSet_Map_cachevalue;
	private static final MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> SafeSyncMap_mapSet__To__syncMap_mapSet_Map = new MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>(1) ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += SafeSyncMap_iter_Map.cleanUpUnnecessaryMappings();
		collected += SafeSyncMap_mapSet_iter_Map.cleanUpUnnecessaryMappings();
		collected += SafeSyncMap_syncMap_mapSet_iter_Map.cleanUpUnnecessaryMappings();
		collected += SafeSyncMap_mapSet__To__syncMap_mapSet_Map.cleanUpUnnecessaryMappings();
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

	public static final void syncEvent(Map syncMap) {
		SafeSyncMap_activated = true;
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_syncMap = null;
		Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if ((syncMap == SafeSyncMap_syncMap_Map_cachekey_syncMap) ) {
			matchedEntry = SafeSyncMap_syncMap_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_syncMap = new CachedWeakReference(syncMap) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
				if ((node_syncMap == null) ) {
					node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
					SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
					node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
					node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_syncMap;
			}
		}
		// D(X) main:1
		SafeSyncMapMonitorDiSL matchedLeaf = matchedEntry.getValue3() ;
		if ((matchedLeaf == null) ) {
			if ((wr_syncMap == null) ) {
				wr_syncMap = new CachedWeakReference(syncMap) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				SafeSyncMapMonitorDiSL created = new SafeSyncMapMonitorDiSL(SafeSyncMap_timestamp++, wr_syncMap) ;
				matchedEntry.setValue3(created) ;
				SafeSyncMapMonitor_SetDiSL enclosingSet = matchedEntry.getValue2() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			SafeSyncMapMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue3() ;
			disableUpdatedLeaf.setDisable(SafeSyncMap_timestamp++) ;
		}
		// D(X) main:8--9
		SafeSyncMapMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue2() ;
		stateTransitionedSet.event_sync(syncMap);

		if ((cachehit == false) ) {
			SafeSyncMap_syncMap_Map_cachekey_syncMap = syncMap;
			SafeSyncMap_syncMap_Map_cachevalue = matchedEntry;
		}

		SafeSyncMap_RVMLock.unlock();
	}

	public static final void createSetEvent(Map syncMap, Set mapSet) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncMap_activated) {
			CachedWeakReference wr_syncMap = null;
			CachedWeakReference wr_mapSet = null;
			Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> matchedEntry = null;
			boolean cachehit = false;
			if (((mapSet == SafeSyncMap_syncMap_mapSet_Map_cachekey_mapSet) && (syncMap == SafeSyncMap_syncMap_mapSet_Map_cachekey_syncMap) ) ) {
				matchedEntry = SafeSyncMap_syncMap_mapSet_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_syncMap = new CachedWeakReference(syncMap) ;
				wr_mapSet = new CachedWeakReference(mapSet) ;
				{
					// FindOrCreateEntry
					Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
					if ((node_syncMap == null) ) {
						node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
						SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
						node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
						node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
					}
					Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
					if ((node_syncMap_mapSet == null) ) {
						node_syncMap_mapSet = new Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
						node_syncMap.getValue1() .putNode(wr_mapSet, node_syncMap_mapSet) ;
						node_syncMap_mapSet.setValue1(new MapOfMonitor<ISafeSyncMapMonitor>(2) ) ;
						node_syncMap_mapSet.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
					}
					matchedEntry = node_syncMap_mapSet;
				}
			}
			// D(X) main:1
			ISafeSyncMapMonitor matchedLeaf = matchedEntry.getValue3() ;
			if ((matchedLeaf == null) ) {
				if ((wr_syncMap == null) ) {
					wr_syncMap = new CachedWeakReference(syncMap) ;
				}
				if ((wr_mapSet == null) ) {
					wr_mapSet = new CachedWeakReference(mapSet) ;
				}
				{
					// D(X) createNewMonitorStates:4 when Dom(theta'') = <syncMap>
					SafeSyncMapMonitorDiSL sourceLeaf = null;
					{
						// FindCode
						Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
						if ((node_syncMap != null) ) {
							SafeSyncMapMonitorDiSL itmdLeaf = node_syncMap.getValue3() ;
							sourceLeaf = itmdLeaf;
						}
					}
					if ((sourceLeaf != null) ) {
						boolean definable = true;
						// D(X) defineTo:1--5 for <syncMap, mapSet>
						if (definable) {
							// FindCode
							Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
							if ((node_syncMap != null) ) {
								Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
								if ((node_syncMap_mapSet != null) ) {
									ISafeSyncMapMonitor itmdLeaf = node_syncMap_mapSet.getValue3() ;
									if ((itmdLeaf != null) ) {
										if (((itmdLeaf.getDisable() > sourceLeaf.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceLeaf.getTau() ) ) ) ) {
											definable = false;
										}
									}
								}
							}
						}
						if (definable) {
							// D(X) defineTo:6
							SafeSyncMapMonitorDiSL created = (SafeSyncMapMonitorDiSL)sourceLeaf.clone() ;
							matchedEntry.setValue3(created) ;
							matchedLeaf = created;
							SafeSyncMapMonitor_SetDiSL enclosingSet = matchedEntry.getValue2() ;
							enclosingSet.add(created) ;
							// D(X) defineTo:7 for <syncMap>
							{
								// InsertMonitor
								Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
								if ((node_syncMap == null) ) {
									node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
									SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
									node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
									node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
								}
								SafeSyncMapMonitor_SetDiSL targetSet = node_syncMap.getValue2() ;
								targetSet.add(created) ;
							}
							// D(X) defineTo:7 for <mapSet-syncMap, mapSet>
							{
								// InsertMonitor
								Tuple2<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_mapSet = SafeSyncMap_mapSet__To__syncMap_mapSet_Map.getNodeEquivalent(wr_mapSet) ;
								if ((node_mapSet == null) ) {
									node_mapSet = new Tuple2<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
									SafeSyncMap_mapSet__To__syncMap_mapSet_Map.putNode(wr_mapSet, node_mapSet) ;
									node_mapSet.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
								}
								SafeSyncMapMonitor_SetDiSL targetSet = node_mapSet.getValue1() ;
								targetSet.add(created) ;
							}
						}
					}
				}
				// D(X) main:6
				ISafeSyncMapMonitor disableUpdatedLeaf = matchedEntry.getValue3() ;
				if ((disableUpdatedLeaf == null) ) {
					SafeSyncMapDisableHolderDiSL holder = new SafeSyncMapDisableHolderDiSL(-1) ;
					matchedEntry.setValue3(holder) ;
					disableUpdatedLeaf = holder;
				}
				disableUpdatedLeaf.setDisable(SafeSyncMap_timestamp++) ;
			}
			// D(X) main:8--9
			SafeSyncMapMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue2() ;
			stateTransitionedSet.event_createSet(syncMap, mapSet);

			if ((cachehit == false) ) {
				SafeSyncMap_syncMap_mapSet_Map_cachekey_mapSet = mapSet;
				SafeSyncMap_syncMap_mapSet_Map_cachekey_syncMap = syncMap;
				SafeSyncMap_syncMap_mapSet_Map_cachevalue = matchedEntry;
			}

		}

		SafeSyncMap_RVMLock.unlock();
	}

	public static final void syncCreateIterEvent(Set mapSet, Iterator iter) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncMap_activated) {
			CachedWeakReference wr_iter = null;
			CachedWeakReference wr_mapSet = null;
			Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> matchedEntry = null;
			boolean cachehit = false;
			if (((iter == SafeSyncMap_mapSet_iter_Map_cachekey_iter) && (mapSet == SafeSyncMap_mapSet_iter_Map_cachekey_mapSet) ) ) {
				matchedEntry = SafeSyncMap_mapSet_iter_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_mapSet = new CachedWeakReference(mapSet) ;
				wr_iter = new CachedWeakReference(iter) ;
				{
					// FindOrCreateEntry
					MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet = SafeSyncMap_mapSet_iter_Map.getNodeEquivalent(wr_mapSet) ;
					if ((node_mapSet == null) ) {
						node_mapSet = new MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ;
						SafeSyncMap_mapSet_iter_Map.putNode(wr_mapSet, node_mapSet) ;
					}
					Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet_iter = node_mapSet.getNodeEquivalent(wr_iter) ;
					if ((node_mapSet_iter == null) ) {
						node_mapSet_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
						node_mapSet.putNode(wr_iter, node_mapSet_iter) ;
						node_mapSet_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
					}
					matchedEntry = node_mapSet_iter;
				}
			}
			// D(X) main:1
			ISafeSyncMapMonitor matchedLeaf = matchedEntry.getValue2() ;
			if ((matchedLeaf == null) ) {
				if ((wr_mapSet == null) ) {
					wr_mapSet = new CachedWeakReference(mapSet) ;
				}
				if ((wr_iter == null) ) {
					wr_iter = new CachedWeakReference(iter) ;
				}
				{
					// D(X) createNewMonitorStates:4 when Dom(theta'') = <mapSet>
					SafeSyncMapMonitor_SetDiSL sourceSet = null;
					{
						// FindCode
						Tuple2<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_mapSet = SafeSyncMap_mapSet__To__syncMap_mapSet_Map.getNodeEquivalent(wr_mapSet) ;
						if ((node_mapSet != null) ) {
							SafeSyncMapMonitor_SetDiSL itmdSet = node_mapSet.getValue1() ;
							sourceSet = itmdSet;
						}
					}
					if ((sourceSet != null) ) {
						int numalive = 0;
						int setlen = sourceSet.getSize() ;
						for (int ielem = 0; (ielem < setlen) ;++ielem) {
							SafeSyncMapMonitorDiSL sourceMonitor = sourceSet.get(ielem) ;
							if ((!sourceMonitor.isTerminated() && (sourceMonitor.RVMRef_syncMap.get() != null) ) ) {
								sourceSet.set(numalive++, sourceMonitor) ;
								CachedWeakReference wr_syncMap = sourceMonitor.RVMRef_syncMap;
								MapOfMonitor<ISafeSyncMapMonitor> destLastMap = null;
								ISafeSyncMapMonitor destLeaf = null;
								{
									// FindOrCreate
									Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
									if ((node_syncMap == null) ) {
										node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
										SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
										node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
										node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
									}
									Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
									if ((node_syncMap_mapSet == null) ) {
										node_syncMap_mapSet = new Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
										node_syncMap.getValue1() .putNode(wr_mapSet, node_syncMap_mapSet) ;
										node_syncMap_mapSet.setValue1(new MapOfMonitor<ISafeSyncMapMonitor>(2) ) ;
										node_syncMap_mapSet.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
									}
									MapOfMonitor<ISafeSyncMapMonitor> itmdMap = node_syncMap_mapSet.getValue1() ;
									destLastMap = itmdMap;
									ISafeSyncMapMonitor node_syncMap_mapSet_iter = node_syncMap_mapSet.getValue1() .getNodeEquivalent(wr_iter) ;
									destLeaf = node_syncMap_mapSet_iter;
								}
								if (((destLeaf == null) || destLeaf instanceof SafeSyncMapDisableHolderDiSL) ) {
									boolean definable = true;
									// D(X) defineTo:1--5 for <iter>
									if (definable) {
										// FindCode
										Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_iter = SafeSyncMap_iter_Map.getNodeEquivalent(wr_iter) ;
										if ((node_iter != null) ) {
											ISafeSyncMapMonitor itmdLeaf = node_iter.getValue2() ;
											if ((itmdLeaf != null) ) {
												if (((itmdLeaf.getDisable() > sourceMonitor.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceMonitor.getTau() ) ) ) ) {
													definable = false;
												}
											}
										}
									}
									// D(X) defineTo:1--5 for <mapSet, iter>
									if (definable) {
										// FindCode
										MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet = SafeSyncMap_mapSet_iter_Map.getNodeEquivalent(wr_mapSet) ;
										if ((node_mapSet != null) ) {
											Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet_iter = node_mapSet.getNodeEquivalent(wr_iter) ;
											if ((node_mapSet_iter != null) ) {
												ISafeSyncMapMonitor itmdLeaf = node_mapSet_iter.getValue2() ;
												if ((itmdLeaf != null) ) {
													if (((itmdLeaf.getDisable() > sourceMonitor.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceMonitor.getTau() ) ) ) ) {
														definable = false;
													}
												}
											}
										}
									}
									// D(X) defineTo:1--5 for <syncMap, mapSet, iter>
									if (definable) {
										// FindCode
										Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
										if ((node_syncMap != null) ) {
											Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
											if ((node_syncMap_mapSet != null) ) {
												ISafeSyncMapMonitor node_syncMap_mapSet_iter = node_syncMap_mapSet.getValue1() .getNodeEquivalent(wr_iter) ;
												if ((node_syncMap_mapSet_iter != null) ) {
													if (((node_syncMap_mapSet_iter.getDisable() > sourceMonitor.getTau() ) || ((node_syncMap_mapSet_iter.getTau() > 0) && (node_syncMap_mapSet_iter.getTau() < sourceMonitor.getTau() ) ) ) ) {
														definable = false;
													}
												}
											}
										}
									}
									if (definable) {
										// D(X) defineTo:6
										SafeSyncMapMonitorDiSL created = (SafeSyncMapMonitorDiSL)sourceMonitor.clone() ;
										destLastMap.putNode(wr_iter, created) ;
										// D(X) defineTo:7 for <iter>
										{
											// InsertMonitor
											Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_iter = SafeSyncMap_iter_Map.getNodeEquivalent(wr_iter) ;
											if ((node_iter == null) ) {
												node_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
												SafeSyncMap_iter_Map.putNode(wr_iter, node_iter) ;
												node_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_iter.getValue1() ;
											targetSet.add(created) ;
										}
										// D(X) defineTo:7 for <mapSet, iter>
										{
											// InsertMonitor
											MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet = SafeSyncMap_mapSet_iter_Map.getNodeEquivalent(wr_mapSet) ;
											if ((node_mapSet == null) ) {
												node_mapSet = new MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ;
												SafeSyncMap_mapSet_iter_Map.putNode(wr_mapSet, node_mapSet) ;
											}
											Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet_iter = node_mapSet.getNodeEquivalent(wr_iter) ;
											if ((node_mapSet_iter == null) ) {
												node_mapSet_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
												node_mapSet.putNode(wr_iter, node_mapSet_iter) ;
												node_mapSet_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_mapSet_iter.getValue1() ;
											targetSet.add(created) ;
										}
										// D(X) defineTo:7 for <syncMap>
										{
											// InsertMonitor
											Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
											if ((node_syncMap == null) ) {
												node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
												SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
												node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
												node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_syncMap.getValue2() ;
											targetSet.add(created) ;
										}
										// D(X) defineTo:7 for <syncMap, mapSet>
										{
											// InsertMonitor
											Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
											if ((node_syncMap == null) ) {
												node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
												SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
												node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
												node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
											if ((node_syncMap_mapSet == null) ) {
												node_syncMap_mapSet = new Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
												node_syncMap.getValue1() .putNode(wr_mapSet, node_syncMap_mapSet) ;
												node_syncMap_mapSet.setValue1(new MapOfMonitor<ISafeSyncMapMonitor>(2) ) ;
												node_syncMap_mapSet.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_syncMap_mapSet.getValue2() ;
											targetSet.add(created) ;
										}
									}
								}
							}
						}
						sourceSet.eraseRange(numalive) ;
					}
				}
				// D(X) main:6
				ISafeSyncMapMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
				if ((disableUpdatedLeaf == null) ) {
					SafeSyncMapDisableHolderDiSL holder = new SafeSyncMapDisableHolderDiSL(-1) ;
					matchedEntry.setValue2(holder) ;
					disableUpdatedLeaf = holder;
				}
				disableUpdatedLeaf.setDisable(SafeSyncMap_timestamp++) ;
			}
			// D(X) main:8--9
			SafeSyncMapMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
			stateTransitionedSet.event_syncCreateIter(mapSet, iter);

			if ((cachehit == false) ) {
				SafeSyncMap_mapSet_iter_Map_cachekey_iter = iter;
				SafeSyncMap_mapSet_iter_Map_cachekey_mapSet = mapSet;
				SafeSyncMap_mapSet_iter_Map_cachevalue = matchedEntry;
			}

		}

		SafeSyncMap_RVMLock.unlock();
	}

	public static final void asyncCreateIterEvent(Set mapSet, Iterator iter) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncMap_activated) {
			CachedWeakReference wr_iter = null;
			CachedWeakReference wr_mapSet = null;
			Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> matchedEntry = null;
			boolean cachehit = false;
			if (((iter == SafeSyncMap_mapSet_iter_Map_cachekey_iter) && (mapSet == SafeSyncMap_mapSet_iter_Map_cachekey_mapSet) ) ) {
				matchedEntry = SafeSyncMap_mapSet_iter_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_mapSet = new CachedWeakReference(mapSet) ;
				wr_iter = new CachedWeakReference(iter) ;
				{
					// FindOrCreateEntry
					MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet = SafeSyncMap_mapSet_iter_Map.getNodeEquivalent(wr_mapSet) ;
					if ((node_mapSet == null) ) {
						node_mapSet = new MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ;
						SafeSyncMap_mapSet_iter_Map.putNode(wr_mapSet, node_mapSet) ;
					}
					Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet_iter = node_mapSet.getNodeEquivalent(wr_iter) ;
					if ((node_mapSet_iter == null) ) {
						node_mapSet_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
						node_mapSet.putNode(wr_iter, node_mapSet_iter) ;
						node_mapSet_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
					}
					matchedEntry = node_mapSet_iter;
				}
			}
			// D(X) main:1
			ISafeSyncMapMonitor matchedLeaf = matchedEntry.getValue2() ;
			if ((matchedLeaf == null) ) {
				if ((wr_mapSet == null) ) {
					wr_mapSet = new CachedWeakReference(mapSet) ;
				}
				if ((wr_iter == null) ) {
					wr_iter = new CachedWeakReference(iter) ;
				}
				{
					// D(X) createNewMonitorStates:4 when Dom(theta'') = <mapSet>
					SafeSyncMapMonitor_SetDiSL sourceSet = null;
					{
						// FindCode
						Tuple2<SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_mapSet = SafeSyncMap_mapSet__To__syncMap_mapSet_Map.getNodeEquivalent(wr_mapSet) ;
						if ((node_mapSet != null) ) {
							SafeSyncMapMonitor_SetDiSL itmdSet = node_mapSet.getValue1() ;
							sourceSet = itmdSet;
						}
					}
					if ((sourceSet != null) ) {
						int numalive = 0;
						int setlen = sourceSet.getSize() ;
						for (int ielem = 0; (ielem < setlen) ;++ielem) {
							SafeSyncMapMonitorDiSL sourceMonitor = sourceSet.get(ielem) ;
							if ((!sourceMonitor.isTerminated() && (sourceMonitor.RVMRef_syncMap.get() != null) ) ) {
								sourceSet.set(numalive++, sourceMonitor) ;
								CachedWeakReference wr_syncMap = sourceMonitor.RVMRef_syncMap;
								MapOfMonitor<ISafeSyncMapMonitor> destLastMap = null;
								ISafeSyncMapMonitor destLeaf = null;
								{
									// FindOrCreate
									Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
									if ((node_syncMap == null) ) {
										node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
										SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
										node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
										node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
									}
									Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
									if ((node_syncMap_mapSet == null) ) {
										node_syncMap_mapSet = new Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
										node_syncMap.getValue1() .putNode(wr_mapSet, node_syncMap_mapSet) ;
										node_syncMap_mapSet.setValue1(new MapOfMonitor<ISafeSyncMapMonitor>(2) ) ;
										node_syncMap_mapSet.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
									}
									MapOfMonitor<ISafeSyncMapMonitor> itmdMap = node_syncMap_mapSet.getValue1() ;
									destLastMap = itmdMap;
									ISafeSyncMapMonitor node_syncMap_mapSet_iter = node_syncMap_mapSet.getValue1() .getNodeEquivalent(wr_iter) ;
									destLeaf = node_syncMap_mapSet_iter;
								}
								if (((destLeaf == null) || destLeaf instanceof SafeSyncMapDisableHolderDiSL) ) {
									boolean definable = true;
									// D(X) defineTo:1--5 for <iter>
									if (definable) {
										// FindCode
										Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_iter = SafeSyncMap_iter_Map.getNodeEquivalent(wr_iter) ;
										if ((node_iter != null) ) {
											ISafeSyncMapMonitor itmdLeaf = node_iter.getValue2() ;
											if ((itmdLeaf != null) ) {
												if (((itmdLeaf.getDisable() > sourceMonitor.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceMonitor.getTau() ) ) ) ) {
													definable = false;
												}
											}
										}
									}
									// D(X) defineTo:1--5 for <mapSet, iter>
									if (definable) {
										// FindCode
										MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet = SafeSyncMap_mapSet_iter_Map.getNodeEquivalent(wr_mapSet) ;
										if ((node_mapSet != null) ) {
											Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet_iter = node_mapSet.getNodeEquivalent(wr_iter) ;
											if ((node_mapSet_iter != null) ) {
												ISafeSyncMapMonitor itmdLeaf = node_mapSet_iter.getValue2() ;
												if ((itmdLeaf != null) ) {
													if (((itmdLeaf.getDisable() > sourceMonitor.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceMonitor.getTau() ) ) ) ) {
														definable = false;
													}
												}
											}
										}
									}
									// D(X) defineTo:1--5 for <syncMap, mapSet, iter>
									if (definable) {
										// FindCode
										Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
										if ((node_syncMap != null) ) {
											Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
											if ((node_syncMap_mapSet != null) ) {
												ISafeSyncMapMonitor node_syncMap_mapSet_iter = node_syncMap_mapSet.getValue1() .getNodeEquivalent(wr_iter) ;
												if ((node_syncMap_mapSet_iter != null) ) {
													if (((node_syncMap_mapSet_iter.getDisable() > sourceMonitor.getTau() ) || ((node_syncMap_mapSet_iter.getTau() > 0) && (node_syncMap_mapSet_iter.getTau() < sourceMonitor.getTau() ) ) ) ) {
														definable = false;
													}
												}
											}
										}
									}
									if (definable) {
										// D(X) defineTo:6
										SafeSyncMapMonitorDiSL created = (SafeSyncMapMonitorDiSL)sourceMonitor.clone() ;
										destLastMap.putNode(wr_iter, created) ;
										// D(X) defineTo:7 for <iter>
										{
											// InsertMonitor
											Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_iter = SafeSyncMap_iter_Map.getNodeEquivalent(wr_iter) ;
											if ((node_iter == null) ) {
												node_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
												SafeSyncMap_iter_Map.putNode(wr_iter, node_iter) ;
												node_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_iter.getValue1() ;
											targetSet.add(created) ;
										}
										// D(X) defineTo:7 for <mapSet, iter>
										{
											// InsertMonitor
											MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet = SafeSyncMap_mapSet_iter_Map.getNodeEquivalent(wr_mapSet) ;
											if ((node_mapSet == null) ) {
												node_mapSet = new MapOfSetMonitor<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ;
												SafeSyncMap_mapSet_iter_Map.putNode(wr_mapSet, node_mapSet) ;
											}
											Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_mapSet_iter = node_mapSet.getNodeEquivalent(wr_iter) ;
											if ((node_mapSet_iter == null) ) {
												node_mapSet_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
												node_mapSet.putNode(wr_iter, node_mapSet_iter) ;
												node_mapSet_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_mapSet_iter.getValue1() ;
											targetSet.add(created) ;
										}
										// D(X) defineTo:7 for <syncMap>
										{
											// InsertMonitor
											Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
											if ((node_syncMap == null) ) {
												node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
												SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
												node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
												node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_syncMap.getValue2() ;
											targetSet.add(created) ;
										}
										// D(X) defineTo:7 for <syncMap, mapSet>
										{
											// InsertMonitor
											Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL> node_syncMap = SafeSyncMap_syncMap_mapSet_iter_Map.getNodeEquivalent(wr_syncMap) ;
											if ((node_syncMap == null) ) {
												node_syncMap = new Tuple3<MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, SafeSyncMapMonitorDiSL>() ;
												SafeSyncMap_syncMap_mapSet_iter_Map.putNode(wr_syncMap, node_syncMap) ;
												node_syncMap.setValue1(new MapOfAll<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>(1) ) ;
												node_syncMap.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_syncMap_mapSet = node_syncMap.getValue1() .getNodeEquivalent(wr_mapSet) ;
											if ((node_syncMap_mapSet == null) ) {
												node_syncMap_mapSet = new Tuple3<MapOfMonitor<ISafeSyncMapMonitor>, SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
												node_syncMap.getValue1() .putNode(wr_mapSet, node_syncMap_mapSet) ;
												node_syncMap_mapSet.setValue1(new MapOfMonitor<ISafeSyncMapMonitor>(2) ) ;
												node_syncMap_mapSet.setValue2(new SafeSyncMapMonitor_SetDiSL() ) ;
											}
											SafeSyncMapMonitor_SetDiSL targetSet = node_syncMap_mapSet.getValue2() ;
											targetSet.add(created) ;
										}
									}
								}
							}
						}
						sourceSet.eraseRange(numalive) ;
					}
				}
				// D(X) main:6
				ISafeSyncMapMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
				if ((disableUpdatedLeaf == null) ) {
					SafeSyncMapDisableHolderDiSL holder = new SafeSyncMapDisableHolderDiSL(-1) ;
					matchedEntry.setValue2(holder) ;
					disableUpdatedLeaf = holder;
				}
				disableUpdatedLeaf.setDisable(SafeSyncMap_timestamp++) ;
			}
			// D(X) main:8--9
			SafeSyncMapMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
			stateTransitionedSet.event_asyncCreateIter(mapSet, iter);

			if ((cachehit == false) ) {
				SafeSyncMap_mapSet_iter_Map_cachekey_iter = iter;
				SafeSyncMap_mapSet_iter_Map_cachekey_mapSet = mapSet;
				SafeSyncMap_mapSet_iter_Map_cachevalue = matchedEntry;
			}

		}

		SafeSyncMap_RVMLock.unlock();
	}

	public static final void accessIterEvent(Iterator iter) {
		while (!SafeSyncMap_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncMap_activated) {
			CachedWeakReference wr_iter = null;
			Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> matchedEntry = null;
			boolean cachehit = false;
			if ((iter == SafeSyncMap_iter_Map_cachekey_iter) ) {
				matchedEntry = SafeSyncMap_iter_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_iter = new CachedWeakReference(iter) ;
				{
					// FindOrCreateEntry
					Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor> node_iter = SafeSyncMap_iter_Map.getNodeEquivalent(wr_iter) ;
					if ((node_iter == null) ) {
						node_iter = new Tuple2<SafeSyncMapMonitor_SetDiSL, ISafeSyncMapMonitor>() ;
						SafeSyncMap_iter_Map.putNode(wr_iter, node_iter) ;
						node_iter.setValue1(new SafeSyncMapMonitor_SetDiSL() ) ;
					}
					matchedEntry = node_iter;
				}
			}
			// D(X) main:1
			ISafeSyncMapMonitor matchedLeaf = matchedEntry.getValue2() ;
			if ((matchedLeaf == null) ) {
				if ((wr_iter == null) ) {
					wr_iter = new CachedWeakReference(iter) ;
				}
				// D(X) main:6
				ISafeSyncMapMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
				if ((disableUpdatedLeaf == null) ) {
					SafeSyncMapDisableHolderDiSL holder = new SafeSyncMapDisableHolderDiSL(-1) ;
					matchedEntry.setValue2(holder) ;
					disableUpdatedLeaf = holder;
				}
				disableUpdatedLeaf.setDisable(SafeSyncMap_timestamp++) ;
			}
			// D(X) main:8--9
			SafeSyncMapMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
			stateTransitionedSet.event_accessIter(iter);

			if ((cachehit == false) ) {
				SafeSyncMap_iter_Map_cachekey_iter = iter;
				SafeSyncMap_iter_Map_cachevalue = matchedEntry;
			}

		}

		SafeSyncMap_RVMLock.unlock();
	}

}
