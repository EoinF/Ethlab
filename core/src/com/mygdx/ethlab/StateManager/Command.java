package com.mygdx.ethlab.StateManager;

/**
 * Created by Eoin on 3/19/2017.
 */
public class Command {
    Object previousState;
    Object newValue;
    ActionType actionType;

    // If the origin of the command is from the ui, we avoid an infinite loop by not updating the UI (it's already set correctly)
    boolean isOriginUI;
}
