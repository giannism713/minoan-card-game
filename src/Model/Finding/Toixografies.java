package Model.Finding;

import Model.Players.Player;

import java.util.ArrayList;

public class Toixografies implements Finding {
    private int points;
    private int id;
    private ArrayList<Integer> players_photographed;

    /**
     * <b>Constructor</b>
     * makes a toixografia finding with points
     * @param points The points associated with the finding
     * @param id     is the id of the certain toixografia
     * @Precondition valid points and an id [1,6]
     */
    public Toixografies(int points, int id) {
        this.points = points;
        this.id = id;
        players_photographed = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            players_photographed.add(0);
        }
    }

    /**
     * <b>Accessor</b>
     * @return Points of the finding
     * @Postcondition points of the finding have been returned
     */
    public int getPoints() {
        return points;
    }

    /**
     * <b>Transformer</b>
     * sets the point of the anaktoro
     * @param points points of the finding
     * @Postcondition sets the points to the finding
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the ID of the finding
     * @Postcondition the ID of the finding has been returned
     */
    public int get_id() {
        return id;
    }


    /**
     * <b>Accessor</b>
     *
     * @return the arraylist of the players that have photographed this toixografia (if at index 0 = 1) then player 0 has photographed
     */
    public ArrayList<Integer> get_players_photographed() {
        return players_photographed;
    }

    /**
     * <b>Transformer</b>
     * @param player_index is the index of the player that you will set to 1
     * @param value is the either 0 1 either if the index player has photographed the photo or not
     * @Postcondition sets the toixografia to have certain photographed people
     */
    public void setPlayers_photographed(int player_index, int value) {
        players_photographed.set(player_index, value);
    }


    /**
     * <b>Transformer</b>
     * sets the finding to the collection of the player
     *
     * @param player is the player that this finding will be added to
     * @Postcondition Adds the finding to the collection correctly
     */
    @Override
    public void addToSyllogi(Player player) {
        int[] current_photos = player.get_toixografies_photographed_ids();
        current_photos[id - 1] = 1; // Check
        player.set_photographed(current_photos);
    }

    /**
     * <b>Accessor</b>
     *
     * @return the string the contains the details of the toixografia
     */
    public String toString() {
        return "toixografia[points=" + points + "-id=" + id + "-players_photographed=" + players_photographed + "]";
    }
}
