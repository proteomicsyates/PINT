package edu.scripps.yates.client.gui.incrementalCommands;

/**
 * This interface provides the method to be called for doing something
 * 
 * @author Salva
 * 
 * @param <Y>
 */
public interface DoSomethingTask<Y> {
	public Y doSomething();
}
