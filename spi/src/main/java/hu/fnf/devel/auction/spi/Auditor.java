package hu.fnf.devel.auction.spi;

import hu.fnf.devel.auction.api.Participant;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public interface Auditor {
    void onAcceptance(Auctioneer auctioneer, Participant participant,
                      String item, float ask,
                      float acceptedBid, Float[] bids);

    void onRejection(Auctioneer auctioneer, Participant participant,
                     String item, float ask,
                     Float[] rejectedBids);

}
