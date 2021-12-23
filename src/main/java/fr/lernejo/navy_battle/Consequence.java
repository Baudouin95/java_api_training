package fr.lernejo.navy_battle;

public class Consequence {
    private final String type;
    private final State state;

    public Consequence(String type,State state) {
        this.type = type;
        this.state = state;
    }


    public String getType() {
        return type;
    }

    public State getState() {
        return state;
    }
}
