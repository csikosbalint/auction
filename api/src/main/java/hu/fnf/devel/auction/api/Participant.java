package hu.fnf.devel.auction.api;
/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public interface Participant {
        String getName();

    void onAcceptance(Auction auction, String item, float price);

    void onRejection(Auction auction, String item, float bestBid);
}
