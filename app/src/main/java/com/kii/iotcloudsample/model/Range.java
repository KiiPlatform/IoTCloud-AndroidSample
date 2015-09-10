package com.kii.iotcloudsample.model;

import com.kii.iotcloudsample.R;

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
    public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
    }


    public static class GreaterThan extends Clause {
        private com.kii.iotcloud.trigger.clause.Range clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_greater_than_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.RANGE;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return "Greater Than";
            }
            return this.clause.getField() + " > " + this.clause.getLowerLimit();
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
            this.clause = (com.kii.iotcloud.trigger.clause.Range)clause;
        }
    }
    public static class GreaterThanEquals extends Clause {
        private com.kii.iotcloud.trigger.clause.Range clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_greater_than_equal_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.RANGE;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return "Greater Than Equals";
            }
            return this.clause.getField() + " >= " + this.clause.getLowerLimit();
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
            this.clause = (com.kii.iotcloud.trigger.clause.Range)clause;
        }
    }
    public static class LessThan extends Clause {
        private com.kii.iotcloud.trigger.clause.Range clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_less_than_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.RANGE;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return "Less Than";
            }
            return this.clause.getField() + " < " + this.clause.getUpperLimit();
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
            this.clause = (com.kii.iotcloud.trigger.clause.Range)clause;
        }
    }
    public static class LessThanEquals extends Clause {
        private com.kii.iotcloud.trigger.clause.Range clause;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_less_than_or_equal_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.RANGE;
        }
        @Override
        public String getSummary() {
            if (this.clause == null) {
                return "Less Than Equals";
            }
            return this.clause.getField() + " <= " + this.clause.getUpperLimit();
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
            this.clause = (com.kii.iotcloud.trigger.clause.Range)clause;
        }
    }
}
