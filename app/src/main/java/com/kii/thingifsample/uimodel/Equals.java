package com.kii.thingifsample.uimodel;

import com.kii.thingifsample.R;

public class Equals extends Clause {
    private com.kii.iotcloud.trigger.clause.Equals clause;
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
    public com.kii.iotcloud.trigger.clause.Clause getClause() {
        return this.clause;
    }
    @Override
    public void setClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        this.clause = (com.kii.iotcloud.trigger.clause.Equals)clause;
    }
}
