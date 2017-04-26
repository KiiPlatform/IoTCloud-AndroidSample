package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.base.BaseClause;
import com.kii.thingif.clause.query.OrClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.trigger.OrClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class Or extends ContainerClause {

    private BaseClause clause;

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
    public TriggerClause getTriggerClause() {
        return (OrClauseInTrigger)this.clause;
    }
    @Override
    public void setTriggerClause(TriggerClause clause) {
        this.clause = clause;
    }
    @Override
    public QueryClause getQueryClause() {
        return (OrClauseInQuery)this.clause;
    }
    @Override
    public void setQueryClause(QueryClause clause) {
        this.clause = clause;
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
        public OrOpen getOpen() {
            return this.open;
        }
        public void setOpen(OrOpen open) {
            this.open = open;
        }
    }
}
