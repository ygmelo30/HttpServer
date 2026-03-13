package org.example.httpserver;

import java.util.ArrayList;
import java.util.List;

public class SpecialEndpointRegistry {
    public List<SpecialEndpoint> registry = new ArrayList<>();

    public void register(SpecialEndpoint specialEndpoint) {
        registry.add(specialEndpoint);
    }
    public SpecialEndpoint resolve(String path) {
        for (SpecialEndpoint specialEndpoint : registry) {
            if(specialEndpoint.matches(path)) {
                return specialEndpoint;
            }
        }
        return null;
    }
}
