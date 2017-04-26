package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.base.BaseClause;
import com.kii.thingif.clause.query.NotEqualsClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.trigger.NotEqualsClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class NotEquals extends Clause {

    private BaseClause clause;

    @Override
    public int getIcon() {
        return R.drawable.ic_code_not_equal_black_36dp;
    }
    @Override
    public ClauseType getType() {
        return ClauseType.NOT_EQUALS;
    }
    @Override
    public String getSummary() {
        if (this.clause == null) {
            return "Not Equals";
        }
        if (this.clause instanceof NotEqualsClauseInTrigger) {
            NotEqualsClauseInTrigger trigger = (NotEqualsClauseInTrigger)this.clause;
            return trigger.getEquals().getAlias() + " : " + trigger.getEquals().getField() + " == " + trigger.getEquals().getValue().toString();
        } else if (this.clause instanceof NotEqualsClauseInQuery) {
            NotEqualsClauseInQuery query = (NotEqualsClauseInQuery) this.clause;
            return query.getEquals().getField() + " == " + query.getEquals().getValue().toString();
        } else {
            return "Invalid";
        }
    }
    @Override
    public TriggerClause getTriggerClause() {
        return (NotEqualsClauseInTrigger)this.clause;
    }
    @Override
    public void setTriggerClause(TriggerClause clause) {
        this.clause = clause;
    }
    @Override
    public QueryClause getQueryClause() {
        return (NotEqualsClauseInQuery)this.clause;
    }
    @Override
    public void setQueryClause(QueryClause clause) {
        this.clause = clause;
    }
}
