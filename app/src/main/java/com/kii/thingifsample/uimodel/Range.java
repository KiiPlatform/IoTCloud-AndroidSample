package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.base.BaseClause;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.query.RangeClauseInQuery;
import com.kii.thingif.clause.trigger.RangeClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class Range extends Clause {

    @Override
    public int getIcon() {
        return 0;
    }
    @Override
    public ClauseType getType() {
        return ClauseType.RANGE;
    }
    @Override
    public String getSummary() {
        return "";
    }
    @Override
    public TriggerClause getTriggerClause() {
        return null;
    }
    public void setTriggerClause(TriggerClause clause) {
    }
    public QueryClause getQueryClause() {
        return null;
    }
    public void setQueryClause(QueryClause clause) {
    }


    public static class GreaterThan extends Clause {
        private BaseClause clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_greater_than_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.GREATER_THAN;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return this.getType().getCaption();
            }
            if (this.clause instanceof RangeClauseInTrigger) {
                RangeClauseInTrigger trigger = (RangeClauseInTrigger)this.clause;
                return trigger.getAlias() + " : " + trigger.getField() + " > " + trigger.getLowerLimit();
            } else if (this.clause instanceof RangeClauseInQuery) {
                RangeClauseInQuery query = (RangeClauseInQuery) this.clause;
                return query.getField() + " > " + query.getLowerLimit();
            } else {
                return "Invalid";
            }
        }
        @Override
        public TriggerClause getTriggerClause() {
            return (RangeClauseInTrigger)this.clause;
        }
        @Override
        public void setTriggerClause(TriggerClause clause) {
            this.clause = clause;
        }
        @Override
        public QueryClause getQueryClause() {
            return (RangeClauseInQuery)this.clause;
        }
        @Override
        public void setQueryClause(QueryClause clause) {
            this.clause = clause;
        }
    }
    public static class GreaterThanEquals extends Clause {
        private BaseClause clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_greater_than_equal_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.GREATER_THAN_EQUALS;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return this.getType().getCaption();
            }
            if (this.clause instanceof RangeClauseInTrigger) {
                RangeClauseInTrigger trigger = (RangeClauseInTrigger)this.clause;
                return trigger.getAlias() + " : " + trigger.getField() + " >= " + trigger.getLowerLimit();
            } else if (this.clause instanceof RangeClauseInQuery) {
                RangeClauseInQuery query = (RangeClauseInQuery) this.clause;
                return query.getField() + " >= " + query.getLowerLimit();
            } else {
                return "Invalid";
            }
        }
        @Override
        public TriggerClause getTriggerClause() {
            return (RangeClauseInTrigger)this.clause;
        }
        @Override
        public void setTriggerClause(TriggerClause clause) {
            this.clause = clause;
        }
        @Override
        public QueryClause getQueryClause() {
            return (RangeClauseInQuery)this.clause;
        }
        @Override
        public void setQueryClause(QueryClause clause) {
            this.clause = clause;
        }
    }
    public static class LessThan extends Clause {
        private BaseClause clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_less_than_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.LESS_THAN;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return this.getType().getCaption();
            }
            if (this.clause instanceof RangeClauseInTrigger) {
                RangeClauseInTrigger trigger = (RangeClauseInTrigger)this.clause;
                return trigger.getAlias() + " : " + trigger.getField() + " < " + trigger.getUpperLimit();
            } else if (this.clause instanceof RangeClauseInQuery) {
                RangeClauseInQuery query = (RangeClauseInQuery) this.clause;
                return query.getField() + " < " + query.getUpperLimit();
            } else {
                return "Invalid";
            }
        }
        @Override
        public TriggerClause getTriggerClause() {
            return (RangeClauseInTrigger)this.clause;
        }
        @Override
        public void setTriggerClause(TriggerClause clause) {
            this.clause = clause;
        }
        @Override
        public QueryClause getQueryClause() {
            return (RangeClauseInQuery)this.clause;
        }
        @Override
        public void setQueryClause(QueryClause clause) {
            this.clause = clause;
        }
    }
    public static class LessThanEquals extends Clause {
        private BaseClause clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_less_than_or_equal_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.LESS_THAN_EQUALS;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return this.getType().getCaption();
            }
            if (this.clause instanceof RangeClauseInTrigger) {
                RangeClauseInTrigger trigger = (RangeClauseInTrigger)this.clause;
                return trigger.getAlias() + " : " + trigger.getField() + " <= " + trigger.getUpperLimit();
            } else if (this.clause instanceof RangeClauseInQuery) {
                RangeClauseInQuery query = (RangeClauseInQuery) this.clause;
                return query.getField() + " <= " + query.getUpperLimit();
            } else {
                return "Invalid";
            }
        }
        @Override
        public TriggerClause getTriggerClause() {
            return (RangeClauseInTrigger)this.clause;
        }
        @Override
        public void setTriggerClause(TriggerClause clause) {
            this.clause = clause;
        }
        @Override
        public QueryClause getQueryClause() {
            return (RangeClauseInQuery)this.clause;
        }
        @Override
        public void setQueryClause(QueryClause clause) {
            this.clause = clause;
        }
    }
}
