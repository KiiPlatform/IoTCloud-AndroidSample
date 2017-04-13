package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.trigger.AndClauseInTrigger;
import com.kii.thingif.clause.trigger.OrClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ClauseParser {
    public static List<Clause> parseClause(TriggerClause clause) {
        List<Clause> clauses = new ArrayList<Clause>();
        parseClause(clause, clauses);
        return clauses;
    }
    private static void parseClause(TriggerClause clause, List<Clause> clauses) {
        if (clause instanceof com.kii.thingif.clause.trigger.AndClauseInTrigger) {
            clauses.add(new And.AndOpen());
        } else if (clause instanceof com.kii.thingif.clause.trigger.OrClauseInTrigger) {
            clauses.add(new Or.OrOpen());
        }
        if (clause instanceof com.kii.thingif.clause.trigger.EqualsClauseInTrigger) {
            Equals equals = new Equals();
            equals.setClause(clause);
            clauses.add(equals);
        } else if (clause instanceof com.kii.thingif.clause.trigger.NotEqualsClauseInTrigger) {
            NotEquals notEquals = new NotEquals();
            notEquals.setClause(clause);
            clauses.add(notEquals);
        } else if (clause instanceof com.kii.thingif.clause.trigger.RangeClauseInTrigger) {
            com.kii.thingif.clause.trigger.RangeClauseInTrigger range =
                    (com.kii.thingif.clause.trigger.RangeClauseInTrigger)clause;
            if (range.getLowerLimit() != null) {
                if (range.getLowerIncluded() == Boolean.TRUE) {
                    Range.GreaterThanEquals greaterThanEquals = new Range.GreaterThanEquals();
                    greaterThanEquals.setClause(range);
                    clauses.add(greaterThanEquals);
                } else {
                    Range.GreaterThan greaterThan = new Range.GreaterThan();
                    greaterThan.setClause(range);
                    clauses.add(greaterThan);
                }
            }
            if (range.getUpperLimit() != null) {
                if (range.getUpperIncluded() == Boolean.TRUE) {
                    Range.LessThanEquals lessThanEquals = new Range.LessThanEquals();
                    lessThanEquals.setClause(range);
                    clauses.add(lessThanEquals);
                } else {
                    Range.LessThan lessThan = new Range.LessThan();
                    lessThan.setClause(range);
                    clauses.add(lessThan);
                }
            }
        }
    }
    public static TriggerClause parseClause(List<Clause> clauses) {
        if (clauses.size() == 0) {
            return null;
        }
        Deque<TriggerClause> deque = new ArrayDeque<TriggerClause>();
        TriggerClause rootClause = null;

        for (Clause clause : clauses) {
            if (clause instanceof And.AndOpen) {
                if (rootClause == null) {
                    rootClause = new AndClauseInTrigger();
                    deque.offerFirst(rootClause);
                } else if (deque.peekFirst() != null) {
                    AndClauseInTrigger andClause = new AndClauseInTrigger();
                    addClauseToPeekFirst(deque, andClause);
                    deque.offerFirst(andClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof And.AndClose) {
                if (!(deque.pollFirst() instanceof AndClauseInTrigger)) {
                    // position of close braces is invalid
                    return null;
                }
            } else if (clause instanceof Or.OrOpen) {
                if (rootClause == null) {
                    rootClause = new OrClauseInTrigger();
                    deque.offerFirst(rootClause);
                } else if (deque.peekFirst() != null) {
                    OrClauseInTrigger orClause = new OrClauseInTrigger();
                    addClauseToPeekFirst(deque, orClause);
                    deque.offerFirst(orClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof Or.OrClose) {
                if (!(deque.pollFirst() instanceof OrClauseInTrigger)) {
                    // position of close brackets is invalid
                    return null;
                }
            } else {
                if (rootClause == null) {
                    rootClause = clause.getClause();
                } else if (deque.peekFirst() != null) {
                    addClauseToPeekFirst(deque, clause.getClause());
                } else {
                    // too many root clause
                    return null;
                }
            }
        }
        return rootClause;
    }

    private static boolean addClauseToPeekFirst(Deque<TriggerClause> deque, TriggerClause clause) {
        if (deque.peekFirst() instanceof AndClauseInTrigger) {
            ((AndClauseInTrigger) deque.peekFirst()).addClause(clause);
            return true;
        } else if (deque.peekFirst() instanceof OrClauseInTrigger) {
            ((OrClauseInTrigger) deque.peekFirst()).addClause(clause);
            return true;
        } else {
            return false;
        }
    }
}
