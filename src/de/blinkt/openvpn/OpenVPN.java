package de.blinkt.openvpn;

import java.util.LinkedList;
import java.util.Vector;

public class OpenVPN {
	private static final int MAXLOGENTRIES = 200;


	public static LinkedList<String> logbuffer = new LinkedList<String>();
	
	private static Vector<LogListener> logListener=new Vector<OpenVPN.LogListener>();
	private static Vector<SpeedListener> speedListener=new Vector<OpenVPN.SpeedListener>();
	private static String[] mBconfig;

	public interface LogListener {
		void newLog(String logmessage);
	}
	
	public interface SpeedListener {
		void updateSpeed(String logmessage);
	}

	synchronized static void logMessage(int level,String prefix, String message)
	{
		logbuffer.addLast(prefix +  message);
		if(logbuffer.size()>MAXLOGENTRIES)
			logbuffer.removeFirst();
		
		for (LogListener ll : logListener) {
			ll.newLog(prefix + message);
		}

	}

	synchronized static void clearLog() {
		logbuffer.clear();
	}

	synchronized static void addLogListener(LogListener ll){
		logListener.add(ll);
	}

	synchronized static void removeLogListener(LogListener ll) {
		logListener.remove(ll);
	}

	
	synchronized static void addSpeedListener(SpeedListener sl){
		speedListener.add(sl);
	}

	synchronized static void removeSpeedListener(SpeedListener sl) {
		speedListener.remove(sl);
	}



	synchronized public static String[] getlogbuffer() {

		// The stoned way of java to return an array from a vector
		// brought to you by eclipse auto complete
		return (String[]) logbuffer.toArray(new String[logbuffer.size()]);

	}
	public static void logBuilderConfig(String[] bconfig) {
		mBconfig =bconfig;
	}
	public static void triggerLogBuilderConfig() {
		if(mBconfig==null) {
			logMessage(0, "", "No active interface");
		} else {
			for (String item : mBconfig) {
				logMessage(0, "", item);
			}	
		}

	}

	public static void updateSpeedString(String msg) {
		for (SpeedListener sl : speedListener) {
			sl.updateSpeed(msg);
		}
	}
}
