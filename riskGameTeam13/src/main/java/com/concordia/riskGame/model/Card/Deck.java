package com.concordia.riskGame.model.Card;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.concordia.riskGame.model.Country.Country;


/**
 * Allows the creation of the Risk deck containing the 42 Risk cards.
 *
 * @author Akshay Shah
 */
public class Deck implements Serializable{

    private int i;
    private static Deck deck;

    private String input;
    private String name;

    private String[] typesArray;

    public  ArrayList<Card> deckOfCards;

    private Card drawCard;

   /**
    *  Creates all cards, one for each territory. Each card has either
     * a type of Infantry, Cavalry, or Artillery.
    * @param list list of country names
 * @return 
    */
    public void setDeckOfCards(List<Country> list) {

        Collections.shuffle(list);

        //Types of cards
        typesArray = new String[]{"Infantry", "Cavalry", "Artillery"};

        deckOfCards = new ArrayList<Card>();

        for (i = 0; i < list.size(); i++) {
            // Add new cards to deck
        	deckOfCards.add(new Card(typesArray[i % 3], list.get(i)));
            //System.out.println("Added new card to deck: " + deck.get(i).getName());
        }
        Collections.shuffle(deckOfCards);
    }

    /**
     * Public default constructor to access other methods.
     */
    public Deck(){
       // deck = new ArrayList<>();
    }
    
    public static Deck getInstance() {
        if (null == deck) {
            	deck = new Deck();
        }
        return deck;
    }

    /**
     * Removes a card from the deck and return it
     * @return card object
     */
    public Card draw() {

        drawCard = deckOfCards.get(0);
        deckOfCards.remove(0);

        return drawCard;
    }

   /**
    * Add a card to the deck
    * @param card name of the card which is to be added to deck
    */
    public void add(Card card) {

        deckOfCards.add(card);
    }

   /**
    * Shuffle the deck of cards
    */
    public void shuffle() {

        Collections.shuffle(deckOfCards);
    }
}
