package Model.Card;

import Model.Players.Player;

public class Minotaur extends SpecialCard {

    /**
     * <b>Constructor</b>
     * makes a minotaur rare finding
     *
     * @param anaktoro is the category of the card
     * @Precondition the number is 1-20, valid anaktoro
     * @Postcondition Creates a card with a certain number and a certain anaktoro
     */
    public Minotaur(AnaktoroEnum anaktoro) {
        super(anaktoro);
    }

}
