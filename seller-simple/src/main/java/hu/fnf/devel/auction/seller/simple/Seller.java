package hu.fnf.devel.auction.seller.simple;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.api.InvalidOfferException;
import hu.fnf.devel.auction.api.Participant;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class Seller implements Participant, Runnable {
    private final String name;
    private Auction auction;

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
        } catch (InvalidOfferException e) {
            e.printStackTrace();
        }
        auction = null;
    }

    public void onAcceptance(Auction auction, String item, float price) {
        System.out.println(this.name + " sold " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        System.out.println("No bidders accepted asked price for " + item + ", best bid was " + bestBid);
    }

}
