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


final class SafeSyncCollectionMonitor_SetDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeSyncCollectionMonitorDiSL> {

	SafeSyncCollectionMonitor_SetDiSL(){
		this.size = 0;
		this.elements = new SafeSyncCollectionMonitorDiSL[4];
	}
	final void event_sync(Object c) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncCollectionMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncCollectionMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_sync(c);
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
	final void event_syncCreateIter(Object c, Iterator iter) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncCollectionMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncCollectionMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_syncCreateIter(c, iter);
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
	final void event_asyncCreateIter(Object c, Iterator iter) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			SafeSyncCollectionMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncCollectionMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_asyncCreateIter(c, iter);
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
			SafeSyncCollectionMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final SafeSyncCollectionMonitorDiSL monitorfinalMonitor = monitor;
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

interface ISafeSyncCollectionMonitor extends IMonitor, IDisableHolder {
}

class SafeSyncCollectionDisableHolderDiSL extends DisableHolder implements ISafeSyncCollectionMonitor {
	SafeSyncCollectionDisableHolderDiSL(long tau) {
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

class SafeSyncCollectionMonitorDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject, ISafeSyncCollectionMonitor {
	protected Object clone() {
		try {
			SafeSyncCollectionMonitorDiSL ret = (SafeSyncCollectionMonitorDiSL) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	Object c;

	WeakReference Ref_c = null;
	WeakReference Ref_iter = null;
	int Prop_1_state;
	static final int Prop_1_transition_sync[] = {2, 4, 4, 4, 4};;
	static final int Prop_1_transition_syncCreateIter[] = {4, 4, 1, 4, 4};;
	static final int Prop_1_transition_asyncCreateIter[] = {4, 4, 3, 4, 4};;
	static final int Prop_1_transition_accessIter[] = {4, 3, 4, 4, 4};;

	boolean Prop_1_Category_match = false;

	SafeSyncCollectionMonitorDiSL(long tau) {
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

	final boolean Prop_1_event_sync(Object c) {
		Iterator iter = null;
		if(Ref_iter != null){
			iter = (Iterator)Ref_iter.get();
		}
		{
			this.c = c;
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		RVM_lastevent = 0;

		Prop_1_state = Prop_1_transition_sync[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_syncCreateIter(Object c, Iterator iter) {
		{
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		if(Ref_iter == null){
			Ref_iter = new WeakReference(iter);
		}
		RVM_lastevent = 1;

		Prop_1_state = Prop_1_transition_syncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_asyncCreateIter(Object c, Iterator iter) {
		{
		}
		if(Ref_c == null){
			Ref_c = new WeakReference(c);
		}
		if(Ref_iter == null){
			Ref_iter = new WeakReference(iter);
		}
		RVM_lastevent = 2;

		Prop_1_state = Prop_1_transition_asyncCreateIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final boolean Prop_1_event_accessIter(Iterator iter) {
		Object c = null;
		if(Ref_c != null){
			c = (Object)Ref_c.get();
		}
		{
			if ( ! (!Thread.holdsLock(this.c)) ) {
				return false;
			}
			{
			}
		}
		if(Ref_iter == null){
			Ref_iter = new WeakReference(iter);
		}
		RVM_lastevent = 3;

		Prop_1_state = Prop_1_transition_accessIter[Prop_1_state];
		Prop_1_Category_match = Prop_1_state == 3;
		return true;
	}

	final void Prop_1_handler_match (){
		{
			//System.out.println("pattern matched!");
			CounterClassDiSL.countJoinPoints("SafeSyncCollection");
		}

	}

	final void reset() {
		RVM_lastevent = -1;
		Prop_1_state = 0;
		Prop_1_Category_match = false;
	}

	// RVMRef_c was suppressed to reduce memory overhead
	// RVMRef_iter was suppressed to reduce memory overhead

	//alive_parameters_0 = [Object c, Iterator iter]
	boolean alive_parameters_0 = true;
	//alive_parameters_1 = [Iterator iter]
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
			//sync
			//alive_c && alive_iter
			if(!(alive_parameters_0)){
				RVM_terminated = true;
				return;
			}
			break;

			case 1:
			//syncCreateIter
			//alive_iter
			if(!(alive_parameters_1)){
				RVM_terminated = true;
				return;
			}
			break;

			case 2:
			//asyncCreateIter
			return;
			case 3:
			//accessIter
			return;
		}
		return;
	}

	public static int getNumberOfEvents() {
		return 4;
	}

	public static int getNumberOfStates() {
		return 5;
	}

}

public class SafeSyncCollectionRuntimeMonitorDiSL implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeSyncCollectionMapManager;
	static {
		SafeSyncCollectionMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		SafeSyncCollectionMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock SafeSyncCollection_RVMLock = new ReentrantLock();
	static final Condition SafeSyncCollection_RVMLock_cond = SafeSyncCollection_RVMLock.newCondition();

	// Declarations for Timestamps
	private static long SafeSyncCollection_timestamp = 1;

	private static boolean SafeSyncCollection_activated = false;

	// Declarations for Indexing Trees
	private static Object SafeSyncCollection_c_Map_cachekey_c;
	private static Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> SafeSyncCollection_c_Map_cachevalue;
	private static Object SafeSyncCollection_c_iter_Map_cachekey_c;
	private static Object SafeSyncCollection_c_iter_Map_cachekey_iter;
	private static ISafeSyncCollectionMonitor SafeSyncCollection_c_iter_Map_cachevalue;
	private static Object SafeSyncCollection_iter_Map_cachekey_iter;
	private static Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> SafeSyncCollection_iter_Map_cachevalue;
	private static final MapOfAll<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> SafeSyncCollection_c_iter_Map = new MapOfAll<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL>(0) ;
	private static final MapOfSetMonitor<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> SafeSyncCollection_iter_Map = new MapOfSetMonitor<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor>(1) ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += SafeSyncCollection_c_iter_Map.cleanUpUnnecessaryMappings();
		collected += SafeSyncCollection_iter_Map.cleanUpUnnecessaryMappings();
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

	public static final void syncEvent(Object c) {
		SafeSyncCollection_activated = true;
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_c = null;
		Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> matchedEntry = null;
		boolean cachehit = false;
		if ((c == SafeSyncCollection_c_Map_cachekey_c) ) {
			matchedEntry = SafeSyncCollection_c_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_c = new CachedWeakReference(c) ;
			{
				// FindOrCreateEntry
				Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
				if ((node_c == null) ) {
					node_c = new Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL>() ;
					SafeSyncCollection_c_iter_Map.putNode(wr_c, node_c) ;
					node_c.setValue1(new MapOfMonitor<ISafeSyncCollectionMonitor>(1) ) ;
					node_c.setValue2(new SafeSyncCollectionMonitor_SetDiSL() ) ;
				}
				matchedEntry = node_c;
			}
		}
		// D(X) main:1
		SafeSyncCollectionMonitorDiSL matchedLeaf = matchedEntry.getValue3() ;
		if ((matchedLeaf == null) ) {
			if ((wr_c == null) ) {
				wr_c = new CachedWeakReference(c) ;
			}
			if ((matchedLeaf == null) ) {
				// D(X) main:4
				SafeSyncCollectionMonitorDiSL created = new SafeSyncCollectionMonitorDiSL(SafeSyncCollection_timestamp++) ;
				matchedEntry.setValue3(created) ;
				SafeSyncCollectionMonitor_SetDiSL enclosingSet = matchedEntry.getValue2() ;
				enclosingSet.add(created) ;
			}
			// D(X) main:6
			SafeSyncCollectionMonitorDiSL disableUpdatedLeaf = matchedEntry.getValue3() ;
			disableUpdatedLeaf.setDisable(SafeSyncCollection_timestamp++) ;
		}
		// D(X) main:8--9
		SafeSyncCollectionMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue2() ;
		stateTransitionedSet.event_sync(c);

		if ((cachehit == false) ) {
			SafeSyncCollection_c_Map_cachekey_c = c;
			SafeSyncCollection_c_Map_cachevalue = matchedEntry;
		}

		SafeSyncCollection_RVMLock.unlock();
	}

	public static final void syncCreateIterEvent(Object c, Iterator iter) {
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncCollection_activated) {
			CachedWeakReference wr_c = null;
			CachedWeakReference wr_iter = null;
			MapOfMonitor<ISafeSyncCollectionMonitor> matchedLastMap = null;
			ISafeSyncCollectionMonitor matchedEntry = null;
			boolean cachehit = false;
			if (((c == SafeSyncCollection_c_iter_Map_cachekey_c) && (iter == SafeSyncCollection_c_iter_Map_cachekey_iter) ) ) {
				matchedEntry = SafeSyncCollection_c_iter_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_c = new CachedWeakReference(c) ;
				wr_iter = new CachedWeakReference(iter) ;
				{
					// FindOrCreateEntry
					Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
					if ((node_c == null) ) {
						node_c = new Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL>() ;
						SafeSyncCollection_c_iter_Map.putNode(wr_c, node_c) ;
						node_c.setValue1(new MapOfMonitor<ISafeSyncCollectionMonitor>(1) ) ;
						node_c.setValue2(new SafeSyncCollectionMonitor_SetDiSL() ) ;
					}
					MapOfMonitor<ISafeSyncCollectionMonitor> itmdMap = node_c.getValue1() ;
					matchedLastMap = itmdMap;
					ISafeSyncCollectionMonitor node_c_iter = node_c.getValue1() .getNodeEquivalent(wr_iter) ;
					matchedEntry = node_c_iter;
				}
			}
			// D(X) main:1
			if ((matchedEntry == null) ) {
				if ((wr_c == null) ) {
					wr_c = new CachedWeakReference(c) ;
				}
				if ((wr_iter == null) ) {
					wr_iter = new CachedWeakReference(iter) ;
				}
				{
					// D(X) createNewMonitorStates:4 when Dom(theta'') = <c>
					SafeSyncCollectionMonitorDiSL sourceLeaf = null;
					{
						// FindCode
						Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
						if ((node_c != null) ) {
							SafeSyncCollectionMonitorDiSL itmdLeaf = node_c.getValue3() ;
							sourceLeaf = itmdLeaf;
						}
					}
					if ((sourceLeaf != null) ) {
						boolean definable = true;
						// D(X) defineTo:1--5 for <c, iter>
						if (definable) {
							// FindCode
							Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
							if ((node_c != null) ) {
								ISafeSyncCollectionMonitor node_c_iter = node_c.getValue1() .getNodeEquivalent(wr_iter) ;
								if ((node_c_iter != null) ) {
									if (((node_c_iter.getDisable() > sourceLeaf.getTau() ) || ((node_c_iter.getTau() > 0) && (node_c_iter.getTau() < sourceLeaf.getTau() ) ) ) ) {
										definable = false;
									}
								}
							}
						}
						// D(X) defineTo:1--5 for <iter>
						if (definable) {
							// FindCode
							Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> node_iter = SafeSyncCollection_iter_Map.getNodeEquivalent(wr_iter) ;
							if ((node_iter != null) ) {
								ISafeSyncCollectionMonitor itmdLeaf = node_iter.getValue2() ;
								if ((itmdLeaf != null) ) {
									if (((itmdLeaf.getDisable() > sourceLeaf.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceLeaf.getTau() ) ) ) ) {
										definable = false;
									}
								}
							}
						}
						if (definable) {
							// D(X) defineTo:6
							SafeSyncCollectionMonitorDiSL created = (SafeSyncCollectionMonitorDiSL)sourceLeaf.clone() ;
							matchedEntry = created;
							matchedLastMap.putNode(wr_iter, created) ;
							// D(X) defineTo:7 for <c>
							{
								// InsertMonitor
								Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
								if ((node_c == null) ) {
									node_c = new Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL>() ;
									SafeSyncCollection_c_iter_Map.putNode(wr_c, node_c) ;
									node_c.setValue1(new MapOfMonitor<ISafeSyncCollectionMonitor>(1) ) ;
									node_c.setValue2(new SafeSyncCollectionMonitor_SetDiSL() ) ;
								}
								SafeSyncCollectionMonitor_SetDiSL targetSet = node_c.getValue2() ;
								targetSet.add(created) ;
							}
							// D(X) defineTo:7 for <iter>
							{
								// InsertMonitor
								Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> node_iter = SafeSyncCollection_iter_Map.getNodeEquivalent(wr_iter) ;
								if ((node_iter == null) ) {
									node_iter = new Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor>() ;
									SafeSyncCollection_iter_Map.putNode(wr_iter, node_iter) ;
									node_iter.setValue1(new SafeSyncCollectionMonitor_SetDiSL() ) ;
								}
								SafeSyncCollectionMonitor_SetDiSL targetSet = node_iter.getValue1() ;
								targetSet.add(created) ;
							}
						}
					}
				}
				// D(X) main:6
				if ((matchedEntry == null) ) {
					SafeSyncCollectionDisableHolderDiSL holder = new SafeSyncCollectionDisableHolderDiSL(-1) ;
					matchedLastMap.putNode(wr_iter, holder) ;
					matchedEntry = holder;
				}
				matchedEntry.setDisable(SafeSyncCollection_timestamp++) ;
			}
			// D(X) main:8--9
			if (matchedEntry instanceof SafeSyncCollectionMonitorDiSL) {
				SafeSyncCollectionMonitorDiSL monitor = (SafeSyncCollectionMonitorDiSL)matchedEntry;
				final SafeSyncCollectionMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_syncCreateIter(c, iter);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}

				if ((cachehit == false) ) {
					SafeSyncCollection_c_iter_Map_cachekey_c = c;
					SafeSyncCollection_c_iter_Map_cachekey_iter = iter;
					SafeSyncCollection_c_iter_Map_cachevalue = matchedEntry;
				}
			}

		}

		SafeSyncCollection_RVMLock.unlock();
	}

	public static final void asyncCreateIterEvent(Object c, Iterator iter) {
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncCollection_activated) {
			CachedWeakReference wr_c = null;
			CachedWeakReference wr_iter = null;
			MapOfMonitor<ISafeSyncCollectionMonitor> matchedLastMap = null;
			ISafeSyncCollectionMonitor matchedEntry = null;
			boolean cachehit = false;
			if (((c == SafeSyncCollection_c_iter_Map_cachekey_c) && (iter == SafeSyncCollection_c_iter_Map_cachekey_iter) ) ) {
				matchedEntry = SafeSyncCollection_c_iter_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_c = new CachedWeakReference(c) ;
				wr_iter = new CachedWeakReference(iter) ;
				{
					// FindOrCreateEntry
					Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
					if ((node_c == null) ) {
						node_c = new Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL>() ;
						SafeSyncCollection_c_iter_Map.putNode(wr_c, node_c) ;
						node_c.setValue1(new MapOfMonitor<ISafeSyncCollectionMonitor>(1) ) ;
						node_c.setValue2(new SafeSyncCollectionMonitor_SetDiSL() ) ;
					}
					MapOfMonitor<ISafeSyncCollectionMonitor> itmdMap = node_c.getValue1() ;
					matchedLastMap = itmdMap;
					ISafeSyncCollectionMonitor node_c_iter = node_c.getValue1() .getNodeEquivalent(wr_iter) ;
					matchedEntry = node_c_iter;
				}
			}
			// D(X) main:1
			if ((matchedEntry == null) ) {
				if ((wr_c == null) ) {
					wr_c = new CachedWeakReference(c) ;
				}
				if ((wr_iter == null) ) {
					wr_iter = new CachedWeakReference(iter) ;
				}
				{
					// D(X) createNewMonitorStates:4 when Dom(theta'') = <c>
					SafeSyncCollectionMonitorDiSL sourceLeaf = null;
					{
						// FindCode
						Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
						if ((node_c != null) ) {
							SafeSyncCollectionMonitorDiSL itmdLeaf = node_c.getValue3() ;
							sourceLeaf = itmdLeaf;
						}
					}
					if ((sourceLeaf != null) ) {
						boolean definable = true;
						// D(X) defineTo:1--5 for <c, iter>
						if (definable) {
							// FindCode
							Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
							if ((node_c != null) ) {
								ISafeSyncCollectionMonitor node_c_iter = node_c.getValue1() .getNodeEquivalent(wr_iter) ;
								if ((node_c_iter != null) ) {
									if (((node_c_iter.getDisable() > sourceLeaf.getTau() ) || ((node_c_iter.getTau() > 0) && (node_c_iter.getTau() < sourceLeaf.getTau() ) ) ) ) {
										definable = false;
									}
								}
							}
						}
						// D(X) defineTo:1--5 for <iter>
						if (definable) {
							// FindCode
							Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> node_iter = SafeSyncCollection_iter_Map.getNodeEquivalent(wr_iter) ;
							if ((node_iter != null) ) {
								ISafeSyncCollectionMonitor itmdLeaf = node_iter.getValue2() ;
								if ((itmdLeaf != null) ) {
									if (((itmdLeaf.getDisable() > sourceLeaf.getTau() ) || ((itmdLeaf.getTau() > 0) && (itmdLeaf.getTau() < sourceLeaf.getTau() ) ) ) ) {
										definable = false;
									}
								}
							}
						}
						if (definable) {
							// D(X) defineTo:6
							SafeSyncCollectionMonitorDiSL created = (SafeSyncCollectionMonitorDiSL)sourceLeaf.clone() ;
							matchedEntry = created;
							matchedLastMap.putNode(wr_iter, created) ;
							// D(X) defineTo:7 for <c>
							{
								// InsertMonitor
								Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL> node_c = SafeSyncCollection_c_iter_Map.getNodeEquivalent(wr_c) ;
								if ((node_c == null) ) {
									node_c = new Tuple3<MapOfMonitor<ISafeSyncCollectionMonitor>, SafeSyncCollectionMonitor_SetDiSL, SafeSyncCollectionMonitorDiSL>() ;
									SafeSyncCollection_c_iter_Map.putNode(wr_c, node_c) ;
									node_c.setValue1(new MapOfMonitor<ISafeSyncCollectionMonitor>(1) ) ;
									node_c.setValue2(new SafeSyncCollectionMonitor_SetDiSL() ) ;
								}
								SafeSyncCollectionMonitor_SetDiSL targetSet = node_c.getValue2() ;
								targetSet.add(created) ;
							}
							// D(X) defineTo:7 for <iter>
							{
								// InsertMonitor
								Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> node_iter = SafeSyncCollection_iter_Map.getNodeEquivalent(wr_iter) ;
								if ((node_iter == null) ) {
									node_iter = new Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor>() ;
									SafeSyncCollection_iter_Map.putNode(wr_iter, node_iter) ;
									node_iter.setValue1(new SafeSyncCollectionMonitor_SetDiSL() ) ;
								}
								SafeSyncCollectionMonitor_SetDiSL targetSet = node_iter.getValue1() ;
								targetSet.add(created) ;
							}
						}
					}
				}
				// D(X) main:6
				if ((matchedEntry == null) ) {
					SafeSyncCollectionDisableHolderDiSL holder = new SafeSyncCollectionDisableHolderDiSL(-1) ;
					matchedLastMap.putNode(wr_iter, holder) ;
					matchedEntry = holder;
				}
				matchedEntry.setDisable(SafeSyncCollection_timestamp++) ;
			}
			// D(X) main:8--9
			if (matchedEntry instanceof SafeSyncCollectionMonitorDiSL) {
				SafeSyncCollectionMonitorDiSL monitor = (SafeSyncCollectionMonitorDiSL)matchedEntry;
				final SafeSyncCollectionMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_asyncCreateIter(c, iter);
				if(monitorfinalMonitor.Prop_1_Category_match) {
					monitorfinalMonitor.Prop_1_handler_match();
				}

				if ((cachehit == false) ) {
					SafeSyncCollection_c_iter_Map_cachekey_c = c;
					SafeSyncCollection_c_iter_Map_cachekey_iter = iter;
					SafeSyncCollection_c_iter_Map_cachevalue = matchedEntry;
				}
			}

		}

		SafeSyncCollection_RVMLock.unlock();
	}

	public static final void accessIterEvent(Iterator iter) {
		while (!SafeSyncCollection_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (SafeSyncCollection_activated) {
			CachedWeakReference wr_iter = null;
			Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> matchedEntry = null;
			boolean cachehit = false;
			if ((iter == SafeSyncCollection_iter_Map_cachekey_iter) ) {
				matchedEntry = SafeSyncCollection_iter_Map_cachevalue;
				cachehit = true;
			}
			else {
				wr_iter = new CachedWeakReference(iter) ;
				{
					// FindOrCreateEntry
					Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor> node_iter = SafeSyncCollection_iter_Map.getNodeEquivalent(wr_iter) ;
					if ((node_iter == null) ) {
						node_iter = new Tuple2<SafeSyncCollectionMonitor_SetDiSL, ISafeSyncCollectionMonitor>() ;
						SafeSyncCollection_iter_Map.putNode(wr_iter, node_iter) ;
						node_iter.setValue1(new SafeSyncCollectionMonitor_SetDiSL() ) ;
					}
					matchedEntry = node_iter;
				}
			}
			// D(X) main:1
			ISafeSyncCollectionMonitor matchedLeaf = matchedEntry.getValue2() ;
			if ((matchedLeaf == null) ) {
				if ((wr_iter == null) ) {
					wr_iter = new CachedWeakReference(iter) ;
				}
				// D(X) main:6
				ISafeSyncCollectionMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
				if ((disableUpdatedLeaf == null) ) {
					SafeSyncCollectionDisableHolderDiSL holder = new SafeSyncCollectionDisableHolderDiSL(-1) ;
					matchedEntry.setValue2(holder) ;
					disableUpdatedLeaf = holder;
				}
				disableUpdatedLeaf.setDisable(SafeSyncCollection_timestamp++) ;
			}
			// D(X) main:8--9
			SafeSyncCollectionMonitor_SetDiSL stateTransitionedSet = matchedEntry.getValue1() ;
			stateTransitionedSet.event_accessIter(iter);

			if ((cachehit == false) ) {
				SafeSyncCollection_iter_Map_cachekey_iter = iter;
				SafeSyncCollection_iter_Map_cachevalue = matchedEntry;
			}

		}

		SafeSyncCollection_RVMLock.unlock();
	}

}

