package com.kii.iotcloudsample.model;

import com.kii.iotcloudsample.R;

public class And extends ContainerClause {

    private com.kii.iotcloud.trigger.clause.And clause;

    @Override
    public int getIcon() {
        return R.drawable.ic_code_and_black_36dp;
    }
    @Override
    public ClauseType getType() {
        return ClauseType.AND;
    }
    @Override
    public String getSummary() {
        return "AND";
    }
    @Override
    public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        this.clause = (com.kii.iotcloud.trigger.clause.And)clause;
    }

    public static class AndOpen extends ContainerClause {
        private AndClose close;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_and_open_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.AND;
        }
        @Override
        public String getSummary() {
            return "AND";
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        }
        public AndClose getClose() {
            return this.close;
        }
        public void setClose(AndClose close)  {
            this.close = close;
        }
    }
    public static class AndClose extends ContainerClause {
        private AndOpen open;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_and_close_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.AND;
        }
        @Override
        public String getSummary() {
            return "AND";
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        }
        public AndOpen getClose() {
            return this.open;
        }
        public void setClose(AndOpen open)  {
            this.open = open;
        }
    }
}
