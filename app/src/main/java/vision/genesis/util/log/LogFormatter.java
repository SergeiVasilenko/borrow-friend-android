package vision.genesis.util.log;

import android.util.Log;

class LogFormatter {

	String format(int priority, String tag, String message, Throwable t) {
		return getPrioritySymbol(priority) + ":" + tag + ": " + message;
	}

	private static String getPrioritySymbol(int priority) {
		switch (priority) {
			case Log.ASSERT:
				return "A";
			case Log.ERROR:
				return "E";
			case Log.WARN:
				return "W";
			case Log.INFO:
				return "I";
			case Log.DEBUG:
				return "D";
			case Log.VERBOSE:
				return "V";
			default:
				return "D";

		}
	}
}
