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


final class FileCloseMonitor_SetDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<FileCloseMonitorDiSL> {

	FileCloseMonitor_SetDiSL(){
		this.size = 0;
		this.elements = new FileCloseMonitorDiSL[4];
	}
	final void event_write(FileWriter f) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			FileCloseMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final FileCloseMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_write(f);
				if(monitorfinalMonitor.Prop_1_Category_fail) {
					monitorfinalMonitor.Prop_1_handler_fail();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
	final void event_close(FileWriter f) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			FileCloseMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final FileCloseMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_close(f);
				if(monitorfinalMonitor.Prop_1_Category_fail) {
					monitorfinalMonitor.Prop_1_handler_fail();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
	final void event_endProg() {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			FileCloseMonitorDiSL monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final FileCloseMonitorDiSL monitorfinalMonitor = monitor;
				monitor.Prop_1_event_endProg();
				if(monitorfinalMonitor.Prop_1_Category_fail) {
					monitorfinalMonitor.Prop_1_handler_fail();
				}
			}
		}
		for(int i = numAlive; i < this.size; i++){
			this.elements[i] = null;
		}
		size = numAlive;
	}
}

class FileCloseMonitorDiSL extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractAtomicMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	protected Object clone() {
		try {
			FileCloseMonitorDiSL ret = (FileCloseMonitorDiSL) super.clone();
			return ret;
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	FileWriter saved_fw;

	WeakReference Ref_f = null;

	static final int Prop_1_transition_write[] = {0, 3, 3, 3};;
	static final int Prop_1_transition_close[] = {2, 3, 2, 3};;
	static final int Prop_1_transition_endProg[] = {3, 3, 1, 3};;

	volatile boolean Prop_1_Category_fail = false;

	private final AtomicInteger pairValue;

	FileCloseMonitorDiSL() {
		this.pairValue = new AtomicInteger(this.calculatePairValue(-1, 0) ) ;

	}

	@Override public final int getState() {
		return this.getState(this.pairValue.get() ) ;
	}
	@Override public final int getLastEvent() {
		return this.getLastEvent(this.pairValue.get() ) ;
	}
	private final int getState(int pairValue) {
		return (pairValue & 3) ;
	}
	private final int getLastEvent(int pairValue) {
		return (pairValue >> 2) ;
	}
	private final int calculatePairValue(int lastEvent, int state) {
		return (((lastEvent + 1) << 2) | state) ;
	}

	private final int handleEvent(int eventId, int[] table) {
		int nextstate;
		while (true) {
			int oldpairvalue = this.pairValue.get() ;
			int oldstate = this.getState(oldpairvalue) ;
			nextstate = table [ oldstate ];
			int nextpairvalue = this.calculatePairValue(eventId, nextstate) ;
			if (this.pairValue.compareAndSet(oldpairvalue, nextpairvalue) ) {
				break;
			}
		}
		return nextstate;
	}

	final boolean Prop_1_event_write(FileWriter f) {
		{
			saved_fw = f;
		}
		if(Ref_f == null){
			Ref_f = new WeakReference(f);
		}

		int nextstate = this.handleEvent(0, Prop_1_transition_write) ;
		this.Prop_1_Category_fail = nextstate == 3;

		return true;
	}

	final boolean Prop_1_event_close(FileWriter f) {
		{
		}
		if(Ref_f == null){
			Ref_f = new WeakReference(f);
		}

		int nextstate = this.handleEvent(1, Prop_1_transition_close) ;
		this.Prop_1_Category_fail = nextstate == 3;

		return true;
	}

	final boolean Prop_1_event_endProg() {
		FileWriter f = null;
		if(Ref_f != null){
			f = (FileWriter)Ref_f.get();
		}
		{
			//System.out.println("Program has ended.");
		}

		int nextstate = this.handleEvent(2, Prop_1_transition_endProg) ;
		this.Prop_1_Category_fail = nextstate == 3;

		return true;
	}

	final void Prop_1_handler_fail (){
		{
			//System.err.println("You should close the file you wrote.");
			CounterClassDiSL.countJoinPoints("FileClose");

			try {
				saved_fw.close();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			this.reset();
		}

	}

	final void reset() {
		this.pairValue.set(this.calculatePairValue(-1, 0) ) ;

		Prop_1_Category_fail = false;
	}

	// RVMRef_f was suppressed to reduce memory overhead

	@Override
	protected final void terminateInternal(int idnum) {
		int lastEvent = this.getLastEvent();

		switch(idnum){
			case 0:
			break;
		}
		switch(lastEvent) {
			case -1:
			return;
			case 0:
			//write
			return;
			case 1:
			//close
			return;
			case 2:
			//endProg
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

public class FileCloseRuntimeMonitorDiSL implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
	private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager FileCloseMapManager;
	static {
		FileCloseMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
		FileCloseMapManager.start();
	}

	// Declarations for the Lock
	static final ReentrantLock FileClose_RVMLock = new ReentrantLock();
	static final Condition FileClose_RVMLock_cond = FileClose_RVMLock.newCondition();

	private static boolean FileClose_activated = false;

	// Declarations for Indexing Trees
	private static Object FileClose_f_Map_cachekey_f;
	private static FileCloseMonitorDiSL FileClose_f_Map_cachevalue;
	private static final MapOfMonitor<FileCloseMonitorDiSL> FileClose_f_Map = new MapOfMonitor<FileCloseMonitorDiSL>(0) ;
	private static final FileCloseMonitor_SetDiSL FileClose__Map = new FileCloseMonitor_SetDiSL() ;

	public static int cleanUp() {
		int collected = 0;
		// indexing trees
		collected += FileClose_f_Map.cleanUpUnnecessaryMappings();
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

	public static final void writeEvent(FileWriter f) {
		FileClose_activated = true;
		while (!FileClose_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_f = null;
		MapOfMonitor<FileCloseMonitorDiSL> matchedLastMap = null;
		FileCloseMonitorDiSL matchedEntry = null;
		boolean cachehit = false;
		if ((f == FileClose_f_Map_cachekey_f) ) {
			matchedEntry = FileClose_f_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_f = new CachedWeakReference(f) ;
			{
				// FindOrCreateEntry
				MapOfMonitor<FileCloseMonitorDiSL> itmdMap = FileClose_f_Map;
				matchedLastMap = itmdMap;
				FileCloseMonitorDiSL node_f = FileClose_f_Map.getNodeEquivalent(wr_f) ;
				matchedEntry = node_f;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_f == null) ) {
				wr_f = new CachedWeakReference(f) ;
			}
			// D(X) main:4
			FileCloseMonitorDiSL created = new FileCloseMonitorDiSL() ;
			matchedEntry = created;
			matchedLastMap.putNode(wr_f, created) ;
			// D(X) defineNew:5 for <>
			{
				// InsertMonitor
				FileCloseMonitor_SetDiSL targetSet = FileClose__Map;
				targetSet.add(created) ;
			}
		}
		// D(X) main:8--9
		final FileCloseMonitorDiSL matchedEntryfinalMonitor = matchedEntry;
		matchedEntry.Prop_1_event_write(f);
		if(matchedEntryfinalMonitor.Prop_1_Category_fail) {
			matchedEntryfinalMonitor.Prop_1_handler_fail();
		}

		if ((cachehit == false) ) {
			FileClose_f_Map_cachekey_f = f;
			FileClose_f_Map_cachevalue = matchedEntry;
		}

		FileClose_RVMLock.unlock();
	}

	public static final void closeEvent(FileWriter f) {
		while (!FileClose_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (FileClose_activated) {
			FileCloseMonitorDiSL matchedEntry = null;
			boolean cachehit = false;
			if ((f == FileClose_f_Map_cachekey_f) ) {
				matchedEntry = FileClose_f_Map_cachevalue;
				cachehit = true;
			}
			else {
				// FindEntry
				FileCloseMonitorDiSL node_f = FileClose_f_Map.getNodeWithStrongRef(f) ;
				matchedEntry = node_f;
			}
			// D(X) main:8--9
			if ((matchedEntry != null) ) {
				final FileCloseMonitorDiSL matchedEntryfinalMonitor = matchedEntry;
				matchedEntry.Prop_1_event_close(f);
				if(matchedEntryfinalMonitor.Prop_1_Category_fail) {
					matchedEntryfinalMonitor.Prop_1_handler_fail();
				}

				if ((cachehit == false) ) {
					FileClose_f_Map_cachekey_f = f;
					FileClose_f_Map_cachevalue = matchedEntry;
				}
			}

		}

		FileClose_RVMLock.unlock();
	}

	public static final void endProgEvent() {
		while (!FileClose_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (FileClose_activated) {
			FileCloseMonitor_SetDiSL matchedEntry = null;
			{
				// FindEntry
				matchedEntry = FileClose__Map;
			}
			// D(X) main:8--9
			if ((matchedEntry != null) ) {
				matchedEntry.event_endProg();

			}

		}

		FileClose_RVMLock.unlock();
	}

}

