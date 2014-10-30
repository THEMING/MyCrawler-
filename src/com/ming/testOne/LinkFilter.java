package com.ming.testOne;

public class LinkFilter {
    String[] seeds;
    
    public LinkFilter(String[] seeds) {
        this.seeds = seeds;
    }

    public boolean accept(String url) {
        for(String ss:seeds){
            if (url.startsWith(ss))
                return true;                
        }
        return false;
       }
}
