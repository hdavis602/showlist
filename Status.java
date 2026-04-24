package edu.csc435.showlist;

public enum Status {
    COMPLETED("Completed"),
    IN_PROGRESS("In Progress"),
    PLAN_TO_WATCH("Plan to Watch");

    private final String string;

    Status(String status) {string = status;}

    public String toString() {
        return string;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.toString().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status " + status);
    }
}