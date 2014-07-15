package ch.usi.dag.et.util;

import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ch.usi.dag.et.util.Functor.Consumer;
import ch.usi.dag.et.util.Functor.Function;
import ch.usi.dag.et.util.Functor.Predicate;
import ch.usi.dag.util.Lists;


/**
 * Utility class providing utility methods for iterables such as collections and
 * arrays. The factory methods rely on type inference to determine the type
 * parameters needed for constructing a specific instance.
 *
 * @author Lubomir Bulej
 */
public final class Iterables {

    /**
     * Prevents creating instances of this class.
     */
    private Iterables () {
        // pure static class - not to be instantiated
    }


    /* ***********************************************************************
     * PUBLIC STATIC FIELDS
     * ***********************************************************************/

    static abstract class ImmutableIterator <T> implements Iterator <T> {
        @Override
        public final void remove () {
            throw new UnsupportedOperationException (
                "element removal not supported by "+ this.getClass ().getSimpleName ()
            );
        }
    }


    @SuppressWarnings ("rawtypes")
    public static final Iterator EMPTY_ITERATOR = new ImmutableIterator () {
        @Override
        public boolean hasNext () {
            return false;
        }

        @Override
        public Object next () {
            throw new NoSuchElementException ();
        }
    };


    /* ***********************************************************************
     * PUBLIC METHODS
     * ***********************************************************************/

    /**
     * Creates an iterable from the given array.
     *
     * @param <E>
     *      element type
     * @param array
     *      the array to create iterable for
     * @return
     *      {@link Iterable} encapsulating the given array.
     */
    public static <E> Iterable <E> iterable (final E [] array) {
        return new Iterable <E> () {
            @Override
            public Iterator <E> iterator () {
                return new ArrayIterator <E> (array);
            }
        };
    }


    /**
     * Creates a random iterable from the given array. The iterable will
     * provide iterators that will return elements of the array in random
     * order, without repeating.
     *
     * @param <E>
     *      element type
     * @param array
     *      the array to create iterable for
     * @return
     *      {@link Iterable} encapsulating the given array in random fashion.
     */
    public static <E> Iterable <E> randomIterable (final E [] array) {
        return __randomIterable (__randomAccessible (array));
    }


    /**
     * Creates an infinite sampling iterable from the given array. The iterable
     * will provide iterators that will return random elements from the array,
     * indefinitely.
     *
     * @param <E>
     *      element type
     * @param array
     *      the array to create iterable for
     * @return
     *      Sampling {@link Iterable} encapsulating the given array.
     */
    public static <E> Iterable <E> samplingIterable (final E [] array) {
        return __samplingIterable (__randomAccessible (array));
    }


    /**
     * Creates a random iterable from the given collection. The iterable will
     * provide iterators that will return elements of the collection in random
     * order, without repeating.
     *
     * @param <E>
     *      element type
     * @param list
     *      the collection to create the iterable for
     * @return
     *      Random {@link Iterable} encapsulating the given collection.
     */
    public static <E> Iterable <E> randomIterable (final Collection <E> collection) {
        return __randomIterable (__randomAccessible (Lists.newArrayList (collection)));
    }


    /**
     * Creates an infinite sampling iterable from the given collection. The
     * iterable will provide iterators that will return random elements from
     * the collection, indefinitely.
     *
     * @param <E>
     *      element type
     * @param list
     *      the collection to create the iterable for
     * @return
     *      Sampling {@link Iterable} encapsulating the given collection.
     */
    public static <E> Iterable <E> samplingIterable (final Collection <E> collection) {
        return __samplingIterable (__randomAccessible (Lists.newArrayList (collection)));
    }


    /**
     * Creates an iterator for the given array.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source array to create the iterator for
     * @return
     *      {@link Iterator} encapsulating the given array
     */
    public static <E> Iterator <E> iterator (final E [] array) {
        return new ArrayIterator <E> (array);
    }


