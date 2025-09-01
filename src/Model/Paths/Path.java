package Model.Paths;

import Model.Card.AnaktoroEnum;
import Model.Card.Card;
import Model.Finding.*;
import Model.Square.FindingSquare;
import Model.Square.SimpleSquare;
import Model.Square.Square;

import java.util.ArrayList;
import java.util.Random;

public class Path {
    private AnaktoroEnum anaktoro;
    private Square[] squares_array = new Square[9];


    /**
     * <b>Constructor</b>
     * makes a path for a certain of the four anaktora
     * @param anaktoro is the place where this path belongs to
     * @Precondition anaktoro is from the enum
     * @Postcondition creates a path that contains 9 squares in an array for a certain anaktoro
     */
    public Path(AnaktoroEnum anaktoro) {
        // fill every square of the path no matter what and then replace one with the rare finding
        squares_array[0] = new SimpleSquare(-20);
        squares_array[1] = new SimpleSquare(-15);
        squares_array[2] = new SimpleSquare(-10);
        squares_array[3] = new SimpleSquare(5);// here too
        squares_array[4] = new SimpleSquare(10);
        squares_array[5] = new SimpleSquare(15);
        squares_array[6] = new SimpleSquare(30);
        squares_array[7] = new SimpleSquare(35);
        squares_array[8] = new SimpleSquare(50);
        int r_index = getRandomIndex();
        int points = squares_array[r_index].getPoints();
        if (anaktoro == AnaktoroEnum.FAISTOS) {
            DiskosFaistou DiskosFaistou = new DiskosFaistou(35, AnaktoroEnum.FAISTOS);
            squares_array[r_index] = new FindingSquare(points, DiskosFaistou);
        } else if (anaktoro == AnaktoroEnum.KNOSSOS) {
            DaxtylidiMinoa DaxtylidiMinoa = new DaxtylidiMinoa(25, AnaktoroEnum.KNOSSOS);
            squares_array[r_index] = new FindingSquare(points, DaxtylidiMinoa);
        } else if (anaktoro == AnaktoroEnum.ZAKROS) {
            RytoZakrou RytoZakrou = new RytoZakrou(25, AnaktoroEnum.ZAKROS);
            squares_array[r_index] = new FindingSquare(points, RytoZakrou);
        } else {
            KosmimaMaliwn KosmimaMaliwn = new KosmimaMaliwn(25, AnaktoroEnum.MALIA);
            squares_array[r_index] = new FindingSquare(points, KosmimaMaliwn);
        }
    }

    private int getRandomIndex() {
        int[] indexes_available = {1, 3, 5, 7, 8};
        Random random = new Random();
        return indexes_available[random.nextInt(indexes_available.length)];
    }

    /**
     * <b>Accessor</b>
     * access the squares of the current path
     * @return an array that contains the squares of the certain path
     */
    public Square[] get_squares_array() {
        return squares_array;
    }

    /**
     * <b>Transformer</b>
     * set the squares of the path
     * @param squares is the squares to be put inside the path
     * @Precondition the size of the squares array is 9 and
     * @Postcondition the squares of the path have been set
     */
    public void set_squares_array(Square[] squares) {
        squares_array = squares;
    }

    /**
     * <b>Transformer</b>
     * @return everything in a string array of the path
     */
    public String toString() {
        ArrayList<String> string_array = new ArrayList<>();
        for (Square square : squares_array) {
            string_array.add(square.toString());
        }
        return String.join("__", string_array);
    }
}




