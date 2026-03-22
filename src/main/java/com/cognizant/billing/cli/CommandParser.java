package com.cognizant.billing.cli;

import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    public static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        if (line == null) return tokens;
        boolean inQuotes = false;
        StringBuilder cur = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') inQuotes = !inQuotes;
            else if (Character.isWhitespace(c) && !inQuotes) {
                if (cur.length() > 0) {
                    tokens.add(cur.toString());
                    cur.setLength(0);
                }
            } else cur.append(c);
        }
        if (cur.length() > 0) tokens.add(cur.toString());
        return tokens;
    }

    public static String getFlag(List<String> tokens, String flag, String def) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equalsIgnoreCase(flag) && i + 1 < tokens.size()) {
                return tokens.get(i + 1);
            }
        }
        return def;
    }
}