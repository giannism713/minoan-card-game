package Model.Card;

import Model.Players.Player;

public class SpecialCard implements Card {

    private AnaktoroEnum anaktoro;

    /**
     * <b>Constructor</b>
     * @param anaktoro is the category of the card
     * @Precondition the number is 1-20, valid anaktoro
     * @Postcondition Creates a card with certain anaktoro
     */
    public SpecialCard(AnaktoroEnum anaktoro) {
        this.anaktoro = anaktoro;
    }

    /**
     * <b>Accessor</b>
     * @return AnaktoroEnum
     * @Postcondition card's anaktoro(category) has been returned
     */
    @Override
    public AnaktoroEnum getAnaktoro() {
        return anaktoro;
    }

    /**
     * <b>Transformer</b>
     * sets the card's value
     * @param anaktoro is the category of the card
     * @Precondition valid anaktoro from the enumerator
     * @Postcondition card's anaktoro has been set
     */
    @Override
    public void setAnaktoro(AnaktoroEnum anaktoro) {
        this.anaktoro = anaktoro;
    }


    /**
     * <b>Accessor</b>
     * @return a string with the info of the card (anaktoro+number)
     * @Precondition the anaktoro currently held from the card is valid
     * @Postcondition the details of the card have been returned as a string
     */
    public String getCardString() {
        if (anaktoro == AnaktoroEnum.KNOSSOS) {
            if (this instanceof Ariadne) {
                return "knossosAri";
            } else if (this instanceof Minotaur) {
                return "knossosMin";
            }
        } else if (anaktoro == AnaktoroEnum.MALIA) {
            if (this instanceof Ariadne) {
                return "maliaAri";
            } else if (this instanceof Minotaur) {
                return "maliaMin";
            }
        } else if (anaktoro == AnaktoroEnum.ZAKROS) {
            if (this instanceof Ariadne) {
                return "zakrosAri";
            } else if (this instanceof Minotaur) {
                return "zakrosMin";
            }
        } else {
            if (this instanceof Ariadne) {
                return "phaistosAri";
            } else if (this instanceof Minotaur) {
                return "phaistosMin";
            }
        }
        return null; // never
    }
}

