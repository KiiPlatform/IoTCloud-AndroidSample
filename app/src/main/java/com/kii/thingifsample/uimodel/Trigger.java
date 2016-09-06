package com.kii.thingifsample.uimodel;

import android.util.Pair;

import com.kii.thingif.Target;
import com.kii.thingif.TypedID;
import com.kii.thingif.command.Action;
import com.kii.thingif.trigger.StatePredicate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Trigger {
    private String triggerID;
    private TypedID targetID;
    private List<Action> actions = new ArrayList<Action>();
    private StatePredicate predicate = null;
    private ServerCode serverCode = null;
    private Target commandTarget = null;

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
        this.targetID = trigger.getTargetID();
    }

    public String getTriggerID() {
        return this.triggerID;
    }
    public TypedID getTargetID() { return this.targetID; }
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
    public Target getCommandTarget() { return this.commandTarget; }
    public void setCommandTarget(Target commandTarget) { this.commandTarget = commandTarget; }

    public static class ServerCode {
        public String endpoint;
        public String executorAccessToken;
        public String targetAppID;
        public List<Pair<String, Object>> parameters = new ArrayList<Pair<String, Object>>();
        public ServerCode() {
        }
        public ServerCode(com.kii.thingif.trigger.ServerCode serverCode) {
            this.endpoint = serverCode.getEndpoint();
            this.executorAccessToken = serverCode.getExecutorAccessToken();
            this.targetAppID = serverCode.getTargetAppID();
            if (serverCode.getParameters() != null) {
                JSONObject json = serverCode.getParameters();
                for(Iterator<String> keys = json.keys(); keys.hasNext();) {
                    String key = keys.next();
                    try {
                        parameters.add(new Pair<String, Object>(key, json.get(key)));
                    } catch (JSONException ignore) {
                    }
                }
            }
        }
    }
}
