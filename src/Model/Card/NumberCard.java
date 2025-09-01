package Model.Card;

import Model.Players.Player;

public class NumberCard implements Card {

    private int number;
    private AnaktoroEnum anaktoro;

    /**
     * <b>Constructor</b>
     * makes a numbered card
     * @param number   is the value of the card
     * @param anaktoro is the category of the card
     * @Precondition the number is 1-10, valid anaktoro
     * @Postcondition Creates a card with a certain number and a certain anaktoro
     */
    public NumberCard(int number, AnaktoroEnum anaktoro) {
        this.number = number;
        this.anaktoro = anaktoro;
    }

    /**
     * <b>Accessor</b>
     * @return the number value of the card
     * @Postcondition Returns the number of the card
     */
    public int getNumber() {
        return number;
    }

    /**
     * <b>Transformer</b>
     * @param number is the value of the card
     * @Precondition the number is [1-9]
     * @Postcondition Sets the number of the card
     */
    public void setNumber(int number) {
        this.number = number;
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
     * sets the anaktoro of the numbered card
     * @param anaktoro is the category of the card
     * @Precondition valid anaktoro from the enum
     * @Postcondition card's anaktoro has been set
     */
    @Override
    public void setAnaktoro(AnaktoroEnum anaktoro) {
        this.anaktoro = anaktoro;
    }

    /**
     * <b>Accessor</b>
     * @return a string with the info of the card (anaktoro+number)
     * @Precondition a valid anaktoro is held from the card
     * @Postcondition the details of the card have been returned as a string
     */
    public String getCardString() {
        if (anaktoro == AnaktoroEnum.KNOSSOS) {
            return "knossos" + number;
        } else if (anaktoro == AnaktoroEnum.MALIA) {
            return "malia" + number;
        } else if (anaktoro == AnaktoroEnum.ZAKROS) {
            return "zakros" + number;
        } else {
            return "phaistos" + number;
        }
    }

}
