package Model.TableOfGame;

import Model.Card.AnaktoroEnum;
import Model.Card.Card;
import Model.Finding.*;
import Model.Paths.Path;
import Model.Square.FindingSquare;
import Model.Square.SimpleSquare;
import Model.Square.Square;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Table {
    private Path path_faistos;
    private Path path_knossos;
    private Path path_malia;
    private Path path_zakros;
    private int[][] record_of_holding;
    private ArrayList<Card> last_played_cards = new ArrayList<>(); // knossos, malia, faistos, zakros

    /**
     * <b>Constructor</b>
     * makes a table that has the 4 paths
     * @param path_f is the path of Faistos
     * @param path_k is the path of Knossos
     * @param path_m is the path of Malia
     * @param path_z is the path of Zakros
     * @Precondition paths are not null
     * @Postcondition creates a table that contains the four paths for different anaktoro
     */
    public Table(Path path_f, Path path_k, Path path_m, Path path_z) {
        path_faistos = path_f;
        path_knossos = path_k;
        path_malia = path_m;
        path_zakros = path_z;
        record_of_holding = new int[4][9];
        for (int i = 0; i < 4; i++) { // at first set the last played cards of the table as null(noone has played yet)
            last_played_cards.add(null);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 9; j++) {
                record_of_holding[i][j] = -1;
            }
        }
    }

    /**
     * <b>Transformer</b>
     * @param path_index is the index of the path
     * @param position   is the position of the rare finding (new)
     * @param new_state  is the new number to be put in the array
     * @Precondition path_index is [0-3], position [0-8], new_state (-2,-1,0,1)
     * @Postcondition sets the record to say who holds what holding in each position for uniqueness issues
     */
    public void change_record_of_holding(int path_index, int position, int new_state) {
        record_of_holding[path_index][position] = new_state;
    }

    /**
     * <b>Accessor</b>
     * @param path_index is the index of the path
     * @param position   is the position of the rare finding (new)
     * @Precondition path_index is [0-3], position [0
     * @return the state of the holding in this position
     */
    public int get_record_of_holding(int path_index, int position) {
        return record_of_holding[path_index][position];
    }


    /**
     * <b>Transformer</b>
     *
     * @Postcondition sets the rest of the path with the rest of the findings
     */
    public void setRestFindings() {
        // choose a random path and place 1 agalmataki there
        Path[] path_array = new Path[]{path_faistos, path_knossos, path_malia, path_zakros};
        // choose a number between [0,3] its an index
        int count_agalmatakia = 10; // thats how many need to be distributed
        while (count_agalmatakia > 0) {
            int random_path = (int) (Math.random() * 4);
            Square[] squares_of_random_path;
            if (path_array[random_path] == path_knossos) {
                squares_of_random_path = path_knossos.get_squares_array();
            } else if (path_array[random_path] == path_malia) {
                squares_of_random_path = path_malia.get_squares_array();
            } else if (path_array[random_path] == path_zakros) {
                squares_of_random_path = path_zakros.get_squares_array();
            } else {
                squares_of_random_path = path_faistos.get_squares_array();
            }

            int square_index = getRandomIndex();
            if (squares_of_random_path[square_index] instanceof SimpleSquare) { // check if there isn't already a finding there
                // create new finding square with the agalmataki in
                int prev_points = squares_of_random_path[square_index].getPoints();
                FindingSquare square = new FindingSquare(prev_points, new Agalmataki());
                squares_of_random_path[square_index] = square;
                if (path_array[random_path] == path_knossos) {
                    path_knossos.set_squares_array(squares_of_random_path);
                } else if (path_array[random_path] == path_malia) {
                    path_malia.set_squares_array(squares_of_random_path);
                } else if (path_array[random_path] == path_zakros) {
                    path_zakros.set_squares_array(squares_of_random_path);
                } else {
                    path_faistos.set_squares_array(squares_of_random_path);
                }
                count_agalmatakia--;
            }
        }

        int toixografies_count = 6; // add the photografies to the rest of the indexes
        while (toixografies_count > 0) {
            int random_path = (int) (Math.random() * 4);
            Square[] squares_of_random_path;
            if (path_array[random_path] == path_knossos) {
                squares_of_random_path = path_knossos.get_squares_array();
            } else if (path_array[random_path] == path_malia) {
                squares_of_random_path = path_malia.get_squares_array();
            } else if (path_array[random_path] == path_zakros) {
                squares_of_random_path = path_zakros.get_squares_array();
            } else {
                squares_of_random_path = path_faistos.get_squares_array();
            }

            int square_index = getRandomIndex();
            if (squares_of_random_path[square_index] instanceof SimpleSquare) { // check if there isn't already a finding there
                // create new finding square with the toixografia in
                int prev_points = squares_of_random_path[square_index].getPoints();
                FindingSquare square;
                if (toixografies_count < 3 || toixografies_count == 4) {
                    square = new FindingSquare(prev_points, new Toixografies(20, toixografies_count));
                } else {
                    square = new FindingSquare(prev_points, new Toixografies(15, toixografies_count));
                }
                squares_of_random_path[square_index] = square;
                if (path_array[random_path] == path_knossos) {
                    path_knossos.set_squares_array(squares_of_random_path);
                } else if (path_array[random_path] == path_malia) {
                    path_malia.set_squares_array(squares_of_random_path);
                } else if (path_array[random_path] == path_zakros) {
                    path_zakros.set_squares_array(squares_of_random_path);
                } else {
                    path_faistos.set_squares_array(squares_of_random_path);
                }
                toixografies_count--;
            }
        }

    }

    private int getRandomIndex() {
        int[] indexes_available = {1, 3, 5, 7, 8};
        Random random = new Random();
        return indexes_available[random.nextInt(indexes_available.length)];
    }

    /**
     * <b>Accessor</b>
     *
     * @return the path Faistos
     */
    public Path getPath_faistos() {
        return path_faistos;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the path Knossos
     */
    public Path getPath_knossos() {
        return path_knossos;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the path Malia
     */
    public Path getPath_malia() {
        return path_malia;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the path Zakros
     */
    public Path getPath_zakros() {
        return path_zakros;
    }


    /**
     * <b>Accessor</b>
     *
     * @return last played card of each path in an arraylist
     */
    public ArrayList<Card> get_last_played_cards() {
        return last_played_cards;
    }

    /**
     * <b>Transformer</b>
     * set last played card of a certain path in an arraylist
     *
     * @param index is the path index for last played card
     * @param card  is the last played card at this path
     * @Postcondition the last played cards are set in a certain index
     */
    public void set_last_played_cards_at_index(int index, Card card) {
        last_played_cards.set(index, card);
    }

    /**
     * <b>Transformer</b>
     * @param file_to_save is the file chosen by the user to save the config of the game
     * @Precondition file_to_save is not null
     * @Postcondition save the details of the table inside a txt
     */
    public void save_details(File file_to_save) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_to_save));
             PrintWriter writer = new PrintWriter(new FileWriter(file_to_save, true))) {

            String line;
            boolean empty_line = false;

            // Find the next empty line
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    empty_line = true;
                    break;
                }
            }

            writer.println("------------------------------------------------------------");

            // Write player details to the file
            writer.println(path_knossos);
            writer.println(path_malia);
            writer.println(path_faistos);
            writer.println(path_zakros);
            writer.println(Arrays.deepToString(record_of_holding));

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * <b>Transformer</b>
     * @param reader is the stream to read the details from
     * @throws IOException when reader is null
     * @Precondition reader is not null
     * @Postcondition restores the table state of the txt file.
     */
    public void load_details(BufferedReader reader) throws IOException {
        String line;


        // Load paths
        path_knossos = parsePath(reader.readLine(), AnaktoroEnum.KNOSSOS);
        path_malia = parsePath(reader.readLine(), AnaktoroEnum.MALIA);
        path_faistos = parsePath(reader.readLine(), AnaktoroEnum.FAISTOS);
        path_zakros = parsePath(reader.readLine(), AnaktoroEnum.ZAKROS);

        // Load the record of holding (2D array)
        if ((line = reader.readLine()) != null) {
            record_of_holding = parse2DArray(line);
        }
    }

    /**
     * <b>Transformer</b>
     * method to parse a path string into a Path object.
     * @throws IOException when parameters are not valid
     * @param pathString The string representation of the path.
     * @param anaktoro   The anaktoro enum for this path.
     * @Precondition pathString is not empty
     * @return A Path object representing the path.
     */
    private Path parsePath(String pathString, AnaktoroEnum anaktoro) throws IOException {
        Path path = new Path(anaktoro);

        // Split the path string by commas
        String[] squareStrings = pathString.split("__");
        Square[] squares = new Square[9];

        for (int i = 0; i < squareStrings.length; i++) {
            String squareString = squareStrings[i];
            if (squareString.startsWith("simple")) {
                // Parse SimpleSquare
                int points = Integer.parseInt(squareString.split("_")[1]);
                squares[i] = new SimpleSquare(points);
            } else if (squareString.startsWith("finding")) {
                // Parse FindingSquare or Toixografia
                if (squareString.contains("toixografia")) {
                    squares[i] = parseToixografia(squareString);
                } else {
                    squares[i] = parseFindingSquare(squareString);
                }
            } else {
                throw new IOException("Unknown square type: " + squareString);
            }
        }

        path.set_squares_array(squares);
        return path;
    }

    /**
     * <b>Transformer</b>
     * method to parse a toixografia string into a FindingSquare.
     * @throws IOException when parameters are not valid
     * @param square_string The string representation of a `toixografia` square.
     * @Precondition square_string is not null
     * @return A FindingSquare with `toixografia` metadata.
     */
    private FindingSquare parseToixografia(String square_string) throws IOException {
        try {
            // Take only the part in []
            square_string = square_string.split("\\[p")[1];

            String[] toixografia_details = square_string.split("-");
            int points = Integer.parseInt(toixografia_details[0].replaceAll("[^\\d]", " ").trim());

            int id = Integer.parseInt(toixografia_details[1].replaceAll("[^\\d]", " ").trim());

            String toixografia_photographed = toixografia_details[2].replaceAll("[^\\d]", " ").trim();

            // Traverse the string
            int v1 = 0;
            int v2 = 0;
            for (int i = 0; i < toixografia_photographed.length(); i++) {
                if (i == 0 && toixografia_photographed.charAt(i) == '1') {
                    v1 = 1;
                } else if (i == 2 && toixografia_photographed.charAt(i) == '1') {
                    v2 = 1;
                }
            }
            int[] toixografies_photographed_by_players = {v1, v2};

            // Create a Toixografia object
            Toixografies toixografia = new Toixografies(points, id);
            for (int i = 0; i < 2; i++) {
                toixografia.setPlayers_photographed(i, toixografies_photographed_by_players[i]);
            }
            return new FindingSquare(points, toixografia);
        } catch (Exception e) {
            throw new IOException(square_string, e);
        }
    }

    /**
     * <b>Transformer</b>
     * method to parse a FindingSquare string.
     * @throws IOException when paremeter is not valid
     * @param square_string The string representation of a `FindingSquare`.
     * @Precondition squareString is not empty
     * @return A FindingSquare.
     */
    private FindingSquare parseFindingSquare(String square_string) throws IOException {
        String[] parts = square_string.split("_");
        int points = Integer.parseInt(parts[1]);
        String findingClass = parts[2].split("@")[0];

        Finding finding;
        switch (findingClass) {
            case "Model.Finding.DiskosFaistou":
                finding = new DiskosFaistou(points, AnaktoroEnum.FAISTOS);
                break;
            case "Model.Finding.DaxtylidiMinoa":
                finding = new DaxtylidiMinoa(points, AnaktoroEnum.KNOSSOS);
                break;
            case "Model.Finding.RytoZakrou":
                finding = new RytoZakrou(points, AnaktoroEnum.ZAKROS);
                break;
            case "Model.Finding.KosmimaMaliwn":
                finding = new KosmimaMaliwn(points, AnaktoroEnum.MALIA);
                break;
            case "Model.Finding.Agalmataki":
                finding = new Agalmataki();
                break;
            default:
                throw new IOException(findingClass);
        }

        return new FindingSquare(points, finding);
    }

    /**
     * <b>Transformer</b>
     * method to parse a 2D array string into an actual int array.
     * @throws IOException If parsing fails.
     * @param arrayString The string representation of a 2D array.
     * @return a 2D array of integers.
     */
    private int[][] parse2DArray(String arrayString) throws IOException {
        try {
            String cleared = arrayString.replace("[[", "").replace("]]", "");
            String[] rows = cleared.split("], \\[");
            int[][] result = new int[rows.length][];
            for (int i = 0; i < rows.length; i++) {
                String[] elements = rows[i].split(", ");
                result[i] = new int[elements.length];
                for (int j = 0; j < elements.length; j++) {
                    result[i][j] = Integer.parseInt(elements[j]);
                }
            }
            return result;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException(arrayString, e);
        }
    }

}



