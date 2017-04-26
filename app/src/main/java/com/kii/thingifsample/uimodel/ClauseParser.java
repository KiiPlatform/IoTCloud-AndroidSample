package com.kii.thingifsample.uimodel;

import com.kii.thingif.clause.query.AndClauseInQuery;
import com.kii.thingif.clause.query.EqualsClauseInQuery;
import com.kii.thingif.clause.query.NotEqualsClauseInQuery;
import com.kii.thingif.clause.query.OrClauseInQuery;
import com.kii.thingif.clause.query.QueryClause;
import com.kii.thingif.clause.query.RangeClauseInQuery;
import com.kii.thingif.clause.trigger.AndClauseInTrigger;
import com.kii.thingif.clause.trigger.EqualsClauseInTrigger;
import com.kii.thingif.clause.trigger.NotEqualsClauseInTrigger;
import com.kii.thingif.clause.trigger.OrClauseInTrigger;
import com.kii.thingif.clause.trigger.RangeClauseInTrigger;
import com.kii.thingif.clause.trigger.TriggerClause;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ClauseParser {
    public static List<Clause> parseTriggerClause(TriggerClause clause) {
        List<Clause> clauses = new ArrayList<Clause>();
        parseTriggerClause(clause, clauses);
        return clauses;
    }
    private static void parseTriggerClause(TriggerClause clause, List<Clause> clauses) {
        if (clause instanceof AndClauseInTrigger) {
            clauses.add(new And.AndOpen());
            for (TriggerClause has : ((AndClauseInTrigger) clause).getClauses()) {
                parseTriggerClause(has, clauses);
            }
            clauses.add(new And.AndClose());
        } else if (clause instanceof OrClauseInTrigger) {
            clauses.add(new Or.OrOpen());
            for (TriggerClause has : ((OrClauseInTrigger) clause).getClauses()) {
                parseTriggerClause(has, clauses);
            }
            clauses.add(new Or.OrClose());
        }
        if (clause instanceof EqualsClauseInTrigger) {
            Equals equals = new Equals();
            equals.setTriggerClause(clause);
            clauses.add(equals);
        } else if (clause instanceof NotEqualsClauseInTrigger) {
            NotEquals notEquals = new NotEquals();
            notEquals.setTriggerClause(clause);
            clauses.add(notEquals);
        } else if (clause instanceof RangeClauseInTrigger) {
            RangeClauseInTrigger range = (RangeClauseInTrigger)clause;
            if (range.getLowerLimit() != null) {
                if (range.getLowerIncluded() == Boolean.TRUE) {
                    Range.GreaterThanEquals greaterThanEquals = new Range.GreaterThanEquals();
                    greaterThanEquals.setTriggerClause(range);
                    clauses.add(greaterThanEquals);
                } else {
                    Range.GreaterThan greaterThan = new Range.GreaterThan();
                    greaterThan.setTriggerClause(range);
                    clauses.add(greaterThan);
                }
            }
            if (range.getUpperLimit() != null) {
                if (range.getUpperIncluded() == Boolean.TRUE) {
                    Range.LessThanEquals lessThanEquals = new Range.LessThanEquals();
                    lessThanEquals.setTriggerClause(range);
                    clauses.add(lessThanEquals);
                } else {
                    Range.LessThan lessThan = new Range.LessThan();
                    lessThan.setTriggerClause(range);
                    clauses.add(lessThan);
                }
            }
        }
    }
    public static TriggerClause parseTriggerClause(List<Clause> clauses) {
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
                    addTriggerClauseToPeekFirst(deque, andClause);
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
                    addTriggerClauseToPeekFirst(deque, orClause);
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
                    rootClause = clause.getTriggerClause();
                } else if (deque.peekFirst() != null) {
                    addTriggerClauseToPeekFirst(deque, clause.getTriggerClause());
                } else {
                    // too many root clause
                    return null;
                }
            }
        }
        return rootClause;
    }

    private static boolean addTriggerClauseToPeekFirst(Deque<TriggerClause> deque, TriggerClause clause) {
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

    public static List<Clause> parseQueryClause(QueryClause clause) {
        List<Clause> clauses = new ArrayList<Clause>();
        parseQueryClause(clause, clauses);
        return clauses;
    }
    private static void parseQueryClause(QueryClause clause, List<Clause> clauses) {
        if (clause instanceof AndClauseInQuery) {
            clauses.add(new And.AndOpen());
            for (QueryClause has : ((AndClauseInQuery) clause).getClauses()) {
                parseQueryClause(has, clauses);
            }
            clauses.add(new And.AndClose());
        } else if (clause instanceof OrClauseInQuery) {
            clauses.add(new Or.OrOpen());
            for (QueryClause has : ((OrClauseInQuery) clause).getClauses()) {
                parseQueryClause(has, clauses);
            }
            clauses.add(new Or.OrClose());
        }
        if (clause instanceof EqualsClauseInQuery) {
            Equals equals = new Equals();
            equals.setQueryClause(clause);
            clauses.add(equals);
        } else if (clause instanceof NotEqualsClauseInQuery) {
            NotEquals notEquals = new NotEquals();
            notEquals.setQueryClause(clause);
            clauses.add(notEquals);
        } else if (clause instanceof RangeClauseInQuery) {
            RangeClauseInQuery range = (RangeClauseInQuery)clause;
            if (range.getLowerLimit() != null) {
                if (range.getLowerIncluded() == Boolean.TRUE) {
                    Range.GreaterThanEquals greaterThanEquals = new Range.GreaterThanEquals();
                    greaterThanEquals.setQueryClause(range);
                    clauses.add(greaterThanEquals);
                } else {
                    Range.GreaterThan greaterThan = new Range.GreaterThan();
                    greaterThan.setQueryClause(range);
                    clauses.add(greaterThan);
                }
            }
            if (range.getUpperLimit() != null) {
                if (range.getUpperIncluded() == Boolean.TRUE) {
                    Range.LessThanEquals lessThanEquals = new Range.LessThanEquals();
                    lessThanEquals.setQueryClause(range);
                    clauses.add(lessThanEquals);
                } else {
                    Range.LessThan lessThan = new Range.LessThan();
                    lessThan.setQueryClause(range);
                    clauses.add(lessThan);
                }
            }
        }
    }
    public static QueryClause parseQueryClause(List<Clause> clauses) {
        if (clauses.size() == 0) {
            return null;
        }
        Deque<QueryClause> deque = new ArrayDeque<>();
        QueryClause rootClause = null;

        for (Clause clause : clauses) {
            if (clause instanceof And.AndOpen) {
                if (rootClause == null) {
                    rootClause = new AndClauseInQuery();
                    deque.offerFirst(rootClause);
                } else if (deque.peekFirst() != null) {
                    AndClauseInQuery andClause = new AndClauseInQuery();
                    addQueryClauseToPeekFirst(deque, andClause);
                    deque.offerFirst(andClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof And.AndClose) {
                if (!(deque.pollFirst() instanceof AndClauseInQuery)) {
                    // position of close braces is invalid
                    return null;
                }
            } else if (clause instanceof Or.OrOpen) {
                if (rootClause == null) {
                    rootClause = new OrClauseInQuery();
                    deque.offerFirst(rootClause);
                } else if (deque.peekFirst() != null) {
                    OrClauseInQuery orClause = new OrClauseInQuery();
                    addQueryClauseToPeekFirst(deque, orClause);
                    deque.offerFirst(orClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof Or.OrClose) {
                if (!(deque.pollFirst() instanceof OrClauseInQuery)) {
                    // position of close brackets is invalid
                    return null;
                }
            } else {
                if (rootClause == null) {
                    rootClause = clause.getQueryClause();
                } else if (deque.peekFirst() != null) {
                    addQueryClauseToPeekFirst(deque, clause.getQueryClause());
                } else {
                    // too many root clause
                    return null;
                }
            }
        }
        return rootClause;
    }

    private static boolean addQueryClauseToPeekFirst(Deque<QueryClause> deque, QueryClause clause) {
        if (deque.peekFirst() instanceof AndClauseInQuery) {
        ((AndClauseInQuery) deque.peekFirst()).addClause(clause);
            return true;
        } else if (deque.peekFirst() instanceof OrClauseInQuery) {
            ((OrClauseInQuery) deque.peekFirst()).addClause(clause);
            return true;
        } else {
            return false;
        }
    }
}