    /**
     * Creates a random iterator for the given array. The iterator will
     * return elements of the array in random order, without repeating.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source array to create the iterator for
     * @return
     *      {@link Iterator} encapsulating the given array.
     */
    public static <E> Iterator <E> randomIterator (final E [] array) {
        return new RandomIterator <E> (__randomAccessible (array));
    }


    /**
     * Creates an infinite sampling iterator for the given array. The iterator
     * will return random elements from the array, indefinitely.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source array to create the iterator for
     * @return
     *      Sampling {@link Iterator} encapsulating the given array.
     */
    public static <E> Iterator <E> samplingIterator (final E [] array) {
        return new RandomSampler <E> (__randomAccessible (array));
    }


    /**
     * Creates a random iterator for the given collection. The iterator will
     * return elements of the collection in random order, without repeating.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source collection to create the iterator for
     * @return
     *      {@link Iterator} encapsulating the given collection.
     */
    public static <E> Iterator <E> randomIterator (final Collection <E> collection) {
        return new RandomIterator <E> (__randomAccessible (Lists.newArrayList (collection)));
    }


    /**
     * Creates an infinite sampling iterator for the given collection. The
     * iterator will return random elements from the collection, indefinitely.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source collection to create the iterator for
     * @return
     *      Sampling {@link Iterator} encapsulating the given collection.
     */
    public static <E> Iterator <E> samplingIterator (final Collection <E> collection) {
        return new RandomSampler <E> (__randomAccessible (Lists.newArrayList (collection)));
    }


    /**
     * Creates a random iterator for the given iterable. The iterator will
     * return elements of the iterable in random order, without repeating.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source iterable to create the iterator for
     * @return
     *      {@link Iterator} encapsulating the given iterable.
     */
    public static <E> Iterator <E> randomIterator (final Iterable <E> iterable) {
        return new RandomIterator <E> (__randomAccessible (Lists.newArrayList (iterable)));
    }


    /**
     * Creates an infinite sampling iterator for the given iterable. The
     * iterator will return random elements from the iterable, indefinitely.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source iterable to create the iterator for
     * @return
     *      {@link Iterator} encapsulating the given iterable.
     */
    public static <E> Iterator <E> samplingIterator (final Iterable <E> iterable) {
        return new RandomSampler <E> (__randomAccessible (Lists.newArrayList (iterable)));
    }


    /**
     * Returns an empty iterator. This is an equivalent of an empty array or
     * an empty collection.
     *
     * @param <E>
     *      element type
     * @return Empty iterator.
     */
    @SuppressWarnings ("unchecked")
    public static <E> Iterator <E> emptyIterator () {
        return EMPTY_ITERATOR;
    }


    /**
     * Makes an array from an iterable of objects, behaves exactly like
     * {@link List#toArray(T[])} (first argument is the iterable).
     *
     * @param objects
     *      iterable of objects for the array
     * @param result
     *      returns the result here, if array is big enough, otherwise
     *      allocates a new array
     * @return
     *      an array containing all objects, that are a part of the type
     *
     * @throws ArrayStoreException
     *      if at least one item in source array has a type, that cannot be
     *      converted to the type of the result array
     * @throws NullPointerException
     *      if the result array is null
     */
    @SuppressWarnings ("rawtypes")
    public static <E> E[] toArray (final Iterable objects, final E[] result) {
        try {
            final List <E> list = Lists.newArrayList();

            for (final Object object : objects) {
                // try to convert it to right type, or throw an exception
                @SuppressWarnings ("unchecked")
                final E element = (E) object;

                list.add (element);
            }

            return list.toArray (result);

        } catch (final ClassCastException cce) {
            throw new ArrayStoreException (cce.getMessage());
        }
    }


