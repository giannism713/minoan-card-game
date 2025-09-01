package Model.Square;


public class SimpleSquare implements Square {
    private int points;

    /**
     * <b>Constructor</b>*
     *
     * @param points is the points of each square
     * @Postcondition creates a square with certain points
     */
    public SimpleSquare(int points) {
        this.points = points;
    }

    /**
     * <b>Accessor</b>
     *
     * @return points of the square
     * @Postcondition returns the points of the square
     */
    @Override
    public int getPoints() {
        return this.points;
    }

    /**
     * <b>Transformer</b>
     *
     * @param points is the points of the
     * @Postcondition sets the point of the certain square
     */
    @Override
    public void setPoints(int points) {

    }

    /**
     * <b>Accessor</b>
     *
     * @return the string of the FindingSquare
     */
    public String toString() {
        return "simple_" + points;
    }
}
