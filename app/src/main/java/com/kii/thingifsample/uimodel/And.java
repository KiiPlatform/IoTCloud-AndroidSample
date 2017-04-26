package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.base.BaseClause;
import com.kii.thingif.clause.query.AndClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.trigger.AndClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class And extends ContainerClause {

    private BaseClause clause;

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
    public TriggerClause getTriggerClause() {
        return (AndClauseInTrigger)this.clause;
    }
    @Override
    public void setTriggerClause(TriggerClause clause) {
        this.clause = clause;
    }
    @Override
    public QueryClause getQueryClause() {
        return (AndClauseInQuery)this.clause;
    }
    @Override
    public void setQueryClause(QueryClause clause) {
        this.clause = clause;
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
        public TriggerClause getTriggerClause() {
            return null;
        }
        @Override
        public void setTriggerClause(TriggerClause clause) {
        }
        @Override
        public QueryClause getQueryClause() {
            return null;
        }
        @Override
        public void setQueryClause(QueryClause clause) {
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
        public TriggerClause getTriggerClause() {
            return null;
        }
        @Override
        public void setTriggerClause(TriggerClause clause) {
        }
        @Override
        public QueryClause getQueryClause() {
            return null;
        }
        @Override
        public void setQueryClause(QueryClause clause) {
        }
        public AndOpen getOpen() {
            return this.open;
        }
        public void setOpen(AndOpen open)  {
            this.open = open;
        }
    }
}
