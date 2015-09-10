package com.kii.iotcloudsample.model;

import com.kii.iotcloudsample.R;

public class Or extends ContainerClause {

    private com.kii.iotcloud.trigger.clause.Or clause;

    @Override
    public int getIcon() {
        return R.drawable.ic_code_or_black_36dp;
    }
    @Override
    public ClauseType getType() {
        return ClauseType.OR;
    }
    @Override
    public String getSummary() {
        return "OR";
    }
    @Override
    public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        this.clause = (com.kii.iotcloud.trigger.clause.Or)clause;
    }

    public static class OrOpen extends ContainerClause {
        private OrClose close;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_or_open_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.OR;
        }
        @Override
        public String getSummary() {
            return "OR";
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        }
        public OrClose getClose() {
            return this.close;
        }
        public void setClose(OrClose close) {
            this.close = close;
        }
    }
    public static class OrClose extends ContainerClause {
        private OrOpen open;
        @Override
        public int getIcon() {
            return R.drawable.ic_code_or_close_black_36dp;
        }
        @Override
        public ClauseType getType() {
            return ClauseType.OR;
        }
        @Override
        public String getSummary() {
            return "OR";
        }
        @Override
        public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        }
        public OrOpen getOpen() {
            return this.open;
        }
        public void setOpen(OrOpen open) {
            this.open = open;
        }
    }
}
