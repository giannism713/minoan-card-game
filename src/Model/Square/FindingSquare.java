package Model.Square;

import Model.Finding.Finding;
import Model.Finding.Agalmataki;

public class FindingSquare implements Square {
    private int points;
    private Finding finding;

    /**
     * <b>Constructor</b>*
     *
     * @param points  is the points of each square
     * @param finding is the finding of the square
     * @Postcondition creates a square with certain points and a certain finding
     */
    public FindingSquare(int points, Finding finding) {
        this.points = points;
        this.finding = finding;
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
     * @param points points to be set for the finding square
     * @Postcondition sets the point of the certain square
     */
    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the finding of the FindingSquare
     */
    public Finding getFinding() {
        return this.finding;
    }

    /**
     * <b>Accessor</b>
     *
     * @return the string of the FindingSquare
     */
    public String toString() {
        return "finding_" + points + "_" + getFinding().toString();
    }

}
