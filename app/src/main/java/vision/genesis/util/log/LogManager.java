package vision.genesis.util.log;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;


/**
 * Created on 18.11.16.
 *
 * @author Sergey Vasilenko (vasilenko.sn@gmail.com)
 */

public class LogManager {

	public static final LogPrinter DEBUG_PRINTER = new DebugLogPrinter();

	private static final int MAX_LOG_LENGTH = 4000;

	private static final    ArrayList<LogPrinter> PRINTERS         = new ArrayList<>();
	private static final    LogPrinter[]          EMPTY_PRINTERS   = new LogPrinter[0];
	private volatile static LogPrinter[]          mPrintersAsArray = EMPTY_PRINTERS;

	private int mLogLevel;

	private final String mTag;

	private static boolean isDebugMode = true;

	static {
		addLogPrinter(DEBUG_PRINTER);
	}

	public LogManager(String tag) {
		mTag = tag;
	}

	public LogManager(String tag, int logLevel) {
		mTag = tag;
		mLogLevel = logLevel;
	}

	public static void setDebugMode(boolean isDebugMode) {
		if (LogManager.isDebugMode == isDebugMode)
			return;
		LogManager.isDebugMode = isDebugMode;
		if (isDebugMode)
			addLogPrinter(DEBUG_PRINTER);
		else
			removePrinter(DEBUG_PRINTER);
	}

	/**
	 * @param logLevel int - the minimum priority from {@link Log} which will be shown in log-cat.
	 */
	public void setLogLevel(int logLevel) {
		mLogLevel = logLevel;
	}

	public void v(String msg, Object... args) {
		log(Log.VERBOSE, msg, null, args);
	}

	public void v(String msg, Throwable tr, Object... args) {
		log(Log.VERBOSE, msg, tr, args);
	}

	public void d(String msg, Object... args) {
		log(Log.DEBUG, msg, null, args);
	}

	public void d(String msg, Throwable tr, Object... args) {
		log(Log.DEBUG, msg, tr, args);
	}


	public void i(String msg, Object... args) {
		log(Log.INFO, msg, null, args);
	}

	public void i(String msg, Throwable tr, Object... args) {
		log(Log.INFO, msg, tr, args);
	}

	public void w(String msg, Object... args) {
		log(Log.WARN, msg, null, args);
	}

	public void w(String msg, Throwable tr, Object... args) {
		log(Log.WARN, msg, tr, args);
	}

	public void w(Throwable tr) {
		log(Log.WARN, null, tr);
	}

	public void e(String msg, Object... args) {
		log(Log.ERROR, msg, null, args);
	}

	public void e(String msg, Throwable tr, Object... args) {
		log(Log.ERROR, msg, tr, args);
	}

	private void log(int logLevel, String message, Throwable t, Object... args) {
		if (mLogLevel > logLevel) {
			return;
		}

		if (TextUtils.isEmpty(message)) {
			if (t == null) {
				return; // Swallow message if it's null and there's no throwable.
			}
			message = getStackTraceString(t);
		} else {
			if (args.length > 0) {
				message = formatMessage(message, args);
			}
			if (t != null) {
				message += "\n" + getStackTraceString(t);
			}
		}
		prepareLog(logLevel, message, t);
	}

	private String getStackTraceString(Throwable t) {
		// Don't replace this with Log.getStackTraceString() - it hides
		// UnknownHostException, which is not what we want.
		StringWriter sw = new StringWriter(256);
		PrintWriter pw = new PrintWriter(sw, false);
		t.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	private void prepareLog(int priority, String message, Throwable t) {
		if (mPrintersAsArray.length == 0) { // small optimization
			return;
		}
		if (message.length() < MAX_LOG_LENGTH) {
			printLog(priority, message, t);
			return;
		}

		// Split by line, then ensure each line can fit into Log's maximum length.
		for (int i = 0, length = message.length(); i < length; i++) {
			int newline = message.indexOf('\n', i);
			newline = newline != -1 ? newline : length;
			do {
				int end = Math.min(newline, i + MAX_LOG_LENGTH);
				String part = message.substring(i, end);
				printLog(priority, part, t);
				i = end;
			} while (i < newline);
		}
	}


	private void printLog(int priority, String message, Throwable t) {
		LogPrinter[] printers = mPrintersAsArray;

		for (LogPrinter printer : printers) {
			printer.printLog(priority, mTag, message, t);
		}
	}

	/**
	 * Formats a log message with optional arguments.
	 */
	protected String formatMessage(String message, Object[] args) {
		return String.format(message, args);
	}

	public static void addLogPrinter(LogPrinter logPrinter) {
		if (PRINTERS == null) {
			throw new NullPointerException("PRINTERS is null");
		}

		if (logPrinter == null) {
			throw new NullPointerException("logPrinter parameter is null");
		}

		synchronized (PRINTERS) {
			PRINTERS.add(logPrinter);
			mPrintersAsArray = PRINTERS.toArray(new LogPrinter[PRINTERS.size()]);
		}
	}

	public static void clearAllPrinters() {
		if (PRINTERS == null) {
			throw new NullPointerException("PRINTERS is null");
		}
		synchronized (PRINTERS) {
			PRINTERS.clear();
			mPrintersAsArray = EMPTY_PRINTERS;
		}
	}

	public static boolean removePrinter(LogPrinter logPrinter) {
		if (PRINTERS == null) {
			throw new NullPointerException("PRINTERS is null");
		}

		if (logPrinter == null) {
			throw new NullPointerException("logPrinter parameter is null");
		}

		synchronized (PRINTERS) {
			boolean isRemoved = PRINTERS.remove(logPrinter);
			if (isRemoved) {
				mPrintersAsArray = PRINTERS.toArray(new LogPrinter[PRINTERS.size()]);
			}
			return isRemoved;
		}
	}

	private static class DebugLogPrinter implements LogPrinter {

		@Override
		public void printLog(int priority, String tag, String message, Throwable t) {
			if (LogManager.isDebugMode) {
				Log.println(priority, tag, message);
			}
		}
	}
}
