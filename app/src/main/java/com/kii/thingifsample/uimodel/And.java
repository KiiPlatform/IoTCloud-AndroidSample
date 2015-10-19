package com.kii.thingifsample.uimodel;

import com.kii.thingifsample.R;

public class And extends ContainerClause {

    private com.kii.thingif.trigger.clause.And clause;

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
        return this.getType().getCaption();
    }
    @Override
    public com.kii.thingif.trigger.clause.Clause getClause() {
        return this.clause;
    }
    @Override
    public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
        this.clause = (com.kii.thingif.trigger.clause.And)clause;
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
            return "";
        }
        @Override
        public com.kii.thingif.trigger.clause.Clause getClause() {
            return null;
        }
        @Override
        public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
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
            return "";
        }
        @Override
        public com.kii.thingif.trigger.clause.Clause getClause() {
            return null;
        }
        @Override
        public void setClause(com.kii.thingif.trigger.clause.Clause clause) {
        }
        public AndOpen getOpen() {
            return this.open;
        }
        public void setOpen(AndOpen open)  {
            this.open = open;
        }
    }
}
