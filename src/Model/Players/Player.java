package Model.Players;

import Model.Card.*;
import Model.Pawn.Archaiologos;
import Model.Pawn.Pawn;
import Model.Pawn.Thiseas;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import static java.lang.Math.abs;

public class Player {
    private ArrayList<Pawn> pawns;
    private ArrayList<Card> cards;
    private ArrayList<Card> last_played_cards; // knossos, malia, faistos, zakros
    private ArrayList<Card> prev_last_played_cards; // knossos, malia, faistos, zakros
    //    private ArrayList<Finding> rare_holdings_array;
    private int[] toixografies_photographed; // if index -> 1 this photo is photographed
    private int score;
    private int count_agalmatakia;
    private int[] rare_holdings; // knossos malia faistos zakros;
    private int[] anaktora_visited;

    /**
     * <b>Constructor</b>
     * makes a player that has some pawns not yet placed in a path
     * @Postcondition Creates an empty collection with cards inside pawns must be picked by the user
     */
    public Player() {
        cards = new ArrayList<>();
        last_played_cards = new ArrayList<>(4);
        prev_last_played_cards = new ArrayList<>(4);
        count_agalmatakia = 0;
        rare_holdings = new int[4];
        pawns = new ArrayList<>();
        anaktora_visited = new int[4];
        toixografies_photographed = new int[6];
        for (int i = 0; i < 8; i++) {
            cards.add(null);
        }
        for (int i = 0; i < 4; i++) {
            last_played_cards.add(null);
        }
        for (int i = 0; i < 4; i++) {
            prev_last_played_cards.add(null);
        }
        for (int i = 0; i < 4; i++) {
            rare_holdings[i] = 0;
        }
        for (int i = 0; i < 3; i++) {
            pawns.add(new Archaiologos(null)); // everything is null we still dont know the anaktoro of each pioni
        }
        pawns.add(new Thiseas(null)); // last index contains the thiseas
        for (int i = 0; i < 6; i++) {
            toixografies_photographed[i] = -1;
        }
        for (int i = 0; i < 4; i++) {
            anaktora_visited[i] = 0;
        }
    }

    /**
     * <b>Transformer</b>
     * set the count of agalmatakia of the player
     * @Postcondition the agalmatakia count of the player has been changed
     */
    public void increment_agalmatakia_count() {
        count_agalmatakia++;
    }

    /**
     * <b>Transformer</b>
     * sets a certain card for the hand of the player
     * @param card       is the card to be added
     * @param card_index the place for the card to be added
     * @Precondition card_index is [0,3] and card is not null
     * @Postcondition the card has been added to the hand of the player
     */
    public void set_card_at_index(int card_index, Card card) {
        cards.set(card_index, card);
    }


    /**
     * <b>Transformer</b>
     *
     * @param cards is the hand of the player or what is left of his hand
     * @Precondition cards are not null
     * @Postcondition sets the cards of the player
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * <b>Transformer</b>
     * @param path_index is the path of the anaktoro to put the last played card
     * @param card       is the card to be the last
     * @Precondition card_index is [0,3] and card is not null
     * @Postcondition sets the last played card for certain path
     */
    public void set_last_played_card_for_path(int path_index, Card card) {
        if (path_index >= 0 && path_index < last_played_cards.size()) {
            if (last_played_cards.get(path_index) != null) {
                prev_last_played_cards.set(path_index, last_played_cards.get(path_index));
            }
            last_played_cards.set(path_index, card);
        } else {
            System.out.println("Invalid path_index: " + path_index);
        }
    }

    /**
     * <b>Transformer</b>
     * @param holdings a new array that has the holdings
     * @Precondition holdings array is not null
     * @Postcondition the rare holdings array of the player has been changed if there is a new holding fetched
     */
    public void set_holdings(int[] holdings) {
        this.rare_holdings = holdings;
    }

