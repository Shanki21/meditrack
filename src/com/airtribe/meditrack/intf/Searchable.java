package com.airtribe.meditrack.intf;

// Contract for anything that can be searched by a keyword (Doctor, Patient, etc.)
public interface Searchable {

    boolean matches(String keyword);

    // Default helper — prints whether a keyword matched, reusing matches() internally
    default void printMatchResult(String keyword) {
        if (matches(keyword)) {
            System.out.println("Match found for: " + keyword);
        } else {
            System.out.println("No match for: " + keyword);
        }
    }
}