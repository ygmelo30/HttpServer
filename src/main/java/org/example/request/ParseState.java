package org.example.request;

public enum ParseState {
    INITIALIZED,
    HEADER,
    BODY,
    DONE
}
