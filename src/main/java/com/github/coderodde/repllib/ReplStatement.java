package com.github.coderodde.repllib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a REPL statement consisting of a list of token 
 * descriptors.
 * 
 * @version 1.0.1
 * @since 1.0.0
 */
public final class ReplStatement {
    
    private static final String TO_STRING_INTEGER = "<integer>";
    private static final String TO_STRING_REAL    = "<real>";
    private static final String TO_STRING_STRING  = "<string>";
    
    private final List<ReplTokenDescriptor> tokenDescriptorList = 
            new ArrayList<>();
    
    private final ReplAction action;
    
    public ReplStatement(final List<ReplTokenDescriptor> tokenDescriptorList,
                         final ReplAction action) {
        
        Objects.requireNonNull(tokenDescriptorList, 
                               "Token descriptor list is null");
        
        Objects.requireNonNull(action, "Input action is null");
        
        if (tokenDescriptorList.isEmpty()) {
            throw new IllegalArgumentException(
                    "Token descriptor list is empty");
        }
        
        this.tokenDescriptorList.addAll(tokenDescriptorList);
        this.action = action;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder().append("[");
        boolean first = true;
        
        for (final ReplTokenDescriptor descriptor : tokenDescriptorList) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            
            if (descriptor instanceof ReplKeyword) {
                sb.append(((ReplKeyword) descriptor).getKeyword());
            } else if (descriptor instanceof ReplInteger) {
                sb.append(TO_STRING_INTEGER);
            } else if (descriptor instanceof ReplReal) {
                sb.append(TO_STRING_REAL);
            } else if (descriptor instanceof ReplString) {
                sb.append(TO_STRING_STRING);
            }
        }
        
        return sb.append("]").toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        
        if (o == null) {
            return false;
        }
        
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        
        final ReplStatement other = (ReplStatement) o;
        
        if (tokenDescriptorList.size() != other.tokenDescriptorList.size()) {
            return false;
        }
        
        for (int i = 0; i < tokenDescriptorList.size(); i++) {
            final ReplTokenDescriptor token1 = tokenDescriptorList.get(i);
            final ReplTokenDescriptor token2 = other.tokenDescriptorList.get(i);
            
            if (token1 instanceof ReplKeyword) {
                if (!(token2 instanceof ReplKeyword)) {
                    return false;
                }
                
                final String keyword1 = ((ReplKeyword) token1).getKeyword();
                final String keyword2 = ((ReplKeyword) token2).getKeyword();
                
                if (!keyword1.equals(keyword2)) {
                    return false;
                }
            } else if (token1 instanceof ReplString) {
                if (!(token2 instanceof ReplString)) {
                    return false;
                }
            } else if (token1 instanceof ReplInteger) {
                if (!(token2 instanceof ReplInteger)) {
                    return false;
                }
            } else if (token1 instanceof ReplReal) {
                if (!(token2 instanceof ReplReal)) {
                    return false;
                }
            } else {
                throw new IllegalArgumentException("Should not get here");
            }
        }
        
        return true;
    }
    
    boolean matchesCommandLine(final String commandLine) {
        final String[] commandLineParts = commandLine.split("\\s");
        
        if (commandLineParts.length != tokenDescriptorList.size()) {
            return false;
        }
        
        for (int i = 0; i < tokenDescriptorList.size(); i++) {
            final ReplTokenDescriptor descriptor = tokenDescriptorList.get(i);
            final String commandLinePart = commandLineParts[i];
            
            if (descriptor instanceof ReplKeyword) {
                final String keyword = ((ReplKeyword) descriptor).getKeyword();
                
                if (!commandLinePart.equals(keyword)) {
                    return false;
                }
            } else if (descriptor instanceof ReplInteger) {
                if (!stringIsInteger(commandLinePart)) {
                    return false;
                }
            } else if (descriptor instanceof ReplReal) {
                if (!stringIsReal(commandLinePart)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    void actImpl(final String commandLine) {
        final String[] commandLineParts = commandLine.split("\\s");
        final List<Object> actionParameterList = new ArrayList<>();
        
        for (int i = 0; i < commandLineParts.length; i++) {
            final ReplTokenDescriptor descriptor = tokenDescriptorList.get(i);
          
            if (descriptor instanceof ReplKeyword) {
                continue;
            }
            
            Object o = null;
            final String argument = commandLineParts[i];
            
            if (descriptor instanceof ReplInteger) {
                actionParameterList.add((Object) Long.parseLong(argument));
            } else if (descriptor instanceof ReplReal) {
                actionParameterList.add((Object) Double.parseDouble(argument));
            } else if (descriptor instanceof ReplString) {
                actionParameterList.add((Object) argument);
            }
        }
        
        action.act(actionParameterList);
    }
    
    private static boolean stringIsInteger(final String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (final NumberFormatException ex) {
            return false;
        }
    }
    
    private static boolean stringIsReal(final String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (final NumberFormatException ex) {
            return false;
        }
    }
}
