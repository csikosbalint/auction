package hu.fnf.devel.auction.api;
/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public interface Auction {
        /**
     * Value: String
     */
    String TYPE = "auction-type";

    /**
     * Value: Integer
     */
    String DURATION = "auction-duration";

    // Service Methods

    Float ask(String item, float price, Participant seller) throws InvalidOfferException;

    Float bid(String item, float price, Participant buyer) throws InvalidOfferException;


}
