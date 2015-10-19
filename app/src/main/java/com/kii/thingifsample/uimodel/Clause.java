package com.kii.thingifsample.uimodel;

public abstract class Clause {

    public enum ClauseType {
        AND("And"),
        OR("Or"),
        EQUALS("Equals"),
        NOT_EQUALS("Not Equals"),
        GREATER_THAN("Greater Than"),
        GREATER_THAN_EQUALS("Greater Than Equals"),
        LESS_THAN("Less Than"),
        LESS_THAN_EQUALS("Less Than Equals"),
        RANGE("Range");
        private final String caption;
        private ClauseType(String caption) {
            this.caption = caption;
        }
        public String getCaption() {
            return this.caption;
        }
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
    public abstract com.kii.iotcloud.trigger.clause.Clause getClause();
    public boolean isContainer() {
        return false;
    }
}
