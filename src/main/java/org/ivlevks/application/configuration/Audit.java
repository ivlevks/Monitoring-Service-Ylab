package org.ivlevks.application.configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Audit {

    private static ArrayList<String> allOperations = new ArrayList<>();

    public static void addInfoInAudit(String information) {
        allOperations.add(LocalDateTime.now().toString() + "  " + information);
    }

}