    /**
     * <b>Transformer</b>
     * @param holdings a new array that has the photographies photographed
     * @Precondition holdings array is not null
     * @Postcondition the photographies holdings array is set
     */
    public void set_photographed(int[] holdings) {
        this.toixografies_photographed = holdings;
    }

    /**
     * <b>Accessor</b>
     * get prev last card at a specific index
     * @param index is the index of the card that we want to return
     * @Precondition index is [0-3]
     * @return the last played card of a player at a certain path
     */
    public Card get_prev_last_card_index(int index) {
        return prev_last_played_cards.get(index); // check adjust to last_played_cards
    }


    /**
     * <b>Accessor</b>
     * get the score of the player
     * @return the current score of the player
     */
    public int get_player_score() {
        int pawn_score = 0;
        int syllogi_score = 0;
        for (int i = 0; i < 4; i++) {
            if (pawns.get(i) instanceof Thiseas) {
                pawn_score = pawn_score + 2 * pawns.get(i).get_score(); // Score is double when its a thiseas pawn
            } else {
                pawn_score = pawn_score + pawns.get(i).get_score();
            }
        }
        // Calculate the points of the syllogi too
        if (rare_holdings[0] == 1) {
            syllogi_score = syllogi_score + 25;
        }
        if (rare_holdings[1] == 1) {
            syllogi_score = syllogi_score + 25;
        }
        if (rare_holdings[2] == 1) {
            syllogi_score = syllogi_score + 35; // faistos case
        }
        if (rare_holdings[3] == 1) {
            syllogi_score = syllogi_score + 25;
        }
        // Calculate agalmatakia score
        int agalmatakia_score = 0;
        if (count_agalmatakia == 1) {
            agalmatakia_score = -20;
        } else if (count_agalmatakia == 2) {
            agalmatakia_score = -15;
        } else if (count_agalmatakia == 3) {
            agalmatakia_score = 10;
        } else if (count_agalmatakia == 4) {
            agalmatakia_score = 15;
        } else if (count_agalmatakia == 5) {
            agalmatakia_score = 30;
        } else if (count_agalmatakia == 6) {
            agalmatakia_score = 50;
        }

        // Calculate toixografies score
        int toixografies_score = 0;
        if (toixografies_photographed[0] == 1) {
            toixografies_score = toixografies_score + 20;
        }
        if (toixografies_photographed[1] == 1) {
            toixografies_score = toixografies_score + 20;
        }
        if (toixografies_photographed[2] == 1) {
            toixografies_score = toixografies_score + 15;
        }
        if (toixografies_photographed[3] == 1) {
            toixografies_score = toixografies_score + 15;
        }
        if (toixografies_photographed[4] == 1) {
            toixografies_score = toixografies_score + 15;
        }
        if (toixografies_photographed[5] == 1) {
            toixografies_score = toixografies_score + 20;
        }

        // Add them
        score = pawn_score + syllogi_score + agalmatakia_score + toixografies_score;

        return score; // check adjust to last_played_cards
    }

    /**
     * <b>Accessor</b>
     * get agalmatakia count of the player
     * @return the current count of agalmatakia of the player
     */
    public int get_player_agalmatakia_count() {
        return count_agalmatakia;
    }

    /**
     * <b>Accessor</b>
     * get the card at a specific index
     * @param index is the index of the card that we want to return
     * @Precondition index is [0-3]
     * @return a certain card at a certain index of the player
     */
    public Card get_card_index(int index) {
        return cards.get(index);
    }

    /**
     * <b>Accessor</b>
     * get last card at a specific index
     * @param index is the index of the card that we want to return
     * @Precondition index is [0-3]
     * @return the last played card of a player at a certain path
     */
    public Card get_last_card_index(int index) {
        return last_played_cards.get(index); // check adjust to last_played_cards
    }

    /**
     * <b>Accessor</b>
     * get the array of the rare holdings of the player
     * @return an array of 1, 0 whether a player holds a rare finding or not
     */
    public int[] get_rare_holdings() {
        return rare_holdings; // check adjust to last_played_cards
    }

