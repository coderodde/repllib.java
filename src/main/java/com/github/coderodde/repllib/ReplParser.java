package com.github.coderodde.repllib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 
 */
public final class ReplParser {
    
    private final List<ReplStatement> statementList = new ArrayList<>();
    
    public void addStatement(final ReplStatement statement) {
        Objects.requireNonNull(statement, "Input statement is null");
        
        if (statementList.contains(statement)) {
            final String exceptionMessage = 
                    String.format("Duplicate statement: [%s]",
                                  statement.toString());
            
            throw new ReplDuplicateStatementException(exceptionMessage);
        }
        
        statementList.add(statement);
    }
    
    public boolean contains(final ReplStatement statement) {
        return statementList.contains(statement);
    }
    
    public void removeStatement(final ReplStatement statement) {
        Objects.requireNonNull(statement, "Input statement is null");
        statementList.remove(statement);
    }
    
    public ReplStatement get(final int index) {
        return statementList.get(index);
    }
    
    public int size() {
        return statementList.size();
    }
    
    public boolean isEmpty() {
        return statementList.isEmpty();
    }
    
    public void parse(String commandLine) {
        Objects.requireNonNull(commandLine, "Input command line is null");
        commandLine = commandLine.trim();
        
        if (commandLine.isEmpty()) {
            throw new IllegalArgumentException("Input command line is blank");
        }
        
        parseImpl(commandLine);
    }
    
    private void parseImpl(final String commandLine) {
        for (final ReplStatement statement : statementList) {
            if (statement.matchesCommandLine(commandLine)) {
                statement.actImpl(commandLine);
                return;
            }
        }
        
        final String exceptionMessage = 
                String.format("Statement \"%s\" has no match", commandLine);
        
        throw new ReplNoMatchingStatementException(exceptionMessage);
    }
}
