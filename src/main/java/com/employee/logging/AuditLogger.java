package com.employee.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuditLogger {

    
    private static final Logger audit =
            LoggerFactory.getLogger("AUDIT");

    private AuditLogger() {}

    public static void log(String user, String action, String target) {
        audit.info("user={} action={} target={}", user, action, target);
    }
}