    /**
     * Creates a filtering iterable from the given source array
     * and filter. The filtering iterable will only return elements
     * that are accepted by the filter.
     *
     * @param <E>
     *      element type
     * @param array
     *      the source array to filter
     * @param filter
     *      object encapsulating the filtering method
     * @return
     *      filtering {@link Iterable} returning elements from the source array
     */
    public static <E> Iterable <E> filter (
        final E [] array, final Predicate <E> filter
    ) {
        return new Iterable <E> () {
            @Override
            public Iterator <E> iterator () {
                return new FilteringIterator <E> (new ArrayIterator <E> (array), filter);
            }
        };
    }


    /**
     * Creates a filtering iterable from the given source iterable
     * and a filter. The filtering iterable will only return elements
     * that are accepted by the filter.
     *
     * @param <E>
     *      element type
     * @param iterable
     *      the source iterable to filter
     * @param filter
     *      object encapsulating the filtering method
     * @return
     *      filtering {@link Iterable} returning elements from the source iterable
     */
    public static <E> Iterable <E> filter (
        final Iterable <E> iterable, final Predicate <E> filter
    ) {
        return new Iterable <E> () {
            @Override
            public Iterator <E> iterator () {
                return new FilteringIterator <E> (iterable.iterator (), filter);
            }
        };
    }


    /**
     * Creates a mapping iterable by applying a lambda function encapsulated in
     * an object implementing the {@link Function} interface to each element
     * of the given iterable. The source and result element types can be
     * different and depend on the type of the value lambda function.
     *
     * @param <F>
     *      source element type
     * @param <T>
     *      resulting element type
     * @param elements
     *      iterable containing elements to apply the lambda on
     * @param functor
     *      object encapsulating the mapping function
     * @return
     *      mapping {@link Iterable} returning elements produced by the value
     *      lambda function from the elements of the source iterable
     *
     * @see Function
     */
    public static <F, T> Iterable <T> map (
        final Iterable <F> elements, final Function <F, T> functor
    ) {
        return new Iterable <T> () {
            @Override
            public Iterator <T> iterator () {
                return new MappingIterator <F, T> (elements.iterator (), functor);
            }
        };
    }


    /**
     * Applies a procedure encapsulated in an object implementing the
     * {@link Consumer} interface on each element of the given array.
     *
     * @param <E>
     *      element type
     * @param elements
     *      array of elements to apply the function on
     * @param procedure
     *      object encapsulating the function to call for each element
     *
     * @see Consumer
     */
    public static <E> Consumer <E> forEach (
        final E [] elements, final Consumer <E> procedure
    ) {
        for (final E element : elements) {
            procedure.accept (element);
        }

        return procedure;
    }

    private static final class ExecutionManager {
        private static final ExecutorService
            __defaultExecutor__ = Executors.newCachedThreadPool ();

        private final Future <Boolean> [] __results;
        private final ExecutorService __executor;

        //

        @SuppressWarnings ("unchecked")
        ExecutionManager (final int taskCount) {
            __results = new Future [taskCount];
            __executor = __defaultExecutor__;
        }

        void waitForCompletion () throws InterruptedException, ExecutionException {
            for (final Future <Boolean> result : __results) {
                if (result != null) {
                    result.get ();
                }
            }
        }

        void execute (final int taskIndex, final Runnable task) {
            assert taskIndex >= 0 && taskIndex < __results.length;
            __results [taskIndex] = __executor.submit (task, Boolean.TRUE);
        }
    }


    private static final class ArraySlicer {
        private final int __sliceCount;
        private final int __sliceLength;
        private final int __excessCount;

        //

        private ArraySlicer (final int elementCount) {
            final int cpuCount = Runtime.getRuntime ().availableProcessors ();

            //
            // The number of slices should not exceed the number of available CPUs.
            // It may be lower if the array is small, but should not be zero.
            // The slices should be equal in length as much as possible.
            //
            __sliceCount = Math.min (Math.max (1, elementCount), cpuCount);
            __sliceLength = elementCount / __sliceCount;
            __excessCount = elementCount % __sliceCount;
        }