    /**
     * <b>Accessor</b>
     * get the pawns of the player in a 2x2 array if they are available or not
     * @return the available pawns as 1 and 0 in an array for count purposes
     */
    public int[] get_available_pawns() {
        int[] available_pawns = new int[2];
        for (int i = 0; i < 3; i++) {
            if (pawns.get(i).get_anaktoro() == null) {
                available_pawns[0]++;
            }
        }
        if (pawns.get(3).get_anaktoro() == null) {
            available_pawns[1]++;
        }
        return available_pawns;
    }

    /**
     * <b>Accessor</b>
     * get the ids of the photographed toixografies of the player
     * @return the toixografies that the player has photographed
     */
    public int[] get_toixografies_photographed_ids() {
        return toixografies_photographed;
    }

    /**
     * <b>Accessor</b>
     * get the pawns of the player inside an array of pawns
     * @return all the pawns of the player (deployed or not)
     */
    public ArrayList<Pawn> get_pawns() {
        return pawns;
    }

    /**
     * <b>Transformer</b>
     * set the pawns of the player inside an array of pawns
     * @param pawns_in is the arraylist with the updated pawns
     * @Precondition pawns_in is not null
     * @Postcondition the array of pawns has been set
     */
    public void set_pawns(ArrayList<Pawn> pawns_in) {
        pawns = pawns_in;
    }

    /**
     * <b>Accessor</b>
     * @param path_index is the index of the path
     * @Precondition path_index is [0-3]
     * @return if the certain anaktoro of the player is visited or not
     */
    public int get_anaktoro_visited_at_index(int path_index) {
        return anaktora_visited[path_index];
    }

    /**
     * <b>Transformer</b>
     * set anaktora visited of the player
     * @param path_index is the index of the path
     * @param value      is the new value of the visited anaktora
     * @Precondition path_index is [0-3] and value [0,1]
     * @Postcondition changes to the anaktora visited array is done
     */
    public void set_anaktoro_visited(int path_index, int value) {
        anaktora_visited[path_index] = value;
    }

    /**
     * <b>Transformer</b>
     * @param file_to_save is the file chsosen by the user to save the data of the game
     * @Precondition file_to_save is not null
     * @Postcondition details of each player are written into a txt file
     */
    public void save_details(File file_to_save) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_to_save));
             PrintWriter writer = new PrintWriter(new FileWriter(file_to_save, true))) {

            String line;

            // Find the next empty line
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    break;
                }
            }

            writer.println("------------------------------------------------------------");

            // Write player details to the file
