package com.kii.thingifsample.uimodel;

import com.kii.thingif.command.Action;
import com.kii.thingif.trigger.StatePredicate;

import java.util.ArrayList;
import java.util.List;

public class Trigger {
    private String triggerID;
    private List<Action> actions = new ArrayList<Action>();
    private StatePredicate predicate = null;
    private ServerCode serverCode = null;

    public Trigger() {
    }
    public Trigger(com.kii.thingif.trigger.Trigger trigger) {
        if (trigger.getCommand() != null) {
            for (Action action : trigger.getCommand().getActions()) {
                this.actions.add(action);
            }
        } else {
            this.serverCode = new ServerCode(trigger.getServerCode());
        }
        // TODO:Supports Schedule Predicate
        this.predicate = (StatePredicate)trigger.getPredicate();
        this.triggerID = trigger.getTriggerID();
    }

    public String getTriggerID() {
        return this.triggerID;
    }
    public void clearActions() {
        this.actions.clear();
    }
    public void addAction(Action action) {
        this.actions.add(action);
    }
    public List<Action> getActions() {
        return this.actions;
    }
    public ServerCode getServerCode() {
        return this.serverCode;
    }
    public void setServerCode(ServerCode serverCode) {
        this.serverCode = serverCode;
    }
    public StatePredicate getPredicate() {
        return this.predicate;
    }
    public void setPredicate(StatePredicate predicate) {
        this.predicate = predicate;
    }

    public static class ServerCode {
        public String endpoint;
        public String executorAccessToken;
        public String targetAppID;
        public ServerCode() {
        }
        public ServerCode(com.kii.thingif.trigger.ServerCode serverCode) {
            this.endpoint = serverCode.getEndpoint();
            this.executorAccessToken = serverCode.getExecutorAccessToken();
            this.targetAppID = serverCode.getTargetAppID();
        }
    }
}
