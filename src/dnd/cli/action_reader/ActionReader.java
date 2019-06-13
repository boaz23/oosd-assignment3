package dnd.cli.action_reader;

/**
 * A service for reading the next input for the 'user'.
 * Allows automatic input to be supplied
 */
public interface ActionReader {
    String nextAction();
}