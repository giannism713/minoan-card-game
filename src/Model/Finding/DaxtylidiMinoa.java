package Model.Finding;

import Model.Card.AnaktoroEnum;
import Model.Players.Player;

public class DaxtylidiMinoa extends RareFinding {

    /**
     * <b>Constructor</b>
     * creates the DaxtylidiMinoa with points and its anaktoro
     * @param points   the points associated with the finding
     * @param anaktoro the associated AnaktoroEnum value
     * @Precondition valid points and an anaktoro from the enum
     */
    public DaxtylidiMinoa(int points, AnaktoroEnum anaktoro) {
        super(points, anaktoro);
    }

    /**
     * <b>Transformer</b>
     * adds the finding to the collection of the player
     *
     * @param player is the player that this finding will be added to
     * @Postcondition The finding is added to the collection correctly
     */
    @Override
    public void addToSyllogi(Player player) {
        // first get the holdings
        int[] current_holdings = player.get_rare_holdings();
        current_holdings[0] = 1;// knossos is at first position
        player.set_holdings(current_holdings);
    }

}
