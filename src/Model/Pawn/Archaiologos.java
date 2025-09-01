package Model.Pawn;

import Model.Card.AnaktoroEnum;

public class Archaiologos extends Pawn {
    /**
     * <b>Constructor</b>
     * makes an Archaiologos Pawn
     * @param anaktoro is the enumerator of the pawn's place
     * @Precondition anaktoro is from the enum
     * @Postcondition creates an Archaiologos pawn for a player that belongs to a certain anaktoro path
     */
    public Archaiologos(AnaktoroEnum anaktoro) {
        super(anaktoro);
    }

    /**
     * <b>Accessor</b>
     *
     * @return the uses of the pawn Thiseas (max3)
     */
    @Override
    public int getUses() {
        return 0;
    }

    /**
     * <b>Transformer</b>
     *
     * @param uses of the pawn to be set
     * @Postcondition the uses are to be set
     */
    @Override
    public void setUses(int uses) {

    }

    /**
     * <b>Transformer</b>
     * overriding toString for Archaiologos Pawn
     */
    public String toString() {
        return "Archaiologos_" + super.get_anaktoro() + "_" + super.get_position() + "_" + super.get_visibility();
    }
}
