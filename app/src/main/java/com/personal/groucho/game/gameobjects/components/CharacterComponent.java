package com.personal.groucho.game.gameobjects.components;

import com.personal.groucho.game.CharacterFactory.CharacterProp;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class CharacterComponent extends Component {
    protected CharacterProp properties;
    @Override
    public ComponentType type() {return ComponentType.CHARACTER;}

    public CharacterComponent(CharacterProp properties) {
        this.properties = properties;
    }
}