//            writer.println(player_index);
            writer.println(get_player_score());
            writer.println(get_player_agalmatakia_count());
            writer.println(Arrays.toString(get_rare_holdings()));
            writer.println(Arrays.toString(get_toixografies_photographed_ids()));
            writer.println(Arrays.toString(anaktora_visited));
            for (int i = 0; i < 4; i++) {
                if (get_last_card_index(i) == null) {
                    writer.println("null");
                } else {
                    writer.println(get_last_card_index(i).getCardString());
                }
            }
            for (int i = 0; i < 8; i++) {
                writer.println(get_card_index(i).getCardString());
            }
            for (int i = 0; i < 4; i++) {
                writer.println(pawns.get(i));
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * @param reader is the stream where will take the data
     * @Precondition reader is not null
     * @Postcondition details of each player are written into a txt file
     * @throws IOException when reader is null
     */
    public void load_details(BufferedReader reader) throws IOException {
        String line;
        line = reader.readLine(); // Ignore ----
        // Score
        line = reader.readLine();
        this.score = Integer.parseInt(line);
        // Agalmatakia count
        line = reader.readLine();
        this.count_agalmatakia = Integer.parseInt(line);
        // Holdings
        line = reader.readLine();
        String[] rareHoldings = line.substring(1, line.length() - 1).split(", ");
        this.rare_holdings = Arrays.stream(rareHoldings).mapToInt(Integer::parseInt).toArray();
        // Photographed toixografies
        line = reader.readLine();
        String[] photographed = line.substring(1, line.length() - 1).split(", ");
        this.toixografies_photographed = Arrays.stream(photographed).mapToInt(Integer::parseInt).toArray();
        // Anaktora visited
        line = reader.readLine();
        String[] anaktoraVisited = line.substring(1, line.length() - 1).split(", ");
        this.anaktora_visited = Arrays.stream(anaktoraVisited).mapToInt(Integer::parseInt).toArray();
        // Read and set the last played cards and normal cards for each path
        for (int i = 0; i < 12; i++) {
            line = reader.readLine();
            if (line.equals("null")) {
                last_played_cards.set(i, null);
            } else {
                // Initialize variables for the number/caps and the cleaned line
                String number = "";
                String caps = "";
                String cleanedLine = ""; // Contains the anaktoro

                // Loop through each character in the line
                int flag = 0; // If 1 its a special card
                for (char ch : line.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        number = number + ch;
                    } else if (Character.isUpperCase(ch)) {
                        flag = 1;
                        caps = caps + ch;
                    } else if (flag == 1) {
                        caps = caps + ch;
                    } else {
                        cleanedLine = cleanedLine + ch;
                    }
                }

                // `cleanedLine` contains the rest of the string

                // Create a new Card with the cleaned line
                AnaktoroEnum anaktoro = null;
                if (cleanedLine.equals("knossos")) {
                    anaktoro = AnaktoroEnum.KNOSSOS;
                } else if (cleanedLine.equals("phaistos")) {
                    anaktoro = AnaktoroEnum.FAISTOS;
                } else if (cleanedLine.equals("malia")) {
                    anaktoro = AnaktoroEnum.MALIA;
                } else {
                    anaktoro = AnaktoroEnum.ZAKROS;
                }
                Card new_card = null;
                if (flag == 0) {
                    new_card = new NumberCard(Integer.parseInt(number), anaktoro);
                } else {
                    if (caps.equals("Ari")) {
                        new_card = new Ariadne(anaktoro);
                    } else if (caps.equals("Min")) {
                        new_card = new Minotaur(anaktoro);
                    }
                }
                if (i < 4) {
                    last_played_cards.set(i, new_card);
                } else {
                    cards.set(abs(4 - i), new_card);
                }
            }
        }
        // Pawns
        for (int i = 0; i < 4; i++) {
            line = reader.readLine();
            String[] details = line.split("_");
            String type = details[0];
            String anaktoro_string = details[1];
            String position = details[2];
            String visibility = details[3];
            String uses = null;
            if (type.equals("Thiseas")) {
                uses = details[4]; // check
            }
            AnaktoroEnum anaktoro = null;
            if (anaktoro_string.equals("KNOSSOS")) {
                anaktoro = AnaktoroEnum.KNOSSOS;
            } else if (anaktoro_string.equals("FAISTOS")) {
                anaktoro = AnaktoroEnum.FAISTOS;
            } else if (anaktoro_string.equals("MALIA")) {
                anaktoro = AnaktoroEnum.MALIA;
            } else if (anaktoro_string.equals("ZAKROS")) {
                anaktoro = AnaktoroEnum.ZAKROS;
            }
            Pawn new_pawn;
            if (line.contains("Thiseas")) {
                new_pawn = new Thiseas(anaktoro);
                if (Integer.parseInt(position) != -1) {
                    new_pawn.set_position(Integer.parseInt(position));
                }
                new_pawn.set_position(Integer.parseInt(position));
                new_pawn.set_visibility(Boolean.parseBoolean(visibility));
                assert uses != null;
                new_pawn.setUses(Integer.parseInt(uses));
            } else {
                new_pawn = new Archaiologos(anaktoro);
                if (Integer.parseInt(position) != -1) {
                    new_pawn.set_position(Integer.parseInt(position));
                }
                new_pawn.set_visibility(Boolean.parseBoolean(visibility));
            }
            pawns.set(i, new_pawn);
        }
    }

}
