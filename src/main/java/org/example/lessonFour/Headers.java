package org.example.lessonFour;

import java.util.HashMap;
import java.util.Map;

public class Headers {
    private Map<String, String> values = new HashMap<>();
    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(String key, String value) {
        values.put(key, value);
    }


    public boolean hasHeader (String key) {
        if(values.get(key) != null) {
            return true;
        }
        return false;
    }

    public String getHeader (String key) {
        return values.get(key);
    }

    public void printAllHeaders () {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ": " + value);
        }
    }
    public StringBuilder getAllHeaders () {
        StringBuilder str = new StringBuilder();
        String currentHeader;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            currentHeader = key + ": " + value;
            str.append(currentHeader + "\r\n");
        }
        return str;
    }
}
