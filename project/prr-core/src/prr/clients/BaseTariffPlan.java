package prr.clients;

import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.exceptions.UnrecognizedEntryException;
import prr.communications.VideoCommunication;

import java.io.Serializable;

import prr.clients.TariffPlan;

import java.io.Serial;

public class BaseTariffPlan implements TariffPlan{

    /** Serial number for serialization. */
    @Serial
    private static final long serialVersionUID = 202208091753L;

    private String _name = "BASE";

    /* friend discount factor */
    private static final double FRIEND_FACTOR = 0.5;

    @Override
    public String getName(){
        return _name;

    }


    public double calculateNormalPrice(Communication communication, boolean friends) 
        throws UnrecognizedEntryException{

            
            double price = switch(communication.getType()) {
                case "TEXT" -> calculateTextPriceNormal(communication, friends);
                case "VOICE" -> calculateVoicePriceNormal(communication, friends);
                case "VIDEO" -> calculateVideoPriceNormal(communication, friends);
                default -> throw new UnrecognizedEntryException(communication.getType());
                };

            return price;
    }


    public double calculateGoldPrice(Communication communication, boolean friends)
        throws UnrecognizedEntryException{
        
            
            double price = switch(communication.getType()) {
                case "TEXT" -> calculateTextPriceGold(communication, friends);
                case "VOICE" -> calculateVoicePriceGold(communication, friends);
                case "VIDEO" -> calculateVideoPriceGold(communication, friends);
                default -> throw new UnrecognizedEntryException(communication.getType());
                };

            return price;
    }

    public double calculatePlatinumPrice(Communication communication, boolean friends)
        throws UnrecognizedEntryException{

            double price = switch(communication.getType()) {
                case "TEXT" -> calculateTextPricePlatinum(communication, friends);
                case "VOICE" -> calculateVoicePricePlatinum(communication, friends);
                case "VIDEO" -> calculateVideoPricePlatinum(communication, friends);
                default -> throw new UnrecognizedEntryException(communication.getType());
                };

            return price;
    }

    
    public double calculateTextPriceNormal(Communication communication, boolean friends){ 
           long units =  communication.getUnits();
           double price;
    
           if(units<50)
                price = 10;
           
           else if(units>=50 && units<100)
                price = 16;
           
           else
                price =  2*units;
           

           
        
            return price;
    }
      
    
    public double calculateTextPriceGold(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price;
      
        if(units<100)
            price = 10;
    
        else
            price = 2*units;
        
        

        return price;

    }
    
    public double calculateTextPricePlatinum(Communication communication, boolean friends){
        long units =  communication.getUnits();
        double price;

        if(units<50)
            price = 0;
        
        else
            price = 4;
        
        return price;
        
    }

    
    public double calculateVoicePriceNormal(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price = units*20;

        if(friends){
            return price*FRIEND_FACTOR;
        }
            
        return price;
    }

   
    public double calculateVoicePriceGold(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price = units*10;

        if(friends){
            return price*FRIEND_FACTOR;
        }
            
        return price;
    }

    
    public double calculateVoicePricePlatinum(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price = units*10;

        if(friends){
            return price*FRIEND_FACTOR;
        }
        return price;
    }

    
    public double calculateVideoPriceNormal(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price = units*30;

        if(friends)
            return price*FRIEND_FACTOR;
        return price;
    }

    
    public double calculateVideoPriceGold(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price = units*20;

        if(friends)
            return price*FRIEND_FACTOR;
        return price;
    }

    
    public double calculateVideoPricePlatinum(Communication communication, boolean friends){
        long units = communication.getUnits();
        double price = units*10;

        if(friends)
            return price*FRIEND_FACTOR;
        return price;
    }
}