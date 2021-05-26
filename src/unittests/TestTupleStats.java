package unittests;

import ai.ntbea.TupleStats;

public class TestTupleStats
{
    public static void main(String[] args)
    {
        TupleStats ts = new TupleStats();

        System.out.println("- Testing TupleStats -");
        System.out.println("----------------------");

        System.out.println("\nEmpty");
        System.out.println(ts);
        System.out.println("----------------------");

        System.out.println(" \nAdd 4");
        ts.add(4);
        System.out.println(ts);
        System.out.println("----------------------");

        System.out.println(" \nAdd 6");
        ts.add(6);
        System.out.println(ts);
        System.out.println("----------------------");
    }
}
