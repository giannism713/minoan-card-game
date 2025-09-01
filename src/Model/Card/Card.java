package Model.Card;

import Model.Players.Player;

/**
 * Contains the methods signatures for creating
 * all types of cards
 *
 * @author Ioannis Markakis
 * @version 1.0
 */

public interface Card {


    /**
     * <b>Accessor</b>
     * returns the card's anaktoro
     * @return AnaktoroEnum
     */
    public AnaktoroEnum getAnaktoro();

    /**
     * <b>Transformer</b>
     * sets the card's anaktoro
     * @param anaktoro is the category of the card
     * @Precondition anaktoro is valid enum value
     * @Postcondition card's anaktoro has been set
     */
    public void setAnaktoro(AnaktoroEnum anaktoro);



    /**
     * <b>Accessor</b>
     * @return a string with the info of the card (anaktoro+number)
     */
    public String getCardString();


}
