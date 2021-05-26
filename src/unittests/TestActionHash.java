package unittests;

import action.*;
import model.Card;
import model.CardType;
import model.Position;

import java.util.*;

public class TestActionHash
{
    public static void main(String[] args)
    {
        List<Integer> all_hash = new ArrayList<>();
        int n1 = 0;
        int n2 = 0;
        int n3 = 0;

        // Generate all posible actions, store theirs hashCode in a list and count the number different actions

        for (int x1=0; x1<9; x1++)
            for (int y1=0; y1<5; y1++)
                for (int x2=0; x2<9; x2++)
                    for (int y2=0; y2<5; y2++)
                        for (UnitActionType u : UnitActionType.values())
                        {
                            Action a = new UnitAction(new Position(x1, y1), new Position(x2, y2), u);
                            all_hash.add(a.hashCode());
                            n1 += 1;
                        }
        System.out.println("There are " + n1 + " UnitAction");

        for (int x1=0; x1<9; x1++)
            for (int y1=0; y1<5; y1++)
                for (Card c : Card.values())
                {
                    Action a = new DropAction(c, new Position(x1, y1));
                    if (all_hash.contains(a.hashCode()))
                        System.out.println(a);
                    all_hash.add(a.hashCode());
                    n2 += 1;
                }
        System.out.println("There are " + n2 + " DropAction");

        for (Card c : Card.values())
        {
            Action a = new SwapCardAction(c);
            all_hash.add(a.hashCode());
            n3 += 1;
        }
        System.out.println("There are " + n3 + " SwapCardAction");

        // If all works, the thrre numbers must be the same
        System.out.println("Number different actions       : " + (n1 + n2 + n3));
        System.out.println("Number of elements in the list : "  + all_hash.size());

        HashSet <Integer> hashSetNumbers = new HashSet<Integer>(all_hash);
        System.out.println("Number of unique elements      : " + hashSetNumbers.size());


    }
}