package pl.lodz.p.ics.model.transform;

/**
 * User: maciek
 * Date: 15.10.13
 * Time: 13:23
 */
public class Ellipse {

    /**
     * Maio, Maltoni: a
     */
    private double semiAxeWidth;
    /**
     * Maio, Maltoni: b
     */
    private double semiAxeHeight;

    /**
     * Maio, Maltoni: expansion coefficient
     */
    private double expansionCoefficient;

    /**
     * Maio, Maltoni: reduction coefficient
     */
    private double reductionCoefficient;

    private double maxSemiAxeHeight;
    private double minSemiAxeHeight;
    private double maxSemiAxeWidth;
    private double minSemiAxeWidth;

    public Ellipse(double semiAxeWidth, double semiAxeHeight, double expansionCoefficient, double reductionCoefficient) {
        this.semiAxeWidth = semiAxeWidth;
        this.semiAxeHeight = semiAxeHeight;
        this.expansionCoefficient = expansionCoefficient;
        this.reductionCoefficient = reductionCoefficient;
        this.maxSemiAxeHeight = semiAxeHeight * expansionCoefficient;
        this.minSemiAxeHeight = semiAxeHeight * reductionCoefficient;
        this.maxSemiAxeWidth =  semiAxeWidth * expansionCoefficient;
        this.minSemiAxeWidth = semiAxeWidth * reductionCoefficient;
    }

    public double getSemiAxeWidth() {
        return semiAxeWidth;
    }

    public void setSemiAxeWidth(double semiAxeWidth) {
        this.semiAxeWidth = semiAxeWidth;
    }

    public double getSemiAxeHeight() {
        return semiAxeHeight;
    }

    public void setSemiAxeHeight(double semiAxeHeight) {
        this.semiAxeHeight = semiAxeHeight;
    }

    public double getExpansionCoefficient() {
        return expansionCoefficient;
    }

    public void setExpansionCoefficient(double expansionCoefficient) {
        this.minSemiAxeWidth = semiAxeWidth * expansionCoefficient;
        this.minSemiAxeHeight = semiAxeHeight * expansionCoefficient;
        this.expansionCoefficient = expansionCoefficient;
    }

    public double getReductionCoefficient() {
        return reductionCoefficient;
    }

    public void setReductionCoefficient(double reductionCoefficient) {
        this.minSemiAxeWidth = semiAxeWidth * reductionCoefficient;
        this.minSemiAxeHeight = semiAxeHeight * reductionCoefficient;
        this.reductionCoefficient = reductionCoefficient;
    }

    public double getMaxSemiAxeHeight() {
        return maxSemiAxeHeight;
    }

    public double getMaxSemiAxeWidth() {
        return maxSemiAxeWidth;
    }

    public double getMinSemiAxeHeight() {
        return minSemiAxeHeight;
    }

    public double getMinSemiAxeWidth() {
        return minSemiAxeWidth;
    }

}
