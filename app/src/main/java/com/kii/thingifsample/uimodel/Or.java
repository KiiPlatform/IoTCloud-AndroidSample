package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class Or extends ContainerClause {

    private com.kii.thingif.clause.trigger.OrClauseInTrigger clause;

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
        return this.getType().getCaption();
    }
    @Override
    public TriggerClause getClause() {
        return this.clause;
    }
    @Override
    public void setClause(TriggerClause clause) {
        this.clause = (com.kii.thingif.clause.trigger.OrClauseInTrigger)clause;
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
            return "";
        }
        @Override
        public TriggerClause getClause() {
            return null;
        }
        @Override
        public void setClause(TriggerClause clause) {
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
            return "";
        }
        @Override
        public TriggerClause getClause() {
            return null;
        }
        @Override
        public void setClause(TriggerClause clause) {
        }
        public OrOpen getOpen() {
            return this.open;
        }
        public void setOpen(OrOpen open) {
            this.open = open;
        }
    }
}
