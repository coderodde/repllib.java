package com.github.coderodde.repllib;

/**
 * This class defines an exception type thrown when adding equivalent statements
 * to a parser {@link com.github.coderodde.repllib.ReplParser}. Two statements 
 * are said to be <i>equivalent</i> when they have the same token descriptor. 
 * Two equivalent statements may, however, have different actions.
 * 
 * @version 1.0.1
 * @since 1.0.1
 */
public final class ReplDuplicateStatementException extends RuntimeException {
 
    public ReplDuplicateStatementException(final String exceptionMessage) {
        super(exceptionMessage);
    }
}
