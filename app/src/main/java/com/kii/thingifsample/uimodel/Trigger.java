package com.kii.thingifsample.uimodel;

import android.util.Pair;

import com.kii.thingif.TypedID;
import com.kii.thingif.command.Action;
import com.kii.thingif.trigger.Condition;
import com.kii.thingif.trigger.StatePredicate;
import com.kii.thingif.trigger.TriggerOptions;
import com.kii.thingif.trigger.TriggersWhen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Trigger {
    private String triggerID;
    private List<Action> actions = new ArrayList<Action>();
    private TypedID commandTargetID = null;
    private String commandTitle = null;
    private String commandDescription = null;
    private JSONObject commandMetadata = null;
    private Condition condition = null;
    private TriggersWhen triggersWhen = null;
    private ServerCode serverCode = null;
    private TriggerOptions options = null;

    public Trigger() {
    }
    public Trigger(com.kii.thingif.trigger.Trigger trigger) {
        if (trigger.getCommand() != null) {
            for (Action action : trigger.getCommand().getActions()) {
                this.actions.add(action);
            }
            this.commandTargetID = trigger.getCommand().getTargetID();
        } else {
            this.serverCode = new ServerCode(trigger.getServerCode());
        }
        // TODO:Supports Schedule Predicate
        StatePredicate predicate = (StatePredicate)trigger.getPredicate();
        this.condition = predicate.getCondition();
        this.triggersWhen = predicate.getTriggersWhen();
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
    public TypedID getCommandTargetID() { return this.commandTargetID; }
    public void setCommandTargetID(TypedID id) { this.commandTargetID = id; }
    public String getCommandTitle() { return this.commandTitle; }
    public void setCommandTitle(String title) { this.commandTitle = title; }
    public String getCommandDescription() { return this.commandDescription; }
    public void setCommandDescription(String description) { this.commandDescription = description; }
    public JSONObject getCommandMetadata() { return this.commandMetadata; }
    public void setCommandMetadata(JSONObject json) { this.commandMetadata = json; }
    public ServerCode getServerCode() {
        return this.serverCode;
    }
    public void setServerCode(ServerCode serverCode) {
        this.serverCode = serverCode;
    }
    public StatePredicate getPredicate() {
        if (this.condition != null && this.triggersWhen != null) {
            return new StatePredicate(this.condition, this.triggersWhen);
        } else {
            return null;
        }
    }
    public Condition getCondition() { return this.condition; }
    public void setCondition(Condition condition) { this.condition = condition; }
    public TriggersWhen getTriggersWhen() { return this.triggersWhen; }
    public void setTriggersWhen(TriggersWhen triggersWhen) { this.triggersWhen = triggersWhen; }
    public TriggerOptions getOptions() { return this.options; }
    public void setOptions(TriggerOptions options) { this.options = options; }

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
