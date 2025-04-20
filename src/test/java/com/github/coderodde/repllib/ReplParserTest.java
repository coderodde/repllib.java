package com.github.coderodde.repllib;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ReplParserTest {

    private static final ReplKeyword keywordFoo  = new ReplKeyword("foo");
    private static final ReplKeyword keywordBar  = new ReplKeyword("bar");
    private static final ReplKeyword keywordBaz  = new ReplKeyword("baz");
    private static final ReplString replString   = new ReplString();
    private static final ReplInteger replInteger = new ReplInteger();
    private static final ReplReal replReal       = new ReplReal();
    
    @Test(expected = ReplDuplicateStatementException.class)
    public void throwsOnDuplicateTokenDescriptorsWithDifferentActions() {
        final ReplParser parser = new ReplParser();
        final List<ReplTokenDescriptor> descriptorList = new ArrayList<>();
        
        descriptorList.add(keywordFoo);
        descriptorList.add(keywordBar);
        
        final ReplStatement statement1 = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                int i = 0;
                i++;
            }
        });
        
        final ReplStatement statement2 = 
                new ReplStatement(descriptorList, new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                int i = 0;
                i--;
            }
        });
        
        parser.addStatement(statement1);
        parser.addStatement(statement2); // Should throw.
    }
    @Test(expected = ReplDuplicateStatementException.class)
    public void throwsOnDuplicateTokenDescriptorsWithSameActions() {
        final ReplParser parser = new ReplParser();
        final List<ReplTokenDescriptor> descriptorList = new ArrayList<>();
        
        descriptorList.add(keywordFoo);
        descriptorList.add(keywordBar);
        
        final ReplAction action = new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                int i = 0;
                i++;
            }
        };
        
        final ReplStatement statement1 = 
                new ReplStatement(descriptorList, action);
        
        final ReplStatement statement2 = 
                new ReplStatement(descriptorList, action);
        
        parser.addStatement(statement1);
        parser.addStatement(statement2); // Should throw.
    }
    
    @Test(expected = ReplNoMatchingStatementException.class)
    public void throwsOnUnmatchingStatement() {
        
        final ReplParser parser = new ReplParser();
        final List<ReplTokenDescriptor> descriptorList = new ArrayList<>();
        
        descriptorList.add(keywordBar);
        descriptorList.add(keywordBaz);
        descriptorList.add(new ReplInteger());
        
        final ReplAction action = new ReplAction() {
                    
            @Override
            public void act(List<Object> actionParameterList) {
                int i = 0;
                i++;
            }
        };
        
        final ReplStatement statement = 
                new ReplStatement(descriptorList, action);
        
        parser.addStatement(statement);
        parser.parse("bar baz 1.4"); // Should throw.
    }
}
