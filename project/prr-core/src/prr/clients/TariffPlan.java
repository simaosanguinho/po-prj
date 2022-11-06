package prr.clients;

import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.exceptions.UnrecognizedEntryException;
import prr.communications.VideoCommunication;

import java.io.Serializable;
import java.io.Serial;



public interface TariffPlan extends Serializable{


    public double calculateNormalPrice(Communication communication, boolean friends) 
        throws UnrecognizedEntryException;

    public double calculateGoldPrice(Communication communication, boolean friends) 
        throws UnrecognizedEntryException;

    public double calculatePlatinumPrice(Communication communication, boolean friends)
        throws UnrecognizedEntryException;

    public double calculateTextPriceNormal(Communication communication, boolean friends);

    public double calculateTextPriceGold(Communication communication, boolean friends);

    public double calculateTextPricePlatinum(Communication communication, boolean friends);

    public double calculateVoicePriceNormal(Communication communication, boolean friends);

    public double calculateVoicePriceGold(Communication communication, boolean friends);

    public double calculateVoicePricePlatinum(Communication communication, boolean friends);

    public double calculateVideoPriceNormal(Communication communication, boolean friends);

    public double calculateVideoPriceGold(Communication communication, boolean friends);

    public double calculateVideoPricePlatinum(Communication communication, boolean friends);

    public String getName();
    
}
