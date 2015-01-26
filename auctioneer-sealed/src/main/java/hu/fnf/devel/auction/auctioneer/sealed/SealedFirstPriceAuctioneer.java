package hu.fnf.devel.auction.auctioneer.sealed;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.spi.Auctioneer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SealedFirstPriceAuctioneer implements Auctioneer {
    private Logger logger = LoggerFactory.getLogger( SealedFirstPriceAuctioneer.class );
    private static final String SEALED_FIRST_PRICE = "Sealed-First-Price";
    private final int DURATION = 3;

    private final Dictionary<String, Object> properties =
        new Hashtable<String, Object>();

    private final Auction auction;

    public SealedFirstPriceAuctioneer() {
        properties.put(Auction.TYPE, SEALED_FIRST_PRICE);
        properties.put(Auction.DURATION, DURATION);
        auction = new SealedFirstPriceAuction(DURATION);
        logger.info( "Created: " + auction.toString() );
    }

    public Auction getAuction() {
        return auction;
    }

    public Dictionary<String, Object> getAuctionProperties() {
        return properties;
    }

}
