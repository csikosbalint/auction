package hu.fnf.devel.auction.seller.simple;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.api.InvalidOfferException;
import hu.fnf.devel.auction.api.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class Seller implements Participant, Runnable {
    private final String name;
    private Auction auction;
    private Logger logger = LoggerFactory.getLogger( Seller.class );

    public Seller(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void ask(Auction auction) {
        this.auction = auction;
        new Thread(this).start();
    }

    public void run() {
        try {
            auction.ask("bicycle", 24.0f, this);
            logger.info( "ASk for bicycle has been made via " + auction.toString() );
        } catch (InvalidOfferException e) {
            e.printStackTrace();
        }
        logger.info( "AuctionInstance " + auction.toString() + " has been nulled" );
        auction = null;
    }

    public void onAcceptance(Auction auction, String item, float price) {
        logger.info(this.name + " sold " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        logger.info("No bidders accepted asked price for " + item + ", best bid was " + bestBid);
    }

}
