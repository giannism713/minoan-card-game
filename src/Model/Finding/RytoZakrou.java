package Model.Finding;

import Model.Card.AnaktoroEnum;
import Model.Players.Player;

public class RytoZakrou extends RareFinding {

    /**
     * <b>Constructor</b>
     * makes the RareFinding RytoZakrou with points and its anaktoro
     * @param points   The points associated with the finding
     * @param anaktoro The associated AnaktoroEnum value
     * @Precondition valid points and an anaktoro from the enum
     */
    public RytoZakrou(int points, AnaktoroEnum anaktoro) {
        super(points, anaktoro);
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
        // first get the holdings
        int[] current_holdings = player.get_rare_holdings();
        current_holdings[3] = 1;// zakros is the last position
        player.set_holdings(current_holdings);
    }
}
