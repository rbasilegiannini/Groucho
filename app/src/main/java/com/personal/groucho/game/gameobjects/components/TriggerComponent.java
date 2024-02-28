package com.personal.groucho.game.gameobjects.components;


import static com.personal.groucho.game.gameobjects.ComponentType.TRIGGER;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class TriggerComponent extends Component {
    private final Runnable triggerAction;

    public TriggerComponent(Runnable triggerAction) {
        this.triggerAction = triggerAction;
    }

    @Override
    public ComponentType type() {
        return TRIGGER;
    }

    public void handleTrigger() {
        triggerAction.run();
    }
}