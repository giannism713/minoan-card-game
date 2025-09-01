package Model.Finding;

import Model.Players.Player;

public interface Finding {


    /**
     * <b>Transformer</b>
     * sets the finding to the collection of the player
     * @param player is the player that this finding will be added to
     * @Precondition player is not null
     * @Postcondition Adds the finding to the collection correctly
     */
    public void addToSyllogi(Player player);

}
