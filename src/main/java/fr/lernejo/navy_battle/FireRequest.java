package fr.lernejo.navy_battle;

public class FireRequest {
    private Consequence consequence;
    private boolean shipLeft;

    public FireRequest(Consequence consequence,boolean shipLeft) {
        this.consequence = consequence;
        this.shipLeft = shipLeft;
    }
}
