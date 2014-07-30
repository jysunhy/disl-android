package ch.usi.dag.et2.tools.etracks.util;

import java.io.IOException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;

import ch.usi.dag.util.Lists;


public class Outputter {

    private static final Formatter __BLACK_HOLE__ = new Formatter (new Appendable () {
        @Override
        public Appendable append (final CharSequence csq, final int start, final int end) throws IOException {
            return this;
        }

        @Override
        public Appendable append (final char c) throws IOException {
            return this;
        }

        @Override
        public Appendable append (final CharSequence csq) throws IOException {
            return this;
        }
    });


    public static final Outputter NULL = new Outputter (null) {
        @Override
        public void format (final String format, final Object ... args) {
            // do nothing
        }
    };

    //

    private static final List <Formatter> __formatters__ = Lists.newLinkedList ();

    static {
        Runtime.getRuntime ().addShutdownHook (new Thread () {
            @Override
            public void run () {
                for (final Formatter formatter : __formatters__) {
                    __close (formatter);
                }
            }

            private void __close (final Formatter formatter) {
                try {
                    formatter.flush ();
                    formatter.close ();
                } catch (final FormatterClosedException fce) {
                    System.err.println ("error closing formatter: "+ fce.getMessage ());
                }
            }
        });
    }

    //

    private final Formatter __output;

    private Formatter __current;


    //

    private Outputter (final Formatter formatter) {
        __output = formatter;
        __current = formatter;
    }

    //

    public void format (final String format, final Object ... args) {
        __current.format (format,  args);
    }


    public Outputter mute () {
        __current = __BLACK_HOLE__;
        return this;
    }


    public Outputter unmute () {
        __current = __output;
        return this;
    }

    //

    public static Outputter create (String fileName) {
        try {
            fileName = fileName +"."+ java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            final Formatter output = new Formatter (fileName);
            __formatters__.add (output);
            return new Outputter (output);

        } catch (final IOException ioe) {
            System.err.printf ("Problem creating outputter for %s, outputting to standard error", fileName);
            return new Outputter (new Formatter (System.err));
        }
    }

}
