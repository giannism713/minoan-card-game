package Model.Finding;

import Model.Card.AnaktoroEnum;
import Model.Players.Player;

public abstract class RareFinding implements Finding {

    private int points;
    private AnaktoroEnum anaktoro;

    /**
     * <b>Constructor</b>
     * makes the RareFinding with points and its anaktoro
     * @param points   The points associated with the finding
     * @param anaktoro The associated AnaktoroEnum value
     * @Precondition valid points and an anaktoro from the enum
     */
    public RareFinding(int points, AnaktoroEnum anaktoro) {
        this.points = points;
        this.anaktoro = anaktoro;
    }

    /**
     * <b>Accessor</b>
     * @return Points of the finding
     * @Postcondition card's anaktoro(category) has been returned
     */
    public int getPoints() {
        return points;
    }

    /**
     * <b>Transformer</b>
     * sets the point of the anaktoro
     * @param points is the points value
     * @Postcondition sets the points to the finding
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * <b>Accessor</b>
     * @return Anaktoro of the rare finding
     */
    public AnaktoroEnum getAnaktoro() {
        return anaktoro;
    }

    /**
     * <b>Transformer</b>
     * sets the anaktoro of the rare finding
     * @param anaktoro is the anaktoro enumerator
     * @Precondition the anaktoro is from the enum
     * @Postcondition the rare finding is placed only on its anaktoro
     */
    public void setAnaktoro(AnaktoroEnum anaktoro) {
        this.anaktoro = anaktoro;
    }

    /**
     * <b>Transformer</b>
     * sets the finding to the collection of the player
     * @param player is the player that this finding will be added to
     * @Precondition player is not null
     * @Postcondition adds the finding to the collection correctly
     */
    @Override
    public void addToSyllogi(Player player) {

    }
}
