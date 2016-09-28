package ch.usi.dag.javamop.fileclose;

import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import ch.usi.dag.dislre.AREDispatch;

import com.runtimeverification.rvmonitor.java.rt.RuntimeOption;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TerminatedMonitorCleaner;


final class FileCloseMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<FileCloseMonitor> {

	FileCloseMonitor_Set(){
		this.size = 0;
		this.elements = new FileCloseMonitor[4];
	}
	final void event_write(final FileWriter f) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final FileCloseMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final FileCloseMonitor monitorfinalMonitor = monitor;
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
	final void event_close(final FileWriter f) {
		int numAlive = 0 ;
		for(int i = 0; i < this.size; i++){
			final FileCloseMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final FileCloseMonitor monitorfinalMonitor = monitor;
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
			final FileCloseMonitor monitor = this.elements[i];
			if(!monitor.isTerminated()){
				elements[numAlive] = monitor;
				numAlive++;

				final FileCloseMonitor monitorfinalMonitor = monitor;
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

class FileCloseMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractAtomicMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject {
	@Override
    protected Object clone() {
		try {
			final FileCloseMonitor ret = (FileCloseMonitor) super.clone();
			return ret;
		}
		catch (final CloneNotSupportedException e) {
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

	FileCloseMonitor() {
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

	final boolean Prop_1_event_write(final FileWriter f) {
		{
			saved_fw = f;
		}
		if(Ref_f == null){
			Ref_f = new WeakReference(f);
		}

		final int nextstate = this.handleEvent(0, Prop_1_transition_write) ;
		this.Prop_1_Category_fail = nextstate == 3;

		return true;
	}

	final boolean Prop_1_event_close(final FileWriter f) {
		{
		}
		if(Ref_f == null){
			Ref_f = new WeakReference(f);
		}

		final int nextstate = this.handleEvent(1, Prop_1_transition_close) ;
		this.Prop_1_Category_fail = nextstate == 3;

		return true;
	}

	final boolean Prop_1_event_endProg() {
		FileWriter f = null;
		if(Ref_f != null){
			f = (FileWriter)Ref_f.get();
		}
		{
			System.out.println("Program has ended.");
			AREDispatch.NativeLog ("Program has ended.");
		}

		final int nextstate = this.handleEvent(2, Prop_1_transition_endProg) ;
		this.Prop_1_Category_fail = nextstate == 3;

		return true;
	}

	final void Prop_1_handler_fail (){
		{
			System.err.println("You should close the file you wrote.");
			AREDispatch.NativeLog ("You should close the file you wrote.");
			try {
				saved_fw.close();
			} catch (final Exception e) {
				System.err.println(e.getMessage());
				AREDispatch.NativeLog (e.getMessage ());
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
	protected final void terminateInternal(final int idnum) {
		final int lastEvent = this.getLastEvent();

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

public class FileCloseRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
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
	private static FileCloseMonitor FileClose_f_Map_cachevalue;
	private static final MapOfMonitor<FileCloseMonitor> FileClose_f_Map = new MapOfMonitor<FileCloseMonitor>(0) ;
	private static final FileCloseMonitor_Set FileClose__Map = new FileCloseMonitor_Set() ;

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

	public static final void writeEvent(final FileWriter f) {
		FileClose_activated = true;
		while (!FileClose_RVMLock.tryLock()) {
			Thread.yield();
		}

		CachedWeakReference wr_f = null;
		MapOfMonitor<FileCloseMonitor> matchedLastMap = null;
		FileCloseMonitor matchedEntry = null;
		boolean cachehit = false;
		if ((f == FileClose_f_Map_cachekey_f) ) {
			matchedEntry = FileClose_f_Map_cachevalue;
			cachehit = true;
		}
		else {
			wr_f = new CachedWeakReference(f) ;
			{
				// FindOrCreateEntry
				final MapOfMonitor<FileCloseMonitor> itmdMap = FileClose_f_Map;
				matchedLastMap = itmdMap;
				final FileCloseMonitor node_f = FileClose_f_Map.getNodeEquivalent(wr_f) ;
				matchedEntry = node_f;
			}
		}
		// D(X) main:1
		if ((matchedEntry == null) ) {
			if ((wr_f == null) ) {
				wr_f = new CachedWeakReference(f) ;
			}
			// D(X) main:4
			final FileCloseMonitor created = new FileCloseMonitor() ;
			matchedEntry = created;
			matchedLastMap.putNode(wr_f, created) ;
			// D(X) defineNew:5 for <>
			{
				// InsertMonitor
				final FileCloseMonitor_Set targetSet = FileClose__Map;
				targetSet.add(created) ;
			}
		}
		// D(X) main:8--9
		final FileCloseMonitor matchedEntryfinalMonitor = matchedEntry;
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

	public static final void closeEvent(final FileWriter f) {
		while (!FileClose_RVMLock.tryLock()) {
			Thread.yield();
		}

		if (FileClose_activated) {
			FileCloseMonitor matchedEntry = null;
			boolean cachehit = false;
			if ((f == FileClose_f_Map_cachekey_f) ) {
				matchedEntry = FileClose_f_Map_cachevalue;
				cachehit = true;
			}
			else {
				// FindEntry
				final FileCloseMonitor node_f = FileClose_f_Map.getNodeWithStrongRef(f) ;
				matchedEntry = node_f;
			}
			// D(X) main:8--9
			if ((matchedEntry != null) ) {
				final FileCloseMonitor matchedEntryfinalMonitor = matchedEntry;
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
			FileCloseMonitor_Set matchedEntry = null;
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

