package Model.Collection;

import Model.Card.*;
import Model.Pawn.Archaiologos;
import Model.Pawn.Pawn;
import Model.Pawn.Thiseas;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.abs;

public class CollectionCards {
    private ArrayList<Card> all_cards;

    /**
     * <b>Constructor</b>
     * makes an empty collection with cards inside
     * @Postcondition Creates an empty collection with cards inside
     */
    public CollectionCards() {
        all_cards = new ArrayList<Card>();
    }

    /**
     * <b>Transformer</b>
     * @Precondition all_cards array is not null
     * @Postcondition Sets the 100 cards and shuffles them
     */
    public void set_shuffle_cards() {
        // Generate 80 cards (20 cards are for every anaktoro with values 1-10) (80)
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i < 11; i++) {
                all_cards.add(new NumberCard(i, AnaktoroEnum.KNOSSOS));
                all_cards.add(new NumberCard(i, AnaktoroEnum.FAISTOS));
                all_cards.add(new NumberCard(i, AnaktoroEnum.ZAKROS));
                all_cards.add(new NumberCard(i, AnaktoroEnum.MALIA));
            }
        }
        // Generate 12 cards for MitoAriadnis (12)
        for (int i = 0; i < 3; i++) {
            all_cards.add(new Ariadne(AnaktoroEnum.KNOSSOS));
            all_cards.add(new Ariadne(AnaktoroEnum.FAISTOS));
            all_cards.add(new Ariadne(AnaktoroEnum.ZAKROS));
            all_cards.add(new Ariadne(AnaktoroEnum.MALIA));
        }
        // Generate 8 cards for Minotaur (8)
        for (int i = 0; i < 2; i++) {
            all_cards.add(new Minotaur(AnaktoroEnum.KNOSSOS));
            all_cards.add(new Minotaur(AnaktoroEnum.FAISTOS));
            all_cards.add(new Minotaur(AnaktoroEnum.ZAKROS));
            all_cards.add(new Minotaur(AnaktoroEnum.MALIA));
        }
        // Shuffle these cards now
        Collections.shuffle(all_cards); // now the cards are shuffled
    }

    /**
     * <b>Accessor</b>
     *
     * @return the ArrayList of all the cards in the specific collection
     */
    public ArrayList<Card> getAll_cards() {
        return all_cards;
    }

    /**
     * <b>Accessor</b>
     * @Precondition the all_cards arraylist is not empty
     * @return the top card of the stack
     */
    public Card get_top_card() {
        Card top_card = all_cards.get(all_cards.size() - 1);
        all_cards.remove(all_cards.size() - 1);
        return top_card;
    }

    /**
     * <b>Accessor</b>
     * @Precondition the arraylist of the cards has been shuffled
     * @return random 8 cards as an ArrayList for the player and removes them from the whole array list
     */
    public ArrayList<Card> getRandom8() {
        ArrayList<Card> cards_for_player = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * all_cards.size());
            cards_for_player.add(all_cards.get(index));
            // also need to remove them from the current stack
            all_cards.remove(index);
        }
        return cards_for_player;
    }

    /**
     * <b>Transformer</b>
     * @param file_to_save is the file chosen by the user to save the config of the game
     * @Precondition file_to_save is not null
     * @Postcondition rest of the card games are placed inside a txt file
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
            for (Card card : all_cards) {
                writer.println(card.getCardString());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * @param reader is the stream where will we take the information
     * @Precondition reader is not null
     * @Postcondition stack of cards of the game is loaded
     * @throws IOException when reader is null
     */
    public void load_details(BufferedReader reader) throws IOException {
        ArrayList<Card> new_cards = new ArrayList<>();
        String line;
        line = reader.readLine(); // Ignore ----
        // Read and set the last played cards and normal cards for each path
        int i = 0;
        while (true) {
            line = reader.readLine();
            if (line.contains("-")) {
                all_cards = new_cards;
                break;
            }
            if (line.equals("null")) {
                all_cards.set(i, null);
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
                new_cards.add(i, new_card);
                i++;
            }
        }

    }

}

