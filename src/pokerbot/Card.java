package pokerbot;

import java.io.File;

public class Card {
	
	//Declaration of instance variables
    private String suitString;
    private int suitValue;
    private int value;
    private File image;
    
    // Constructor, sets suit and value of the card
    public Card(int suit, int value) {
    	
    	//Determines whether the card is a diamond, heart, club or spade
        if(suit == 1) {
            suitString = "diamonds";
        } else if (suit == 2) {
            suitString = "hearts";
        } else if (suit == 3) {
            suitString = "clubs";
        } else if (suit == 4) {
            suitString = "spades";
        }
        
        suitValue = suit;
        this.value = value;
        
        String filePath = "cards/" +suitString + "/" + value + ".jpg";
        image = new File(filePath);
    }
    
    public int getValue() { //Function that returns the value of the card
    	return value;
    }
    
    public int getSuitValue() { //Function that returns the numerical value of the suit of the card
    	return suitValue;
    }
    
    public String getSuitString() { //Function that returns the string value of the suit of the card
    	return suitString;
    }
    
    public String toString() { //Function that returns suit and value of the card
        return value + " of " + suitString;
    }
    
    public File getFile() { //Function that returns suit and value of the card
        return image;
    }
}