        //

        public int getSliceCount () {
            return __sliceCount;
        }

        public int getSliceStart (final int sliceIndex) {
            assert sliceIndex >= 0 && sliceIndex < __sliceCount;
            return __getSliceStart (sliceIndex);
        }

        public int getSliceEnd (final int sliceIndex) {
            assert sliceIndex >= 0 && sliceIndex < __sliceCount;
            return __getSliceStart (sliceIndex + 1);
        }

        //

        private int __getSliceStart (final int sliceIndex) {
            return (sliceIndex * __sliceLength) + Math.min (sliceIndex,  __excessCount);
        }

        public static ArraySlicer getInstance (final int elementCount) {
            return new ArraySlicer (elementCount);
        }
    }



    private static abstract class ParallelArrayProcessor {

        private final int __elementCount;

        public ParallelArrayProcessor (final int elementCount) {
            __elementCount = elementCount;
        }

        public final void execute () throws InterruptedException, ExecutionException {
            final ArraySlicer slicer = ArraySlicer.getInstance (__elementCount);
            final int sliceCount = slicer.getSliceCount ();

            //

            final ExecutionManager manager = new ExecutionManager (sliceCount);

            for (int slice = 0; slice < sliceCount; slice++) {
                final int sliceIndex = slice;
                manager.execute (sliceIndex, new Runnable () {
                    @Override
                    public void run () {
                        _processSlice (
                            slicer.getSliceStart (sliceIndex),
                            slicer.getSliceEnd (sliceIndex)
                        );
                    }
                });
            }

            manager.waitForCompletion ();
        }

        protected abstract void _processSlice (int start, int end);
    }


    public static <E> Consumer <E> forEachParallel (
        final E [] elements, final Consumer <E> procedure
    ) throws InterruptedException, ExecutionException {

        final ParallelArrayProcessor processor =
            new ParallelArrayProcessor (elements.length) {
                @Override
                protected void _processSlice (final int start, final int end) {
                    for (int i = start; i < end; i++) {
                        procedure.accept (elements [i]);
                    }
                }
            };

        processor.execute ();
        return procedure;
    }


    public static void main (final String [] args) throws InterruptedException, ExecutionException {
        final Integer [] numbers = new Integer [18];
        for (int i = 0; i < numbers.length; i++) {
            numbers [i] = i;
        }

        final Consumer <Integer> functor = new Consumer <Integer> () {
            @Override
            public void accept (final Integer element) {
                System.out.printf ("Thread: %s, element: %d\n", Thread.currentThread ().getName (), element);
            }
        };


        forEach (numbers, functor);
        System.out.println ("----------------------------------------------------");
        forEachParallel (numbers, functor);
        System.out.println ("----------------------------------------------------");
        forEachParallel (numbers, functor);
    }


    /**
     * Applies a function encapsulated in an object implementing the
     * {@link Consumer} interface to each element of the given iterable.
     *
     * @param <E>
     *      element type
     * @param elements
     *      iterable containing the elements to apply the function to
     * @param procedure
     *      object encapsulating the function
     *
     * @see Consumer
     */
    public static <E> Consumer <E> forEach (
        final Iterable <E> elements, final Consumer <E> procedure
    ) {
        for (final E element : elements) {
            procedure.accept (element);
        }

        return procedure;
    }


    /**
     * Collects elements from an iterable into a list provided by the
     * caller. The choice of the list implementation is left with the caller.
     *
     * @param <E>
     *      element type
     * @param elements
     *      iterable containing elements to add to the list, must not be
     *      {@code null}
     * @param output
     *      a list to collect the elements into, must not be {@code null}
     * @return
     *      the given list containing the elements from the iterable
     */
    public static <E> List <E> collect (
        final Iterable <E> elements, final List <E> output
    ) {
        for (final E element : elements) {
            output.add (element);
        }

        return output;
    }


