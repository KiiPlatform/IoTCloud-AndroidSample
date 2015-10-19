package com.kii.thingifsample.uimodel;

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
    public com.kii.thingif.trigger.clause.Clause getClause() {
        return null;
    }
    public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
    }


    public static class GreaterThan extends Clause {
        private com.kii.thingif.trigger.clause.Range clause;
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
            return this.clause.getField() + " > " + this.clause.getLowerLimit();
        }
        @Override
        public com.kii.thingif.trigger.clause.Clause getClause() {
            return this.clause;
        }
        @Override
        public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
            this.clause = (com.kii.thingif.trigger.clause.Range)clause;
        }
    }
    public static class GreaterThanEquals extends Clause {
        private com.kii.thingif.trigger.clause.Range clause;
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
            return this.clause.getField() + " >= " + this.clause.getLowerLimit();
        }
        @Override
        public com.kii.thingif.trigger.clause.Clause getClause() {
            return this.clause;
        }
        @Override
        public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
            this.clause = (com.kii.thingif.trigger.clause.Range)clause;
        }
    }
    public static class LessThan extends Clause {
        private com.kii.thingif.trigger.clause.Range clause;
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
            return this.clause.getField() + " < " + this.clause.getUpperLimit();
        }
        @Override
        public com.kii.thingif.trigger.clause.Clause getClause() {
            return this.clause;
        }
        @Override
        public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
            this.clause = (com.kii.thingif.trigger.clause.Range)clause;
        }
    }
    public static class LessThanEquals extends Clause {
        private com.kii.thingif.trigger.clause.Range clause;
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
            return this.clause.getField() + " <= " + this.clause.getUpperLimit();
        }
        @Override
        public com.kii.thingif.trigger.clause.Clause getClause() {
            return this.clause;
        }
        @Override
        public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
            this.clause = (com.kii.thingif.trigger.clause.Range)clause;
        }
    }
}
