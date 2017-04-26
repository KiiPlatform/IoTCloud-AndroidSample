package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.trigger.TriggerClause;

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
    public abstract void setTriggerClause(TriggerClause clause);
    public abstract TriggerClause getTriggerClause();
    public abstract void setQueryClause(QueryClause clause);
    public abstract QueryClause getQueryClause();
    public boolean isContainer() {
        return false;
    }
}
