package PCL;
/**
 * Java Interface for PCL - The Performance Counter Library (http://www.fz-juelich.de/zam/PCL/)
 * 
 * @author Rudolf Berrendorf (rudolf.berrendorf@fh-bonn-rhein-sieg.de)
 * @version PCL version 2.2
 *
 * PCL is an interface to access hardware performance counters on a variety of
 * microprocessors and system:
 *  -  UltraSPARC I/II/III under Solaris 2.x
 *  -  PowerPC 604, 604e, Power3, Power3-II under AIX >= 4.3
 *  -  MIPS R10000/R12000 under IRIX 6.x
 *  -  DEC Alpha 21164 under Compaq/DEC Tru64 4.0x (formerly Digital Unix)
 *     and SGI/CRAY Unicos (Cray T3E)
 *  -  Intel Pentium/Pentium MMX/PentiumPro/Pentium II/Pentium III under Linux 2.x.x
 *  - AMD Athlon
 *
 *
 *                  PCL - The Performance Counter Library
 *
 *
 *   Copyright (C) 2002
 *   University of Applied Sciences Bonn-Rhein-Sieg
 *
 *   Copyright (C) 1999, 2000
 *   Forschungszentrum Juelich GmbH, Federal Republic of Germany. All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *     - Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     - Any publications that result from the use of this software shall
 *       reasonably refer to the Research Centre's development.
 *
 *     - All advertising materials mentioning features or use of this software
 *       must display the following acknowledgement:
 *
 *           This product includes software developed by Forschungszentrum
 *           Juelich GmbH, Federal Republic of Germany.
 *
 *     - Forschungszentrum Juelich GmbH is not obligated to provide the user with
 *       any support, consulting, training or assistance of any kind with regard
 *       to the use, operation and performance of this software or to provide
 *       the user with any updates, revisions or new versions.
 *
 *
 *   THIS SOFTWARE IS PROVIDED BY FORSCHUNGSZENTRUM JUELICH GMBH "AS IS" AND ANY
 *   EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL FORSCHUNGSZENTRUM JUELICH GMBH BE LIABLE FOR
 *   ANY SPECIAL, DIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER
 *   RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF
 *   CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 *   CONNECTION WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
public class PCL
{

    //------------------------------------------------------------------------
    // some maximum values

    /**
     * total number of supported events
     */
    public static final int PCL_MAX_EVENT = 61;
    /**
     * maximum number of events per call
     */
    public static final int PCL_MAX_EVENT_PER_CALL = 16;
    /**
     * maximum nesting level of calls to PCLread
     */
    public static final int PCL_MAX_NESTING_LEVEL = 16;

    /**
     * maximum number of performance counters
     */
    public static final int PCL_COUNTER_MAX = 19;


    //------------------------------------------------------------------------
    // error codes

    /**
     * error code: successful operation
     */
    public static final int PCL_SUCCESS = 0;
    /*
     * error code: event not supported
     */
    public static final int PCL_NOT_SUPPORTED = -1;
    /**
     * error code: not all requested events supported
     */
    public static final int PCL_TOO_MANY_EVENTS = -2;
    /**
     * error code: too many nestings of calls
     */
    public static final int PCL_TOO_MANY_NESTINGS = -3;
    /**
     * error code: illegal call nesting
     */
    public static final int PCL_ILL_NESTING = -4;
    /**
     * error code: illegal event identifier
     */
    public static final int PCL_ILL_EVENT = -5;
    /**
     * error code: count mode not supported
     */
    public static final int PCL_MODE_NOT_SUPPORTED = -6;
    /**
     * error code: something else
     */
    public static final int PCL_FAILURE = -7;


    //------------------------------------------------------------------------
    // count modes

    /**
     * count mode: count in user mode only
     */
    public static final int PCL_MODE_USER = 1;
    /**
     * count mode: count in system mode only
     */
    public static final int PCL_MODE_SYSTEM = 2;
    /**
     * count mode: count in user and system mode
     */
    public static final int PCL_MODE_USER_SYSTEM = 3;


    //------------------------------------------------------------------------
    // supported events

    // L1 cache

    /**
     * event type: L1 cache read
     */
    public static final int PCL_L1CACHE_READ = 0;
    /**
     * event type: L1 cache write
     */
    public static final int PCL_L1CACHE_WRITE = 1;
    /**
     * event type: L1 cache read or write
     */
    public static final int PCL_L1CACHE_READWRITE = 2;
    /**
     * event type: L1 cache hit
     */
    public static final int PCL_L1CACHE_HIT = 3;
    /**
     * event type: L1 cache miss
     */
    public static final int PCL_L1CACHE_MISS = 4;


    // L1 data cache

    /**
     * event type: L1 data cache read
     */
    public static final int PCL_L1DCACHE_READ = 5;
    /**
     * event type: L1 data cache write
     */
    public static final int PCL_L1DCACHE_WRITE = 6;
    /**
     * event type: L1 data cache read or write
     */
    public static final int PCL_L1DCACHE_READWRITE = 7;
    /**
     * event type: L1 data cache hit
     */
    public static final int PCL_L1DCACHE_HIT = 8;
    /**
     * event type: L1 data cache miss
     */
    public static final int PCL_L1DCACHE_MISS = 9;


    // Level-1-Instruction-Cache

    /**
     * event type: L1 instruction cache read
     */
    public static final int PCL_L1ICACHE_READ = 10;
    /**
     * event type: L1 instruction cache write
     */
    public static final int PCL_L1ICACHE_WRITE = 11;
    /**
     * event type: L1 instruction cache read or write
     */
    public static final int PCL_L1ICACHE_READWRITE = 12;
    /**
     * event type: L1 instruction cache hit
     */
    public static final int PCL_L1ICACHE_HIT = 13;
    /**
     * event type: L1 instruction cache miss
     */
    public static final int PCL_L1ICACHE_MISS = 14;


    // Level-2-Cache

    /**
     * event type: L2 cache read
     */
    public static final int PCL_L2CACHE_READ = 15;
    /**
     * event type: L2 cache write
     */
    public static final int PCL_L2CACHE_WRITE = 16;
    /**
     * event type: L2 cache read or write
     */
    public static final int PCL_L2CACHE_READWRITE = 17;
    /**
     * event type: L2 cache hit
     */
    public static final int PCL_L2CACHE_HIT = 18;
    /**
     * event type: L2 cache miss
     */
    public static final int PCL_L2CACHE_MISS = 19;


    // Level-2-Data-Cache

    /**
     * event type: L2 data cache read
     */
    public static final int PCL_L2DCACHE_READ = 20;
    /**
     * event type: L2 data cache write
     */
    public static final int PCL_L2DCACHE_WRITE = 21;
    /**
     * event type: L2 data cache read or write
     */
    public static final int PCL_L2DCACHE_READWRITE = 22;
    /**
     * event type: L2 data cache hit
     */
    public static final int PCL_L2DCACHE_HIT = 23;
    /**
     * event type: L2 data cache miss
     */
    public static final int PCL_L2DCACHE_MISS = 24;


    // Level-2-Instruction-Cache

    /**
     * event type: L2 instruction cache read
     */
    public static final int PCL_L2ICACHE_READ = 25;
    /**
     * event type: L2 instruction cache write
     */
    public static final int PCL_L2ICACHE_WRITE = 26;
    /**
     * event type: L2 instruction cache read or write
     */
    public static final int PCL_L2ICACHE_READWRITE = 27;
    /**
     * event type: L2 instruction cache hit
     */
    public static final int PCL_L2ICACHE_HIT = 28;
    /**
     * event type: L2 instruction cache miss
     */
    public static final int PCL_L2ICACHE_MISS = 29;


    // TLB

    /**
     * event type: TLB hit
     */
    public static final int PCL_TLB_HIT = 30;
    /**
     * event type: TLB miss
     */
    public static final int PCL_TLB_MISS = 31;


    // Instruction-TLB

    /**
     * event type: instruction TLB hit
     */
    public static final int PCL_ITLB_HIT = 32;
    /**
     * event type: instruction TLB miss
     */
    public static final int PCL_ITLB_MISS = 33;


    // Data-TLB

    /**
     * event type: data TLB hit
     */
    public static final int PCL_DTLB_HIT = 34;
    /**
     * event type: data TLB miss
     */
    public static final int PCL_DTLB_MISS = 35;


    // Cycles

    /**
     * event type: spent cycles for process/thread
     */
    public static final int PCL_CYCLES = 36;
    /**
     * event type: elapsed cycles
     */
    public static final int PCL_ELAPSED_CYCLES = 37;


    // Instructions

    /**
     * event type: integer instructions
     */
    public static final int PCL_INTEGER_INSTR = 38;
    /**
     * event type: floating point instructions
     */
    public static final int PCL_FP_INSTR = 39;
    /**
     * event type: load instructions
     */
    public static final int PCL_LOAD_INSTR = 40;
    /**
     * event type: store instructions
     */
    public static final int PCL_STORE_INSTR = 41;
    /**
     * event type: load or store instructions
     */
    public static final int PCL_LOADSTORE_INSTR = 42;
    /**
     * event type: instructions
     */
    public static final int PCL_INSTR = 43;


    // Jump Instructions

    /**
     * event type: successful jump instructions
     */
    public static final int PCL_JUMP_SUCCESS = 44;
    /**
     * event type: unsuccessful jump instructions
     */
    public static final int PCL_JUMP_UNSUCCESS = 45;
    /**
     * event type: jump instructions
     */
    public static final int PCL_JUMP = 46;


    // Atomic Instructions

    /**
     * event type: successful atomic instructions
     */
    public static final int PCL_ATOMIC_SUCCESS = 47;
    /**
     * event type: unsuccessful atomic instructions
     */
    public static final int PCL_ATOMIC_UNSUCCESS = 48;
    /**
     * event type: atomic instructions
     */
    public static final int PCL_ATOMIC = 49;


    // unit stalls

    /**
     * event type: integer unit stall
     */
    public static final int PCL_STALL_INTEGER = 50;
    /**
     * event type: floating point unit stall
     */
    public static final int PCL_STALL_FP = 51;
    /**
     * event type: jump unit stall
     */
    public static final int PCL_STALL_JUMP = 52;
    /**
     * event type: load unit stall
     */
    public static final int PCL_STALL_LOAD = 53;
    /**
     * event type: store unit stall
     */
    public static final int PCL_STALL_STORE = 54;
    /**
     * event type: unit stall
     */
    public static final int PCL_STALL = 55;


    // derived numbers (results in floating point)
    /**
     * event type: MFLOPS
     */
    public static final int PCL_MFLOPS = 56;
    /**
     * event type: instructions per cycle
     */
    public static final int PCL_IPC = 57;
    /**
     * event type: L1 dcache miss rate
     */
    public static final int PCL_L1DCACHE_MISSRATE = 58;
    /**
     * event type: L2 dcache miss rate
     */
    public static final int PCL_L2DCACHE_MISSRATE = 59;
    /**
     * event type: memory ops / floating point ops ratio
     */
    public static final int PCL_MEM_FP_RATIO = 60;


    //------------------------------------------------------------------------
    // variables

    /**
     * handle
     */
    private long descr[];


    //------------------------------------------------------------------------
    // native methods for the C-side of the Java interface


    /**
     * native C-interface function for PCLinit (not callable from outside)
     */
    public native int cPCLinit(long descr[]); /*???*/
    /**
     * native C-interface function for PCLexit (not callable from outside)
     */
    public native int cPCLexit(long descr);
    /**
     * native C-interface function for PCLquery (not callable from outside)
     */
    public native int cPCLquery(long descr, int event_list[], int n_event, int mode);
    /**
     * native C-interface function for PCLstart (not callable from outside)
     */
    public native int cPCLstart(long descr, int event_list[], int n_event, int mode);
    /**
     * native C-interface function for PCLread (not callable from outside)
     */
    public native int cPCLread(long descr, long i_result_list[], double fp_result_list[], int n_counter);
    /**
     * native C-interface function for PCLstop (not callable from outside)
     */
    public native int cPCLstop(long descr, long i_result_list[], double fp_result_list[], int n_counter);
    /**
     * native C-interface function for PCLeventname (not callable from outside)
     */
    public native String cPCLeventname(int event);


    //------------------------------------------------------------------------
    // Java interface methods


    /**
     * inits PCL and allocates handle
     * @return PCL_SUCCESS if successful, or a failure value else
     */
    public int PCLinit(long d)
    {
	descr = new long[1];
	descr[0] = 0;
	return cPCLinit(descr);
    }

    /**
     * exits PCL and releases handle
     * @return PCL_SUCCESS if exitis successful, or a failure value else
     */
    public int PCLexit(long d)
    {
	return cPCLexit(descr[0]);
    }

    /**
     * query for functionality
     * @param event_list array of requested events
     * @param n_event number of requested events
     * @param mode count mode
     * @return PCL_SUCCESS if events are available, or a failure value else
     */
    public int PCLquery(long d, int event_list[], int n_event, int mode)
    {
	return cPCLquery(descr[0], event_list, n_event, mode);
    }

    /**
     * start counting
     * @param event_list array of requested events
     * @param n_event number of requested events
     * @param mode count mode
     * @return PCL_SUCCESS if events are available, or a failure value else
     */
    public int PCLstart(long d, int event_list[], int n_event, int mode)
    {
	return cPCLstart(descr[0], event_list, n_event, mode);
    }

    /**
     * read out counter values without stop counting
     * @param i_result_list array to hold requested integer values
     * @param fp_result_list array to hold requested floating point values
     * @param n_counter number of requested values
     * @return PCL_SUCCESS if values could be fetched, or a failure value else
     */
    public int PCLread(long d, long i_result_list[], double fp_result_list[], int n_counter)
    {
	return cPCLread(descr[0], i_result_list, fp_result_list, n_counter);
    }

    /**
     * read out counter values and stop counting
     * @param result_list array to hold requested integer values
     * @param fp_result_list array to hold requested floating point values
     * @param n_counter number of requested values
     * @return PCL_SUCCESS if values could be fetched, or a failure value else
     */
    public int PCLstop(long d, long result_list[], double fp_result_list[], int n_counter)
    {
	return cPCLstop(descr[0], result_list, fp_result_list, n_counter);
    }

    /**
     * get a name in form of a string for an event specifier (integer)
     * @param event event to get name for
     * @return String with name of event
     */
    public String PCLeventname(int event)
    {
	return cPCLeventname(event);
    }


    /**
     * determines whether an event produces long or double results
     * @param event event to get result type for
     * @return true, if this event generates long results
     */
    public boolean PCLeventIsInt(int event)
    {
	return event < PCL_MFLOPS;
    }


    /**
     * determines whether an event produces event counts or event rates
     * @param event event to get result type for
     * @return true, if this event generates counts rather than rates
     */
    public boolean PCLeventIsRate(int event)
    {
	return event >= PCL_MFLOPS;
    }


    //------------------------------------------------------------------------
    // load C-implementation


    /**
     * load the dynamic library with the C-interface
     */
    static
    {
	System.loadLibrary("PCL");
    }
}
