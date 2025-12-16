
package org.example.service;

import java.util.Collections;
import java.util.List;

public class ValidationResult {
    private final List<String> errors;
    private final List<String> warnings;
    private final boolean historyUpdated;

    public ValidationResult(List<String> errors, List<String> warnings, boolean historyUpdated) {
        this.errors = errors;
        this.warnings = warnings;
        this.historyUpdated = historyUpdated;
    }

    public boolean isValid() { return errors.isEmpty(); }
    public boolean historyUpdated() { return historyUpdated; }
    public List<String> errors()   { return Collections.unmodifiableList(errors); }
    public List<String> warnings() { return Collections.unmodifiableList(warnings); }
}
