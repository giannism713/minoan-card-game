package Model.Square;

public interface Square {
    int points = 0;

    /**
     * <b>Accessor</b>
     *
     * @return points of the square
     * @Postcondition returns the points of the square
     */
    int getPoints();


    /**
     * <b>Transformer</b>
     *
     * @param points to be set
     * @Postcondition sets the point of the certain square
     */
    void setPoints(int points);


}
