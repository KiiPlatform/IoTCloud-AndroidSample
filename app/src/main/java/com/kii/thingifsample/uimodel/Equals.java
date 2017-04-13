package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.trigger.TriggerClause;
import com.kii.thingifsample.R;

public class Equals extends Clause {
    private com.kii.thingif.clause.trigger.EqualsClauseInTrigger clause;
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
        return this.clause.getField() + " == " + this.clause.getValue().toString();
    }
    @Override
    public TriggerClause getClause() {
        return this.clause;
    }
    @Override
    public void setClause(TriggerClause clause) {
        this.clause = (com.kii.thingif.clause.trigger.EqualsClauseInTrigger)clause;
    }
}
