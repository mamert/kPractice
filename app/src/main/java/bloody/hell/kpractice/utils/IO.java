package bloody.hell.kpractice.utils;

import java.io.PrintWriter;

public class IO {


    /** better than printStackTrace, gives full cause stack trace. Doesn't, however, print suppressed. */
    public static void writeThrowable(Throwable t, PrintWriter pw) {
        pw.println(t.toString());
        StackTraceElement[] trace = t.getStackTrace();
        for (StackTraceElement traceElement : trace)
            pw.println("\t@ " + traceElement);

        Throwable cause = t.getCause();
        if (cause != null) {
            pw.println("Caused by: ");
            writeThrowable(cause, pw);
        }
    }
}
