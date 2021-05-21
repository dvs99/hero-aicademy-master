
package ai.ntbea;

import java.util.List;


//TODO: REVIEW AND KEEP JUST WHAT WE NEED, UNIT TESTING
//Handles stats as needed
//Mostly taken from https://github.com/SimonLucas/ntbea/blob/5f91d3a5cbc42bd6dfb9406541b9223b4c83fc1f/java/src/utilities/StatSummary.java
public class TupleStats implements Comparable<TupleStats> {


    private double sum;
    private double sumsq;
    private double min;
    private double max;

    private double mean;
    private double sd;


    private boolean strict = false;

    int n;
    boolean valid;

    public TupleStats() {
        // System.out.println("Creating TupleStats");
        n = 0;
        sum = 0;
        sumsq = 0;
        // ensure that the first number to be
        // added will fix up min and max to
        // be that number
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
        // System.out.println("Finished Creating SS");
        valid = false;
    }

    public TupleStats setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public final void reset() {
        n = 0;
        sum = 0;
        sumsq = 0;
        // ensure that the first number to be
        // added will fix up min and max to
        // be that number
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
    }


    static String strictMessage = "No values in summary";

    public double max() {
        if (strict && n < 1) throw new RuntimeException(strictMessage);
        return max;
    }

    public double min() {
        if (strict && n < 1) throw new RuntimeException(strictMessage);
        return min;
    }

    public double mean() {
        if (strict && n < 1) throw new RuntimeException(strictMessage);
        if (!valid)
            computeStats();
        return mean;
    }

    public double sum() {
        if (strict && n < 1) throw new RuntimeException(strictMessage);
        return sum;
    }

    // returns the sum of the squares of the differences
    //  between the mean and the ith values
    public double sumSquareDiff() {
        return sumsq - n * mean() * mean();
    }


    private void computeStats() {
        if (!valid) {
            mean = sum / n;
            double num = sumsq - (n * mean * mean);
            if (num < 0) {
                // avoids tiny negative numbers possible through imprecision
                num = 0;
            }
            // System.out.println("Num = " + num);
            sd = Math.sqrt(num / (n - 1));
            // System.out.println(" GVGAISimpleTest: sd = " + sd);
            // System.out.println(" GVGAISimpleTest: n = " + n);
            valid = true;
        }
    }

    public double sd() {
        if (strict && n < 2) throw new RuntimeException(strictMessage);
        if (!valid)
            computeStats();
        return sd;
    }

    public int n() {
        return n;
    }

    public double stdErr() {
        return sd() / Math.sqrt(n);
    }


    public TupleStats add(double d) {
        n++;
        sum += d;
        sumsq += d * d;
        min = Math.min(min, d);
        max = Math.max(max, d);
        valid = false;
        return this;
    }

    public TupleStats add(double... xa) {
        for (double x : xa) {
            add(x);
        }
        return this;
    }

    public TupleStats add(List<Double> xa) {
        for (double x : xa) {
            add(x);
        }
        return this;
    }

    public String toString() {
        String s = " min = " + min() + "\n" +
                " max = " + max() + "\n" +
                " ave = " + mean() + "\n" +
                " sd  = " + sd() + "\n" +
                " se  = " + stdErr() + "\n" +
                " sum  = " + sum + "\n" +
                " sumsq  = " + sumsq + "\n" +
                " n   = " + n;
        return s;

    }

    @Override
    public int compareTo(TupleStats other) {
        if (mean() > other.mean()) return 1;
        if (mean() < other.mean()) return -1;
        return 0;
    }
}