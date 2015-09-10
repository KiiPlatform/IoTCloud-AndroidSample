package com.kii.iotcloudsample.model;

public abstract class Clause {

    public enum ClauseType {
        AND,
        OR,
        EQUALS,
        NOT_EQUALS,
        RANGE
    }
    public static final Clause[] ALL_CLAUSES = {
            new And(),
            new Or(),
            new Equals(),
            new NotEquals(),
            new Range.GreaterThan(),
            new Range.GreaterThanEquals(),
            new Range.LessThan(),
            new Range.LessThanEquals()
    };

    public abstract int getIcon();
    public abstract ClauseType getType();
    public abstract String getSummary();
    public abstract void setClause(com.kii.iotcloud.trigger.clause.Clause clause);
    public boolean isContainer() {
        return false;
    }
}
