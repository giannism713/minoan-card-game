package Model.Finding;

import Model.Players.Player;

public class Agalmataki implements Finding {
    /**
     * <b>Transformer</b>
     * sets the finding to the collection of the player
     * @param player is the player that this finding will be added to
     * @Precondition player is not null
     * @Postcondition Adds the finding to the collection correctly
     */
    @Override
    public void addToSyllogi(Player player) {
        player.increment_agalmatakia_count(); // increment the agalmatakia by one
    }


}
