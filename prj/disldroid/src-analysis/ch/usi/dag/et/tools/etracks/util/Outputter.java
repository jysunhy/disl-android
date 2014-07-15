package ch.usi.dag.et.tools.etracks.util;

import java.io.IOException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;


public class Outputter {

    private static final Formatter __DUMMY_FORMATTER__ = new Formatter (new Appendable () {
        @Override
        public Appendable append (CharSequence csq, int start, int end) throws IOException {
            return this;
        }

        @Override
        public Appendable append (char c) throws IOException {
            return this;
        }

        @Override
        public Appendable append (CharSequence csq) throws IOException {
            return this;
        }
    });


    public static final Outputter NULL = new Outputter (null) {
        @Override
        public void format (final String format, final Object ... args) {
            // do nothing
        }
    };

    private static final List <Formatter>
        __formatters__ = new LinkedList <Formatter> ();

    static {
        Runtime.getRuntime ().addShutdownHook (new Thread () {
            @Override
            public void run () {
                for (final Formatter formatter : __formatters__) {
                    formatter.flush ();
                }
            }
        });
    }

    //

    private Formatter __formatter;

    private Formatter __realFormatter;

    //

    private Outputter (final Formatter formatter) {
        __realFormatter = formatter;
        __formatter = formatter;
    }

    //

    public void format (final String format, final Object ... args) {
        __formatter.format (format,  args);
    }


    public Outputter mute () {
        if (__formatter != __DUMMY_FORMATTER__) {
            __realFormatter = __formatter;
            __formatter = __DUMMY_FORMATTER__;
        }
        return this;
    }


    public Outputter unmute () {
        if (__formatter == __DUMMY_FORMATTER__) {
            __formatter = __realFormatter;
        }
        return this;
    }

    //

    public static Outputter create (final String fileName) {
        try {
            final Formatter stream = new Formatter (fileName);
            __formatters__.add (stream);
            return new Outputter (stream);

        } catch (final IOException ioe) {
            System.err.printf ("Problem creating outputter for %s, outputting to standard error", fileName);
            return new Outputter (new Formatter (System.err));
        }
    }

}
