package vision.genesis.util.log;

public interface LogPrinter {
	void printLog(int priority, String tag, String message, Throwable t);
}
