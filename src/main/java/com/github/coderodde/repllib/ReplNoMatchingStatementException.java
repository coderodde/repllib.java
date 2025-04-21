package com.github.coderodde.repllib;

/**
 * This class implements an exception type thrown when a command has no matching
 * statements in a parser.
 * 
 * @version 1.0.1
 * @since 1.0.0
 */
public final class ReplNoMatchingStatementException extends RuntimeException {
    
    public ReplNoMatchingStatementException(final String exceptionMessage) {
        super(exceptionMessage);
    }
}
