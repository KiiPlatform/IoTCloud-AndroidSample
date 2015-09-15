package com.kii.iotcloudsample.model;

import com.kii.iotcloud.trigger.clause.ContainerClause;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ClauseParser {
    public static List<Clause> parseClause(com.kii.iotcloud.trigger.clause.Clause clause) {
        List<Clause> clauses = new ArrayList<Clause>();
        parseClause(clause, clauses);
        return clauses;
    }
    private static void parseClause(com.kii.iotcloud.trigger.clause.Clause clause, List<Clause> clauses) {
        if (clause instanceof com.kii.iotcloud.trigger.clause.ContainerClause) {
            com.kii.iotcloud.trigger.clause.ContainerClause container = (com.kii.iotcloud.trigger.clause.ContainerClause)clause;
            if (container instanceof com.kii.iotcloud.trigger.clause.And) {
                clauses.add(new And.AndOpen());
            } else if (container instanceof com.kii.iotcloud.trigger.clause.Or) {
                clauses.add(new Or.OrOpen());
            }
            for (com.kii.iotcloud.trigger.clause.Clause innerClause : container.getClauses()) {
                parseClause(innerClause, clauses);
            }
            if (container instanceof com.kii.iotcloud.trigger.clause.And) {
                clauses.add(new And.AndClose());
            } else if (container instanceof com.kii.iotcloud.trigger.clause.Or) {
                clauses.add(new Or.OrClose());
            }
        } else {
            if (clause instanceof com.kii.iotcloud.trigger.clause.Equals) {
                Equals equals = new Equals();
                equals.setClause(clause);
                clauses.add(equals);
            } else if (clause instanceof com.kii.iotcloud.trigger.clause.NotEquals) {
                NotEquals notEquals = new NotEquals();
                notEquals.setClause(clause);
                clauses.add(notEquals);
            } else if (clause instanceof com.kii.iotcloud.trigger.clause.Range) {
                com.kii.iotcloud.trigger.clause.Range range = (com.kii.iotcloud.trigger.clause.Range)clause;
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
    }
    public static com.kii.iotcloud.trigger.clause.Clause parseClause(List<Clause> clauses) {
        if (clauses.size() == 0) {
            return null;
        }
        Deque<com.kii.iotcloud.trigger.clause.ContainerClause> deque = new ArrayDeque<ContainerClause>();
        com.kii.iotcloud.trigger.clause.Clause rootClause = null;

        for (Clause clause : clauses) {
            if (clause instanceof And.AndOpen) {
                if (rootClause == null) {
                    rootClause = new com.kii.iotcloud.trigger.clause.And();
                    deque.offerFirst((com.kii.iotcloud.trigger.clause.ContainerClause)rootClause);
                } else if (deque.peekFirst() != null) {
                    com.kii.iotcloud.trigger.clause.And andClause = new com.kii.iotcloud.trigger.clause.And();
                    deque.peekFirst().addClause(andClause);
                    deque.offerFirst(andClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof And.AndClose) {
                if (!(deque.pollFirst() instanceof com.kii.iotcloud.trigger.clause.And)) {
                    // position of close braces is invalid
                    return null;
                }
            } else if (clause instanceof Or.OrOpen) {
                if (rootClause == null) {
                    rootClause = new com.kii.iotcloud.trigger.clause.Or();
                    deque.offerFirst((com.kii.iotcloud.trigger.clause.ContainerClause)rootClause);
                } else if (deque.peekFirst() != null) {
                    com.kii.iotcloud.trigger.clause.Or orClause = new com.kii.iotcloud.trigger.clause.Or();
                    deque.peekFirst().addClause(orClause);
                    deque.offerFirst(orClause);
                } else {
                    // too many root clause
                    return null;
                }
            } else if (clause instanceof Or.OrClose) {
                if (!(deque.pollFirst() instanceof com.kii.iotcloud.trigger.clause.Or)) {
                    // position of close brackets is invalid
                    return null;
                }
            } else {
                if (rootClause == null) {
                    rootClause = clause.getClause();
                } else if (deque.peekFirst() != null) {
                    deque.peekFirst().addClause(clause.getClause());
                } else {
                    // too many root clause
                    return null;
                }
            }
        }
        return rootClause;
    }
}
