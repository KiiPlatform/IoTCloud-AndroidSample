package com.kii.iotcloudsample.fragments.wizard;

import android.support.v4.app.Fragment;

import com.kii.iotcloudsample.model.Trigger;

import java.lang.ref.WeakReference;

public abstract class WizardFragment extends Fragment {

    public static final int EXIT_NEXT = 1;
    public static final int EXIT_PREVIOUS = 2;

    public interface WizardController {
        public void setNextButtonEnabled(boolean enabled);
    }

    protected Trigger editingTrigger;
    private WeakReference<WizardController> controller;

    public void setController(WizardController controller) {
        this.controller = new WeakReference<WizardController>(controller);
    }
    public void setEditingTrigger(Trigger editingTrigger) {
        this.editingTrigger = editingTrigger;
    }
    protected void setNextButtonEnabled(boolean enabled) {
        if (this.controller != null && this.controller.get() != null) {
            this.controller.get().setNextButtonEnabled(enabled);
        }
    }

    public abstract String getNextButtonText();
    public abstract String getPreviousButtonText();
    public void onActivate() {
    }
    public void onInactivate(int exitCode) {
    }

}
