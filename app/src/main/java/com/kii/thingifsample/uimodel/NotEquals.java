package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class NotEquals extends Clause {

    private com.kii.thingif.clause.trigger.NotEqualsClauseInTrigger clause;

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
        return this.clause.getEquals().getField() + " != " + this.clause.getEquals().getValue().toString();
    }
    @Override
    public TriggerClause getClause() {
        return this.clause;
    }
    @Override
    public void setClause(TriggerClause clause) {
        this.clause = (com.kii.thingif.clause.trigger.NotEqualsClauseInTrigger)clause;
    }
}
