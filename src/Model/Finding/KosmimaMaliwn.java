package Model.Finding;

import Model.Card.AnaktoroEnum;
import Model.Players.Player;

public class KosmimaMaliwn extends RareFinding {
    /**
     * <b>Constructor</b>
     * makes the KosmimaMaliwn with points and its anaktoro
     * @param points   the points associated with the finding
     * @param anaktoro the associated AnaktoroEnum value
     * @Precondition valid points and an anaktoro from the enum
     */
    public KosmimaMaliwn(int points, AnaktoroEnum anaktoro) {
        super(points, anaktoro);
    }

    /**
     * <b>Transformer</b>
     * adds the finding to the collection of the player
     * @param player is the player that this finding will be added to
     * @Precondition player is not null
     * @Postcondition The finding is added to the collection correctly
     */
    @Override
    public void addToSyllogi(Player player) {
        // first get the holdings
        int[] current_holdings = player.get_rare_holdings();
        current_holdings[1] = 1;// malia is at second position
        player.set_holdings(current_holdings);
    }
}
