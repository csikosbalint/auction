package hu.fnf.devel.auction.spi;

import hu.fnf.devel.auction.api.Auction;

import java.util.Dictionary;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public interface Auctioneer {
     Auction getAuction();

    Dictionary<String, Object> getAuctionProperties();

}
