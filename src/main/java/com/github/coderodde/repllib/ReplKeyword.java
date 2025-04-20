package com.github.coderodde.repllib;

import java.util.Objects;

/**
 *
 */
public final class ReplKeyword implements ReplTokenDescriptor {
    
    private final String keyword;
    
    public ReplKeyword(final String keyword) {
        Objects.requireNonNull(keyword, "The input keyword is null");
        
        if (keyword.isBlank()) {
            throw new IllegalArgumentException("The input keyword is blank");
        }
        
        this.keyword = keyword;
    }
    
    public String getKeyword() {
        return keyword;
    }
}
