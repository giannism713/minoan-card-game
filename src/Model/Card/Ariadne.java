package Model.Card;

import Model.Players.Player;

public class Ariadne extends SpecialCard {

    /**
     * <b>Constructor</b>
     * makes a Ariadne rare finding
     * @param anaktoro is the category of the card
     * @Precondition the number is 1-10, valid anaktoro
     * @Postcondition Creates a card with a certain anaktoro
     */
    public Ariadne(AnaktoroEnum anaktoro) {
        super(anaktoro);
    }

}
