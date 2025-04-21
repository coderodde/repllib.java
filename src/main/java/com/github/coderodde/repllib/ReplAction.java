package com.github.coderodde.repllib;

import java.util.List;

/**
 * This interface specifies an action for a REPL statement
 * {@link com.github.coderodde.repllib.ReplStatement}.
 * 
 * @version 1.1.0
 * @since 1.0.0
 */
public interface ReplAction {
    
    public void act(final List<Object> actionParameterList);
    
    public static int toInt(final Object integer) {
        return ((Long) integer).intValue();
    }
    
    public static long toLong(final Object integer) {
        return (long) integer;
    }
    
    public static float toFloat(final Object real) {
        return ((Double) real).floatValue();
    }
    
    public static double toDouble(final Object real) {
        return (double) real;
    }
}
