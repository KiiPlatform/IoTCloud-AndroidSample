package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.base.BaseClause;
import com.kii.thingif.clause.query.EqualsClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.trigger.EqualsClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class Equals extends Clause {
    private BaseClause clause;
    @Override
    public int getIcon() {
        return R.drawable.ic_code_equal_black_36dp;
    }
    @Override
    public ClauseType getType() {
        return ClauseType.EQUALS;
    }
    @Override
    public String getSummary() {
        if (this.clause == null) {
            return "Equals";
        }
        if (this.clause instanceof EqualsClauseInTrigger) {
            EqualsClauseInTrigger trigger = (EqualsClauseInTrigger)this.clause;
            return trigger.getAlias() + " : " + trigger.getField() + " == " + trigger.getValue().toString();
        } else if (this.clause instanceof EqualsClauseInQuery) {
            EqualsClauseInQuery query = (EqualsClauseInQuery) this.clause;
            return query.getField() + " == " + query.getValue().toString();
        } else {
            return "Invalid";
        }
    }
    @Override
    public TriggerClause getTriggerClause() {
        return (EqualsClauseInTrigger)this.clause;
    }
    @Override
    public void setTriggerClause(TriggerClause clause) {
        this.clause = clause;
    }
    @Override
    public QueryClause getQueryClause() {
        return (EqualsClauseInQuery)this.clause;
    }
    @Override
    public void setQueryClause(QueryClause clause) {
        this.clause = clause;
    }
}