    /* ***********************************************************************
     * PUBLIC STATIC CLASS: AbstractIterator
     * ***********************************************************************/

    /**
     * A helper abstract class for iterating over other iterators. Subclasses
     * only need to define the {@link #_getNext()} method, everything else is
     * taken care of by this class.
     */
    public static abstract class AbstractIterableIterator <E>
    implements Iterator <E> {

        /**
         * The element to return from the next call to
         * the {@link #next()} method.
         */
        private E __next;

        //

        /*
         * @see Iterator#remove()
         */
        @Override
        public final void remove () {
            //
            // We don't support element removal.
            //
            throw new UnsupportedOperationException (
                "element removal not supported by "+ this.getClass ().getSimpleName ()
            );
        }


        /*
         * @see Iterator#next()
         */
        @Override
        public final E next () {
            if (!hasNext ()) {
                throw new NoSuchElementException ();
            }

            final E result = __next;
            __next = null;

            return result;
        }


        /*
         * @see Iterator#hasNext()
         */
        @Override
        public final boolean hasNext () {
            //
            // If there is no next value ready, try to get one and if there
            // still is not any, report no more elements to the caller.
            //
            if (__next == null) {
                __next = _getNext ();
            }

            return (__next != null);
        }


        /**
         * Returns the next element for this iterator. Returns
         * {@code null} if there are no more elements, which implies that
         * the iterable itself must not contain {@code null} elements.
         * <p>
         * This method should be implemented by subclasses.
         *
         * @return
         *      next element of this iterator
         */
        protected abstract E _getNext ();

    }


    /* ***********************************************************************
     * PRIVATE STATIC CLASS: FilteringIterator
     * ***********************************************************************/

    private static final class FilteringIterator <E>
    extends AbstractIterableIterator <E> {

        private final Iterator <E> __source;
        private final Predicate <E> __filter;

        //

        private FilteringIterator (
            final Iterator <E> source, final Predicate <E> filter
        ) {
            __filter = filter;
            __source = source;
        }


        @Override
        protected E _getNext () {
            final Iterator <E> source = __source;
            final Predicate <E> filter = __filter;

            //
            // Iterate over the source until we encounter an acceptable element.
            // If we reach the end without encountering any, just return null.
            //
            while (source.hasNext ()) {
                final E element = source.next ();
                if (filter.test (element)) {
                    return element;
                }
            }

            return null;
        }

    }


    /* ***********************************************************************
     * PRIVATE STATIC CLASS: MappingIterator
     * ***********************************************************************/

    private static final class MappingIterator <E, R> extends AbstractIterableIterator <R> {
        private final Iterator <E> __source;
        private final Function <E, R> __functor;

        private MappingIterator (
            final Iterator <E> source, final Function <E, R> functor
        ) {
            __source = source;
            __functor = functor;
        }


        @Override
        protected R _getNext () {
            final Iterator <E> source = __source;
            if (source.hasNext ()) {
                return __functor.apply (source.next ());
            } else {
                return null;
            }
        }

    }


    /* *******************************************************************
     * PRIVATE STATIC CLASS: ArrayIterator
     * *******************************************************************/

    private static final class ArrayIterator <E> implements Iterator <E> {
        private final E [] __array;
        private final int __length;
        private int __position;

        private ArrayIterator (final E [] array) {
            __array = array;
            __length = array.length;
            __position = 0;
        }


        /*
         * @see Iterator#remove()
         */
        @Override
        public void remove () {
            throw new UnsupportedOperationException (
                "cannot remove elements from array"
            );
        }


        /*
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext () {
            return __position < __length;
        }


        /*
         * @see Iterator#next()
         */
        @Override
        public E next () {
            if (!hasNext ()) {
                throw new NoSuchElementException ();
            }

            return __array [__position++];
        }
    }


    /* *******************************************************************
     * PRIVATE STATIC CLASS: RandomAccessible
     * *******************************************************************/

