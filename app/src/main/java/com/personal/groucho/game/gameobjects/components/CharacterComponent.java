package com.personal.groucho.game.gameobjects.components;

import com.personal.groucho.game.CharacterFactory.CharacterProperties;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class CharacterComponent extends Component {
    protected CharacterProperties properties;
    @Override
    public ComponentType type() {return ComponentType.CHARACTER;}

    public CharacterComponent(CharacterProperties properties) {
        this.properties = properties;
    }
}
