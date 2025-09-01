package Model.Pawn;

import Model.Card.AnaktoroEnum;

public abstract class Pawn {
    protected int position_index;
    protected AnaktoroEnum anaktoro;
    protected boolean visible;

    /**
     * <b>Constructor</b>
     * makes a pawn for a path
     *
     * @param anaktoro is the enumerator of the pawn's place
     * @Postcondition creates a pawn for a player that belongs to a certain anaktoro path at index 0 (first square)
     */
    Pawn(AnaktoroEnum anaktoro) {
        this.position_index = -1;
        this.anaktoro = anaktoro;
        this.visible = false;
    }

    /**
     * <b>Accessor</b>
     * returns the anaktoro that the pawn has been placed
     *
     * @return the anaktoro of the pawn it is in
     */
    public AnaktoroEnum get_anaktoro() {
        return anaktoro;
    }

    /**
     * <b>Transformer</b>
     * set the anaktoro of the certain pawn
     *
     * @param anaktoro is the anaktoro to be assigned inside the pawn
     * @Precondition anaktoro is from the enum
     * @Postcondition the anaktoro of the pawn (location) has been set
     */
    public void set_anaktoro(AnaktoroEnum anaktoro) {
        this.anaktoro = anaktoro;
    }

    /**
     * <b>Accessor</b>
     * get the position of the pawn
     * @return the current position of the pawn in the path
     */
    public int get_position() {
        return position_index;
    }

    /**
     * <b>Transformer</b>
     * set the position of the pawn
     * @param position is the position to be set for the pawn in the path [0-7]
     * @Precondition positions are [1,9]
     * @Postcondition the position of the pawn has been set
     */
    public void set_position(int position) {
        this.position_index = position;
    }

    /**
     * <b>Accessor</b>
     * get the visibility of the pawn
     *
     * @return the visibility of the pawn
     */
    public boolean get_visibility() {
        return visible;
    }

    /**
     * <b>Transformer</b>
     * set the visibility of the pawn
     * @param visible is the boolean to tell us were the pawn is visible or not
     * @Precondition visibility is false at the beginning then true
     * @Postcondition the visibility of the pawn has been set
     */
    public void set_visibility(boolean visible) {
        this.visible = visible;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the score of the current position of the pawn
     */
    public int get_score() {
        if (position_index == 0) {
            return -20;
        } else if (position_index == 1) {
            return -15;
        } else if (position_index == 2) {
            return -10;
        } else if (position_index == 3) {
            return 5;
        } else if (position_index == 4) {
            return 10;
        } else if (position_index == 5) {
            return 15;
        } else if (position_index == 6) {
            return 30;
        } else if (position_index == 7) {
            return 35;
        } else if (position_index == 8) {
            return 50;
        } else
            return 0;
    }

    /**
     * <b>Accessor</b>
     * @return the uses of the pawn Thiseas (max3)
     */
    public abstract int getUses();

    /**
     * <b>Transformer</b>
     *
     * @param uses of the pawn to be set
     * @Postcondition the uses are to be set
     */
    public abstract void setUses(int uses);
}