    private interface RandomAccessible <E> {
        public abstract E get (int index);
        public abstract void set (int index, E element);
        public abstract int size ();
    }

    //

    public static <E> RandomAccessible <E> __randomAccessible (final E [] array) {
        return new RandomAccessible <E> () {
            @Override
            public E get (final int index) {
                return array [index];
            }

            @Override
            public void set (final int index, final E element) {
                array [index] = element;
            }

            @Override
            public int size () {
                return array.length;
            }
        };
    }

    //

    public static <E, T extends List <E>, RandomAccess>
    RandomAccessible <E> __randomAccessible (final T list) {
        return new RandomAccessible <E> () {
            @Override
            public E get (final int index) {
                return list.get (index);
            }

            @Override
            public void set (final int index, final E element) {
                list.set (index, element);
            }

            @Override
            public int size () {
                return list.size ();
            }
        };
    }


    /* *******************************************************************
     * PRIVATE STATIC CLASS: RandomSampler
     * *******************************************************************/

    private static <E> Iterable <E> __samplingIterable (
        final RandomAccessible <E> elements
    ) {
        return new Iterable <E> () {
            @Override
            public Iterator <E> iterator () {
                return new RandomSampler <E> (elements);
            }
        };
    }

    private static class RandomSampler <E> extends ImmutableIterator <E> {

        protected final Random _random;
        protected final RandomAccessible <E> _elements;
        protected final int _count;

        //

        private RandomSampler (final RandomAccessible <E> elements, final Random random) {
            _elements = elements;
            _count = elements.size ();
            _random = random;
        }

        private RandomSampler (final RandomAccessible <E> elements) {
            this (elements, new Random ());
        }

        private RandomSampler (final RandomAccessible <E> elements, final long seed) {
            this (elements, new Random (seed));
        }

        //

        /*
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext () {
            return true;
        }

        /*
         * @see Iterator#next()
         */
        @Override
        public E next () {
            return _elements.get (_random.nextInt (_count));
        }

    }


    /* *******************************************************************
     * PRIVATE STATIC CLASS: RandomIterator
     * *******************************************************************/

    private static <E> Iterable <E> __randomIterable (
        final RandomAccessible <E> elements
    ) {
        return new Iterable <E> () {
            @Override
            public Iterator <E> iterator () {
                return new RandomIterator <E> (elements);
            }
        };
    }

    private static final class RandomIterator <E> extends RandomSampler <E> {

        private int __lower;
        private int __upper;
        private final BitSet __used;

        //

        private RandomIterator (final RandomAccessible <E> elements, final Random random) {
            super (elements, random);

            __lower = 0;
            __upper = elements.size ();
            __used = new BitSet (elements.size ());
        }

        private RandomIterator (final RandomAccessible <E> elements) {
            this (elements, new Random ());
        }

        private RandomIterator (final RandomAccessible <E> elements, final long seed) {
            this (elements, new Random (seed));
        }


        /*
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext () {
            return __lower < __upper;
        }


        /*
         * @see Iterator#next()
         */
        @Override
        public E next () {
            if (!hasNext ()) {
                throw new NoSuchElementException ();
            }

            // TODO Make this more work-conserving - this gets ineffective. --LB
            final int range = __upper - __lower;
            while (true) {
                final int index = __lower + _random.nextInt (range);
                if (! __used.get (index)) {
                    __used.set (index);
                    __updateLowerBoundary (index);
                    __updateUpperBoundary (index);
                    return _elements.get (index);
                }
            }
        }

        private void __updateLowerBoundary (final int index) {
            if (__lower == index) {
                __lower = __used.nextClearBit (__lower);
                if (__lower < 0) {
                    __lower = __upper;
                }
            }
        }

        private void __updateUpperBoundary (final int index) {
            if (index + 1 == __upper) {
                while (__lower < __upper && __used.get (__upper - 1)) {
                    __upper--;
                };
            }
        }

    }

}
