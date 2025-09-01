package Model.Priority;

import java.io.*;
import java.util.Random;

public class Turn {
    private static int first = 0;
    private int turn;

    /**
     * <b>Constructor</b>
     * makes a microcontroller that handles the turn of each player
     * @Postcondition a microcontoller that cha
     */
    public Turn() {
    }

    /**
     * <b>Observer</b>
     * observes the value of the turn of the player
     * @Precondition the game has started
     * @return returns the current turn
     */
    public int get_turn() {
        if (first == 0) {
            first = 1;
            turn = getRandomIndex();
        }
        return turn;
    }

    /**
     * <b>Transformer</b>
     * changes the turn accordingly
     * @Precondition the turn has been initialized
     * @Postcondition changes the turn to the player it and does the appropriate moves and denies priority if certain cards have been played
     */
    public void update_turn() {
        if (turn == 0) {
            turn = 1;
        } else {
            turn = 0;
        }
    }


    private int getRandomIndex() {
        int[] indexes_available = {0, 1};
        Random random = new Random();
        return indexes_available[random.nextInt(indexes_available.length)];
    }

    /**
     * <b>Transformer</b>
     * method to store the turn data to the txt
     * @param file_to_save is the file chosen by the user to save the config of the game
     * @Precondition file_to_save is not null
     * @Postcondition the details of the turn are stored to a txt
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
            writer.println(get_turn());

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * method to load the turn from the txt to the Turn object
     * @param reader is the stream from which we will take the data
     * @Precondition reader is not null
     * @Postcondition the details of the turn are restored from the txt
     * @throws IOException when reader is null
     */
    public void load_details(BufferedReader reader) throws IOException {
        String line;
        line = reader.readLine(); // Ignore --
        line = reader.readLine();

        turn = Integer.parseInt(line);
    }

}
