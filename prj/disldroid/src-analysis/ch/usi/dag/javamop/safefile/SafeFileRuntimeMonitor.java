package ch.usi.dag.javamop.safefile;

import java.io.FileReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import ch.usi.dag.dislre.AREDispatch;

import com.runtimeverification.rvmonitor.java.rt.RuntimeOption;
import com.runtimeverification.rvmonitor.java.rt.ref.CachedWeakReference;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfMap;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfMonitor;
import com.runtimeverification.rvmonitor.java.rt.table.MapOfSetMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.DisableHolder;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IDisableHolder;
import com.runtimeverification.rvmonitor.java.rt.tablebase.IMonitor;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TableAdopter.Tuple2;
import com.runtimeverification.rvmonitor.java.rt.tablebase.TerminatedMonitorCleaner;


final class SafeFileMonitor_Set extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractMonitorSet<SafeFileMonitor> {

SafeFileMonitor_Set(){
this.size = 0;
this.elements = new SafeFileMonitor[4];
}
final void event_open(final Thread t, final FileReader f) {
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
final SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

final SafeFileMonitor monitorfinalMonitor = monitor;
monitor.Prop_1_event_open(t, f);
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
final void event_close(final FileReader f, final Thread t) {
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
final SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

final SafeFileMonitor monitorfinalMonitor = monitor;
monitor.Prop_1_event_close(f, t);
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
final void event_begin(final Thread t) {
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
final SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

final SafeFileMonitor monitorfinalMonitor = monitor;
monitor.Prop_1_event_begin(t);
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
final void event_end(final Thread t) {
int numAlive = 0 ;
for(int i = 0; i < this.size; i++){
final SafeFileMonitor monitor = this.elements[i];
if(!monitor.isTerminated()){
elements[numAlive] = monitor;
numAlive++;

final SafeFileMonitor monitorfinalMonitor = monitor;
monitor.Prop_1_event_end(t);
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

interface ISafeFileMonitor extends IMonitor, IDisableHolder {
}

class SafeFileDisableHolder extends DisableHolder implements ISafeFileMonitor {
SafeFileDisableHolder(final long tau) {
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

class SafeFileMonitor extends com.runtimeverification.rvmonitor.java.rt.tablebase.AbstractSynchronizedMonitor implements Cloneable, com.runtimeverification.rvmonitor.java.rt.RVMObject, ISafeFileMonitor {
@Override
protected Object clone() {
try {
final SafeFileMonitor ret = (SafeFileMonitor) super.clone();
ret.Prop_1_stacks = new ArrayList<IntStack>();
for(int Prop_1_i = 0; Prop_1_i < this.Prop_1_stacks.size(); Prop_1_i++){
final IntStack Prop_1_stack = this.Prop_1_stacks.get(Prop_1_i);
ret.Prop_1_stacks.add(Prop_1_stack.fclone());
}
return ret;
}
catch (final CloneNotSupportedException e) {
throw new InternalError(e.toString());
}
}


WeakReference Ref_f = null;
/* %%_%_CFG_%_%% */ArrayList<IntStack> Prop_1_stacks = new ArrayList<IntStack>();
static int[][] Prop_1_gt = { { 0, 26, -1,  }, { 0, 30, 28,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 15, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 13, -1,  }, { 0, 38, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 10, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 30, 24,  }, { 0, 32, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 47, -1,  }, { 0, 41, -1,  }, { 0, -1, -1,  }, { 0, 22, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, 51, -1,  }, { 0, 25, -1,  }, { 0, 18, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  }, { 0, -1, -1,  },  };
;
static int[][][][] Prop_1_at = { { { { 43,  },  }, { { 48,  },  }, { { 45,  },  }, {  },  }, { { { 20,  },  }, {  }, { { 4,  },  }, {  },  }, { { { 1, 2,  },  }, { { 1, 2,  },  }, { { 1, 2,  },  }, {  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, { { 1, 2,  },  },  }, { { { 44,  },  }, {  }, { { 31,  },  }, { { 49,  },  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 1, 4,  },  }, { { 1, 4,  },  }, { { 1, 4,  },  }, {  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 39,  },  }, { { 40,  },  }, { { 36,  },  }, {  },  }, { { { 44,  },  }, {  }, { { 31,  },  }, { { 6,  },  },  }, { { { 44,  },  }, {  }, { { 31,  },  }, { { 14,  },  },  }, { { { 37,  },  }, {  }, { { 11,  },  }, { { 16,  },  },  }, { { { 1, 2,  }, { 1, 3,  },  }, {  }, { { 1, 2,  }, { 1, 3,  },  }, {  },  }, { { { 37,  },  }, {  }, { { 11,  },  }, { { 23,  },  },  }, { { { 1, 4,  },  }, {  }, { { 1, 4,  },  }, { { 1, 4,  },  },  }, { { { 1, 3,  }, { 1, 4,  },  }, {  }, { { 1, 3,  }, { 1, 4,  },  }, {  },  }, { { { 37,  },  }, {  }, { { 11,  },  }, { { 35,  },  },  }, { { { 1, 4,  },  }, { { 1, 4,  },  }, { { 1, 4,  },  }, {  },  }, { { { 43,  },  }, { { 42,  },  }, { { 45,  },  }, {  },  }, { { { 1, 3,  }, { 1, 4,  },  }, {  }, { { 1, 3,  }, { 1, 4,  },  }, {  },  }, { { { 39,  },  }, { { 19,  },  }, { { 36,  },  }, {  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, {  },  }, { {  }, {  }, {  }, {  },  }, { { { 39,  },  }, { { 5,  },  }, { { 36,  },  }, {  },  }, { { { 39,  },  }, { { 17,  },  }, { { 36,  },  }, {  },  }, { { { 1, 2,  },  }, { { 1, 2,  },  }, { { 1, 2,  },  }, {  },  }, { {  }, {  }, {  }, {  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 0,  },  }, {  }, { { 12,  },  }, {  },  }, { { { 44,  },  }, {  }, { { 31,  },  }, { { 34,  },  },  }, { { { 37,  },  }, {  }, { { 11,  },  }, { { 33,  },  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, { { 1, 2,  },  },  }, { { { 1, 3,  },  }, { { 1, 3,  },  }, { { 1, 3,  },  }, {  },  }, { { { 44,  },  }, {  }, { { 31,  },  }, { { 8,  },  },  }, { { { 43,  },  }, { { 50,  },  }, { { 45,  },  }, {  },  }, { { { 37,  },  }, {  }, { { 11,  },  }, { { 21,  },  },  }, { { { 43,  },  }, { { 29,  },  }, { { 45,  },  }, {  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, {  },  }, { { { 39,  },  }, { { 46,  },  }, { { 36,  },  }, {  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, {  },  }, { { { 43,  },  }, { { 2,  },  }, { { 45,  },  }, {  },  }, { { { 43,  },  }, { { 3,  },  }, { { 45,  },  }, {  },  }, { { { 44,  },  }, {  }, { { 31,  },  }, { { 27,  },  },  }, { { { 1, 4,  },  }, {  }, { { 1, 4,  },  }, { { 1, 4,  },  },  }, { { { 37,  },  }, {  }, { { 11,  },  }, { { 7,  },  },  }, { { { 1, 2,  }, { 1, 3,  },  }, {  }, { { 1, 2,  }, { 1, 3,  },  }, {  },  }, { { { 1, 2,  },  }, {  }, { { 1, 2,  },  }, {  },  }, { { { 1, 3,  },  }, {  }, { { 1, 3,  },  }, { { 1, 3,  },  },  }, { { { 39,  },  }, { { 9,  },  }, { { 36,  },  }, {  },  },  };
;
static boolean[] Prop_1_acc = { false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, true, false, false, false, true, false, true, true, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, true, true, false, false, };
int Prop_1_cat; // ACCEPT = 0, UNKNOWN = 1, FAIL = 2
int Prop_1_event = -1;

class IntStack implements java.io.Serializable {
    int[] data;
    int curr_index = 0;
    public IntStack(){
        data = new int[32];
    }
    @Override
    public String toString(){
        String ret = "[";
        for (int i = curr_index; i>=0; i--){
            ret += Integer.toString(data[i])+",";
        }
        return ret+"]";
    }
    @Override
    public int hashCode() {
        return curr_index^peek();
    }
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof IntStack)) {
            return false;
        }
        final IntStack s = (IntStack)o;
        if(curr_index != s.curr_index) {
            return false;
        }
        for(int i = 0; i < curr_index; i++){
            if(data[i] != s.data[i]) {
                return false;
            }
        }
        return true;
    }
    public IntStack(final int size){
        data = new int[size];
    }
    public int peek(){
        return data[curr_index - 1];
    }
    public int pop(){
        return data[--curr_index];
    }
    public void pop(final int num){
        curr_index -= num;
    }
    public void push(final int datum){
        if(curr_index < data.length){
            data[curr_index++] = datum;
        } else{
            final int len = data.length;
            final int[] old = data;
            data = new int[len * 2];
            for(int i = 0; i < len; ++i){
                data[i] = old[i];
            }
            data[curr_index++] = datum;
        }
    }
    public IntStack fclone(){
        final IntStack ret = new IntStack(data.length);
        ret.curr_index = curr_index;
        for(int i = 0; i < curr_index; ++i){
            ret.data[i] = data[i];
        }
        return ret;
    }
    public void clear(){
        curr_index = 0;
    }
}

boolean Prop_1_Category_fail = false;

SafeFileMonitor(final long tau) {
this.tau = tau;
final IntStack stack = new IntStack();
stack.push(-2);
stack.push(1);
Prop_1_stacks.add(stack);

}

@Override
public final int getState() {
return -1;
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

final boolean Prop_1_event_open(final Thread t, final FileReader f) {
{
	}
if(Ref_f == null){
Ref_f = new WeakReference(f);
}
RVM_lastevent = 0;

Prop_1_event = 1;
if (Prop_1_cat != 2) {
    Prop_1_event--;
    Prop_1_cat = 1;
    for (int Prop_1_i = Prop_1_stacks.size()-1; Prop_1_i >=0; Prop_1_i--) {
        IntStack stack = Prop_1_stacks.get(Prop_1_i);
        Prop_1_stacks.set(Prop_1_i,null);
        while (stack != null) {
            final int s = stack.peek();
            if (s >= 0 && Prop_1_at[s][Prop_1_event].length >= 0) {
                /* not in an error state and something to do? */
                for (int j = 0; j < Prop_1_at[s][Prop_1_event].length; j++) {
                    IntStack tstack;
                    if (Prop_1_at[s][Prop_1_event].length > 1){
                        tstack = stack.fclone();
                    } else{
                        tstack = stack;
                    }
                    switch (Prop_1_at[s][Prop_1_event][j].length) {
                        case 1:/* Shift */
                            tstack.push(Prop_1_at[s][Prop_1_event][j][0]);
                            Prop_1_stacks.add(tstack);
                            if (Prop_1_acc[Prop_1_at[s][Prop_1_event][j][0]]) {
                                Prop_1_cat = 0;
                            }
                            break;
                        case 2: /* Reduce */
                            tstack.pop(Prop_1_at[s][Prop_1_event][j][1]);
                            final int Prop_1_old = tstack.peek();
                            tstack.push(Prop_1_gt[Prop_1_old][Prop_1_at[s][Prop_1_event][j][0]]);
                            Prop_1_stacks.add(Prop_1_i,tstack);
                            break;
                    }
                }
            }
            stack = Prop_1_stacks.get(Prop_1_i);
            Prop_1_stacks.remove(Prop_1_i);
        }
    }
    if (Prop_1_stacks.isEmpty()) {
        Prop_1_cat = 2;
    }
};
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final boolean Prop_1_event_close(final FileReader f, final Thread t) {
{
	}
if(Ref_f == null){
Ref_f = new WeakReference(f);
}
RVM_lastevent = 1;

Prop_1_event = 2;
if (Prop_1_cat != 2) {
    Prop_1_event--;
    Prop_1_cat = 1;
    for (int Prop_1_i = Prop_1_stacks.size()-1; Prop_1_i >=0; Prop_1_i--) {
        IntStack stack = Prop_1_stacks.get(Prop_1_i);
        Prop_1_stacks.set(Prop_1_i,null);
        while (stack != null) {
            final int s = stack.peek();
            if (s >= 0 && Prop_1_at[s][Prop_1_event].length >= 0) {
                /* not in an error state and something to do? */
                for (int j = 0; j < Prop_1_at[s][Prop_1_event].length; j++) {
                    IntStack tstack;
                    if (Prop_1_at[s][Prop_1_event].length > 1){
                        tstack = stack.fclone();
                    } else{
                        tstack = stack;
                    }
                    switch (Prop_1_at[s][Prop_1_event][j].length) {
                        case 1:/* Shift */
                            tstack.push(Prop_1_at[s][Prop_1_event][j][0]);
                            Prop_1_stacks.add(tstack);
                            if (Prop_1_acc[Prop_1_at[s][Prop_1_event][j][0]]) {
                                Prop_1_cat = 0;
                            }
                            break;
                        case 2: /* Reduce */
                            tstack.pop(Prop_1_at[s][Prop_1_event][j][1]);
                            final int Prop_1_old = tstack.peek();
                            tstack.push(Prop_1_gt[Prop_1_old][Prop_1_at[s][Prop_1_event][j][0]]);
                            Prop_1_stacks.add(Prop_1_i,tstack);
                            break;
                    }
                }
            }
            stack = Prop_1_stacks.get(Prop_1_i);
            Prop_1_stacks.remove(Prop_1_i);
        }
    }
    if (Prop_1_stacks.isEmpty()) {
        Prop_1_cat = 2;
    }
};
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final boolean Prop_1_event_begin(final Thread t) {
FileReader f = null;
if(Ref_f != null){
f = (FileReader)Ref_f.get();
}
{
	}
RVM_lastevent = 2;

Prop_1_event = 3;
if (Prop_1_cat != 2) {
    Prop_1_event--;
    Prop_1_cat = 1;
    for (int Prop_1_i = Prop_1_stacks.size()-1; Prop_1_i >=0; Prop_1_i--) {
        IntStack stack = Prop_1_stacks.get(Prop_1_i);
        Prop_1_stacks.set(Prop_1_i,null);
        while (stack != null) {
            final int s = stack.peek();
            if (s >= 0 && Prop_1_at[s][Prop_1_event].length >= 0) {
                /* not in an error state and something to do? */
                for (int j = 0; j < Prop_1_at[s][Prop_1_event].length; j++) {
                    IntStack tstack;
                    if (Prop_1_at[s][Prop_1_event].length > 1){
                        tstack = stack.fclone();
                    } else{
                        tstack = stack;
                    }
                    switch (Prop_1_at[s][Prop_1_event][j].length) {
                        case 1:/* Shift */
                            tstack.push(Prop_1_at[s][Prop_1_event][j][0]);
                            Prop_1_stacks.add(tstack);
                            if (Prop_1_acc[Prop_1_at[s][Prop_1_event][j][0]]) {
                                Prop_1_cat = 0;
                            }
                            break;
                        case 2: /* Reduce */
                            tstack.pop(Prop_1_at[s][Prop_1_event][j][1]);
                            final int Prop_1_old = tstack.peek();
                            tstack.push(Prop_1_gt[Prop_1_old][Prop_1_at[s][Prop_1_event][j][0]]);
                            Prop_1_stacks.add(Prop_1_i,tstack);
                            break;
                    }
                }
            }
            stack = Prop_1_stacks.get(Prop_1_i);
            Prop_1_stacks.remove(Prop_1_i);
        }
    }
    if (Prop_1_stacks.isEmpty()) {
        Prop_1_cat = 2;
    }
};
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final boolean Prop_1_event_end(final Thread t) {
FileReader f = null;
if(Ref_f != null){
f = (FileReader)Ref_f.get();
}
{
	}
RVM_lastevent = 3;

Prop_1_event = 4;
if (Prop_1_cat != 2) {
    Prop_1_event--;
    Prop_1_cat = 1;
    for (int Prop_1_i = Prop_1_stacks.size()-1; Prop_1_i >=0; Prop_1_i--) {
        IntStack stack = Prop_1_stacks.get(Prop_1_i);
        Prop_1_stacks.set(Prop_1_i,null);
        while (stack != null) {
            final int s = stack.peek();
            if (s >= 0 && Prop_1_at[s][Prop_1_event].length >= 0) {
                /* not in an error state and something to do? */
                for (int j = 0; j < Prop_1_at[s][Prop_1_event].length; j++) {
                    IntStack tstack;
                    if (Prop_1_at[s][Prop_1_event].length > 1){
                        tstack = stack.fclone();
                    } else{
                        tstack = stack;
                    }
                    switch (Prop_1_at[s][Prop_1_event][j].length) {
                        case 1:/* Shift */
                            tstack.push(Prop_1_at[s][Prop_1_event][j][0]);
                            Prop_1_stacks.add(tstack);
                            if (Prop_1_acc[Prop_1_at[s][Prop_1_event][j][0]]) {
                                Prop_1_cat = 0;
                            }
                            break;
                        case 2: /* Reduce */
                            tstack.pop(Prop_1_at[s][Prop_1_event][j][1]);
                            final int Prop_1_old = tstack.peek();
                            tstack.push(Prop_1_gt[Prop_1_old][Prop_1_at[s][Prop_1_event][j][0]]);
                            Prop_1_stacks.add(Prop_1_i,tstack);
                            break;
                    }
                }
            }
            stack = Prop_1_stacks.get(Prop_1_i);
            Prop_1_stacks.remove(Prop_1_i);
        }
    }
    if (Prop_1_stacks.isEmpty()) {
        Prop_1_cat = 2;
    }
};
Prop_1_Category_fail = Prop_1_cat == 2;
return true;
}

final void Prop_1_handler_fail (){
{
		System.out.println("improper use of files");
		AREDispatch.NativeLog ("improper use of files");
	}

}

final void reset() {
RVM_lastevent = -1;
Prop_1_stacks.clear();
final IntStack stack = new IntStack();
stack.push(-2);
stack.push(1);
Prop_1_stacks.add(stack);
Prop_1_Category_fail = false;
}

@Override
public final int hashCode() {
if(Prop_1_stacks.size() == 0) {
    return 0;
}
return Prop_1_stacks.size() ^ Prop_1_stacks.get(Prop_1_stacks.size() - 1).hashCode();
}

@Override
public final boolean equals(final Object o) {
if(o == null) {
    return false;
}
if(! (o instanceof SafeFileMonitor)) {
    return false ;
}
final SafeFileMonitor m = (SafeFileMonitor) o;
if (Prop_1_stacks.size() != m.Prop_1_stacks.size()) {
    return false;
}
for(int Prop_1_i = 0; Prop_1_i < Prop_1_stacks.size(); Prop_1_i++){
final IntStack Prop_1_stack = Prop_1_stacks.get(Prop_1_i);
final IntStack Prop_1_stack2 = m.Prop_1_stacks.get(Prop_1_i);
if(Prop_1_stack.curr_index != Prop_1_stack2.curr_index) {
    return false;
}
for(int Prop_1_j = 0; Prop_1_j < Prop_1_stack.curr_index; Prop_1_j++){
if(Prop_1_stack.data[Prop_1_j] != Prop_1_stack2.data[Prop_1_j]) {
    return false;
}
}
}
return true;
}

// RVMRef_f was suppressed to reduce memory overhead
// RVMRef_t was suppressed to reduce memory overhead


@Override
protected final void terminateInternal(final int idnum) {
switch(idnum){
case 0:
break;
case 1:
break;
}
switch(RVM_lastevent) {
case -1:
return;
case 0:
//open
return;
case 1:
//close
return;
case 2:
//begin
return;
case 3:
//end
return;
}
return;
}

public static int getNumberOfEvents() {
return 4;
}

public static int getNumberOfStates() {
return 0;
}

}

public class SafeFileRuntimeMonitor implements com.runtimeverification.rvmonitor.java.rt.RVMObject {
private static com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager SafeFileMapManager;
static {
SafeFileMapManager = new com.runtimeverification.rvmonitor.java.rt.map.RVMMapManager();
SafeFileMapManager.start();
}

// Declarations for the Lock
static final ReentrantLock SafeFile_RVMLock = new ReentrantLock();
static final Condition SafeFile_RVMLock_cond = SafeFile_RVMLock.newCondition();

// Declarations for Timestamps
private static long SafeFile_timestamp = 1;

private static boolean SafeFile_activated = false;

// Declarations for Indexing Trees
private static Object SafeFile_f_t_Map_cachekey_f;
private static Object SafeFile_f_t_Map_cachekey_t;
private static SafeFileMonitor SafeFile_f_t_Map_cachevalue;
private static Object SafeFile_t_Map_cachekey_t;
private static Tuple2<SafeFileMonitor_Set, SafeFileMonitor> SafeFile_t_Map_cachevalue;
private static final MapOfMap<MapOfMonitor<SafeFileMonitor>> SafeFile_f_t_Map = new MapOfMap<MapOfMonitor<SafeFileMonitor>>(0) ;
private static final MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor> SafeFile_t_Map = new MapOfSetMonitor<SafeFileMonitor_Set, SafeFileMonitor>(1) ;


public static int cleanUp() {
int collected = 0;
// indexing trees
collected += SafeFile_f_t_Map.cleanUpUnnecessaryMappings();
collected += SafeFile_t_Map.cleanUpUnnecessaryMappings();
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

public static final void openEvent(final Thread t, final FileReader f) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}

CachedWeakReference wr_t = null;
CachedWeakReference wr_f = null;
MapOfMonitor<SafeFileMonitor> matchedLastMap = null;
SafeFileMonitor matchedEntry = null;
boolean cachehit = false;
if (((f == SafeFile_f_t_Map_cachekey_f) && (t == SafeFile_f_t_Map_cachekey_t) ) ) {
	matchedEntry = SafeFile_f_t_Map_cachevalue;
	cachehit = true;
}
else {
	wr_f = new CachedWeakReference(f) ;
	wr_t = new CachedWeakReference(t) ;
	{
		// FindOrCreateEntry
		MapOfMonitor<SafeFileMonitor> node_f = SafeFile_f_t_Map.getNodeEquivalent(wr_f) ;
		if ((node_f == null) ) {
			node_f = new MapOfMonitor<SafeFileMonitor>(1) ;
			SafeFile_f_t_Map.putNode(wr_f, node_f) ;
		}
		matchedLastMap = node_f;
		final SafeFileMonitor node_f_t = node_f.getNodeEquivalent(wr_t) ;
		matchedEntry = node_f_t;
	}
}
// D(X) main:1
if ((matchedEntry == null) ) {
	if ((wr_f == null) ) {
		wr_f = new CachedWeakReference(f) ;
	}
	if ((wr_t == null) ) {
		wr_t = new CachedWeakReference(t) ;
	}
	{
		// D(X) createNewMonitorStates:4 when Dom(theta'') = <t>
		SafeFileMonitor sourceLeaf = null;
		{
			// FindCode
			final Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
			if ((node_t != null) ) {
				final SafeFileMonitor itmdLeaf = node_t.getValue2() ;
				sourceLeaf = itmdLeaf;
			}
		}
		if ((sourceLeaf != null) ) {
			boolean definable = true;
			// D(X) defineTo:1--5 for <f, t>
			if (definable) {
				// FindCode
				final MapOfMonitor<SafeFileMonitor> node_f = SafeFile_f_t_Map.getNodeEquivalent(wr_f) ;
				if ((node_f != null) ) {
					final SafeFileMonitor node_f_t = node_f.getNodeEquivalent(wr_t) ;
					if ((node_f_t != null) ) {
						if (((node_f_t.getDisable() > sourceLeaf.getTau() ) || ((node_f_t.getTau() > 0) && (node_f_t.getTau() < sourceLeaf.getTau() ) ) ) ) {
							definable = false;
						}
					}
				}
			}
			if (definable) {
				// D(X) defineTo:6
				final SafeFileMonitor created = (SafeFileMonitor)sourceLeaf.clone() ;
				matchedEntry = created;
				matchedLastMap.putNode(wr_t, created) ;
				// D(X) defineTo:7 for <t>
				{
					// InsertMonitor
					Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
					if ((node_t == null) ) {
						node_t = new Tuple2<SafeFileMonitor_Set, SafeFileMonitor>() ;
						SafeFile_t_Map.putNode(wr_t, node_t) ;
						node_t.setValue1(new SafeFileMonitor_Set() ) ;
					}
					final SafeFileMonitor_Set targetSet = node_t.getValue1() ;
					targetSet.add(created) ;
				}
			}
		}
	}
	if ((matchedEntry == null) ) {
		// D(X) main:4
		final SafeFileMonitor created = new SafeFileMonitor(SafeFile_timestamp++) ;
		matchedEntry = created;
		matchedLastMap.putNode(wr_t, created) ;
		// D(X) defineNew:5 for <t>
		{
			// InsertMonitor
			Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
			if ((node_t == null) ) {
				node_t = new Tuple2<SafeFileMonitor_Set, SafeFileMonitor>() ;
				SafeFile_t_Map.putNode(wr_t, node_t) ;
				node_t.setValue1(new SafeFileMonitor_Set() ) ;
			}
			final SafeFileMonitor_Set targetSet = node_t.getValue1() ;
			targetSet.add(created) ;
		}
	}
	// D(X) main:6
	matchedEntry.setDisable(SafeFile_timestamp++) ;
}
// D(X) main:8--9
final SafeFileMonitor matchedEntryfinalMonitor = matchedEntry;
matchedEntry.Prop_1_event_open(t, f);
if(matchedEntryfinalMonitor.Prop_1_Category_fail) {
matchedEntryfinalMonitor.Prop_1_handler_fail();
}

if ((cachehit == false) ) {
	SafeFile_f_t_Map_cachekey_f = f;
	SafeFile_f_t_Map_cachekey_t = t;
	SafeFile_f_t_Map_cachevalue = matchedEntry;
}


SafeFile_RVMLock.unlock();
}

public static final void closeEvent(final FileReader f, final Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}

CachedWeakReference wr_t = null;
CachedWeakReference wr_f = null;
MapOfMonitor<SafeFileMonitor> matchedLastMap = null;
SafeFileMonitor matchedEntry = null;
boolean cachehit = false;
if (((f == SafeFile_f_t_Map_cachekey_f) && (t == SafeFile_f_t_Map_cachekey_t) ) ) {
	matchedEntry = SafeFile_f_t_Map_cachevalue;
	cachehit = true;
}
else {
	wr_f = new CachedWeakReference(f) ;
	wr_t = new CachedWeakReference(t) ;
	{
		// FindOrCreateEntry
		MapOfMonitor<SafeFileMonitor> node_f = SafeFile_f_t_Map.getNodeEquivalent(wr_f) ;
		if ((node_f == null) ) {
			node_f = new MapOfMonitor<SafeFileMonitor>(1) ;
			SafeFile_f_t_Map.putNode(wr_f, node_f) ;
		}
		matchedLastMap = node_f;
		final SafeFileMonitor node_f_t = node_f.getNodeEquivalent(wr_t) ;
		matchedEntry = node_f_t;
	}
}
// D(X) main:1
if ((matchedEntry == null) ) {
	if ((wr_f == null) ) {
		wr_f = new CachedWeakReference(f) ;
	}
	if ((wr_t == null) ) {
		wr_t = new CachedWeakReference(t) ;
	}
	{
		// D(X) createNewMonitorStates:4 when Dom(theta'') = <t>
		SafeFileMonitor sourceLeaf = null;
		{
			// FindCode
			final Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
			if ((node_t != null) ) {
				final SafeFileMonitor itmdLeaf = node_t.getValue2() ;
				sourceLeaf = itmdLeaf;
			}
		}
		if ((sourceLeaf != null) ) {
			boolean definable = true;
			// D(X) defineTo:1--5 for <f, t>
			if (definable) {
				// FindCode
				final MapOfMonitor<SafeFileMonitor> node_f = SafeFile_f_t_Map.getNodeEquivalent(wr_f) ;
				if ((node_f != null) ) {
					final SafeFileMonitor node_f_t = node_f.getNodeEquivalent(wr_t) ;
					if ((node_f_t != null) ) {
						if (((node_f_t.getDisable() > sourceLeaf.getTau() ) || ((node_f_t.getTau() > 0) && (node_f_t.getTau() < sourceLeaf.getTau() ) ) ) ) {
							definable = false;
						}
					}
				}
			}
			if (definable) {
				// D(X) defineTo:6
				final SafeFileMonitor created = (SafeFileMonitor)sourceLeaf.clone() ;
				matchedEntry = created;
				matchedLastMap.putNode(wr_t, created) ;
				// D(X) defineTo:7 for <t>
				{
					// InsertMonitor
					Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
					if ((node_t == null) ) {
						node_t = new Tuple2<SafeFileMonitor_Set, SafeFileMonitor>() ;
						SafeFile_t_Map.putNode(wr_t, node_t) ;
						node_t.setValue1(new SafeFileMonitor_Set() ) ;
					}
					final SafeFileMonitor_Set targetSet = node_t.getValue1() ;
					targetSet.add(created) ;
				}
			}
		}
	}
	if ((matchedEntry == null) ) {
		// D(X) main:4
		final SafeFileMonitor created = new SafeFileMonitor(SafeFile_timestamp++) ;
		matchedEntry = created;
		matchedLastMap.putNode(wr_t, created) ;
		// D(X) defineNew:5 for <t>
		{
			// InsertMonitor
			Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
			if ((node_t == null) ) {
				node_t = new Tuple2<SafeFileMonitor_Set, SafeFileMonitor>() ;
				SafeFile_t_Map.putNode(wr_t, node_t) ;
				node_t.setValue1(new SafeFileMonitor_Set() ) ;
			}
			final SafeFileMonitor_Set targetSet = node_t.getValue1() ;
			targetSet.add(created) ;
		}
	}
	// D(X) main:6
	matchedEntry.setDisable(SafeFile_timestamp++) ;
}
// D(X) main:8--9
final SafeFileMonitor matchedEntryfinalMonitor = matchedEntry;
matchedEntry.Prop_1_event_close(f, t);
if(matchedEntryfinalMonitor.Prop_1_Category_fail) {
matchedEntryfinalMonitor.Prop_1_handler_fail();
}

if ((cachehit == false) ) {
	SafeFile_f_t_Map_cachekey_f = f;
	SafeFile_f_t_Map_cachekey_t = t;
	SafeFile_f_t_Map_cachevalue = matchedEntry;
}


SafeFile_RVMLock.unlock();
}

public static final void beginEvent(final Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}

CachedWeakReference wr_t = null;
Tuple2<SafeFileMonitor_Set, SafeFileMonitor> matchedEntry = null;
boolean cachehit = false;
if ((t == SafeFile_t_Map_cachekey_t) ) {
	matchedEntry = SafeFile_t_Map_cachevalue;
	cachehit = true;
}
else {
	wr_t = new CachedWeakReference(t) ;
	{
		// FindOrCreateEntry
		Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
		if ((node_t == null) ) {
			node_t = new Tuple2<SafeFileMonitor_Set, SafeFileMonitor>() ;
			SafeFile_t_Map.putNode(wr_t, node_t) ;
			node_t.setValue1(new SafeFileMonitor_Set() ) ;
		}
		matchedEntry = node_t;
	}
}
// D(X) main:1
final SafeFileMonitor matchedLeaf = matchedEntry.getValue2() ;
if ((matchedLeaf == null) ) {
	if ((wr_t == null) ) {
		wr_t = new CachedWeakReference(t) ;
	}
	if ((matchedLeaf == null) ) {
		// D(X) main:4
		final SafeFileMonitor created = new SafeFileMonitor(SafeFile_timestamp++) ;
		matchedEntry.setValue2(created) ;
		final SafeFileMonitor_Set enclosingSet = matchedEntry.getValue1() ;
		enclosingSet.add(created) ;
	}
	// D(X) main:6
	final SafeFileMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
	disableUpdatedLeaf.setDisable(SafeFile_timestamp++) ;
}
// D(X) main:8--9
final SafeFileMonitor_Set stateTransitionedSet = matchedEntry.getValue1() ;
stateTransitionedSet.event_begin(t);

if ((cachehit == false) ) {
	SafeFile_t_Map_cachekey_t = t;
	SafeFile_t_Map_cachevalue = matchedEntry;
}


SafeFile_RVMLock.unlock();
}

public static final void endEvent(final Thread t) {
SafeFile_activated = true;
while (!SafeFile_RVMLock.tryLock()) {
Thread.yield();
}

CachedWeakReference wr_t = null;
Tuple2<SafeFileMonitor_Set, SafeFileMonitor> matchedEntry = null;
boolean cachehit = false;
if ((t == SafeFile_t_Map_cachekey_t) ) {
	matchedEntry = SafeFile_t_Map_cachevalue;
	cachehit = true;
}
else {
	wr_t = new CachedWeakReference(t) ;
	{
		// FindOrCreateEntry
		Tuple2<SafeFileMonitor_Set, SafeFileMonitor> node_t = SafeFile_t_Map.getNodeEquivalent(wr_t) ;
		if ((node_t == null) ) {
			node_t = new Tuple2<SafeFileMonitor_Set, SafeFileMonitor>() ;
			SafeFile_t_Map.putNode(wr_t, node_t) ;
			node_t.setValue1(new SafeFileMonitor_Set() ) ;
		}
		matchedEntry = node_t;
	}
}
// D(X) main:1
final SafeFileMonitor matchedLeaf = matchedEntry.getValue2() ;
if ((matchedLeaf == null) ) {
	if ((wr_t == null) ) {
		wr_t = new CachedWeakReference(t) ;
	}
	if ((matchedLeaf == null) ) {
		// D(X) main:4
		final SafeFileMonitor created = new SafeFileMonitor(SafeFile_timestamp++) ;
		matchedEntry.setValue2(created) ;
		final SafeFileMonitor_Set enclosingSet = matchedEntry.getValue1() ;
		enclosingSet.add(created) ;
	}
	// D(X) main:6
	final SafeFileMonitor disableUpdatedLeaf = matchedEntry.getValue2() ;
	disableUpdatedLeaf.setDisable(SafeFile_timestamp++) ;
}
// D(X) main:8--9
final SafeFileMonitor_Set stateTransitionedSet = matchedEntry.getValue1() ;
stateTransitionedSet.event_end(t);

if ((cachehit == false) ) {
	SafeFile_t_Map_cachekey_t = t;
	SafeFile_t_Map_cachevalue = matchedEntry;
}


SafeFile_RVMLock.unlock();
}

}

