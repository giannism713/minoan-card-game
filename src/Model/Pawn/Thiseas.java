package Model.Pawn;

import Model.Card.AnaktoroEnum;

public class Thiseas extends Pawn {
    private int uses = 0;

    /**
     * <b>Constructor</b>
     * makes a thiseas pawn for a path
     * @param anaktoro is the enumerator of the pawn's place
     * @Precondition anaktoro is from the enum
     * @Postcondition creates a Thiseas pawn for a player that belongs to a certain anaktoro path
     */
    public Thiseas(AnaktoroEnum anaktoro) {
        super(anaktoro);
    }

    /**
     * <b>Accessor</b>
     * @return the uses of the pawn Thiseas (max3)
     */
    public int getUses() {
        return uses;
    }

    /**
     * <b>Transformer</b>
     * @param uses of the pawn to be set
     * @Precondition uses are [1,3]
     * @Postcondition the uses are to be set
     */
    public void setUses(int uses) {
        this.uses = uses;
    }

    /**
     * <b>Transformer</b>
     * overriding toString for Thiseas Pawn
     */
    public String toString() {
        return "Thiseas_" + super.get_anaktoro() + "_" + super.get_position() + "_" + super.get_visibility() + "_" + getUses();
    }
}
