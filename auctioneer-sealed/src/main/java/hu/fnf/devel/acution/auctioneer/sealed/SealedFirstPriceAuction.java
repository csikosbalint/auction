package hu.fnf.devel.acution.auctioneer.sealed;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.api.InvalidOfferException;
import hu.fnf.devel.auction.api.Participant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SealedFirstPriceAuction implements Auction {
    private class Book {
        float ask;
        Participant seller;
        float highestBid;
        Participant highestBidder;
        int numberOfBids;
    }

    private Map<String, Book> openTransactions;
    private final int maxAllowedBids;

    public SealedFirstPriceAuction(int duration) {
        maxAllowedBids = duration;
        openTransactions = new HashMap<String, Book>();
    }

    public Float ask(String item, float price, Participant seller)
            throws InvalidOfferException {
        if (price <= 0.0f) {
            throw new InvalidOfferException("Ask must be greater than zero.");
        }

        Book book = openTransactions.get(item);

        if (book == null) {
            book = new Book();
            openTransactions.put(item, book);

        } else if (book.seller != null) {
            throw new InvalidOfferException("Item [" + item + "] has already being auctioned.");
        }

        book.ask = price;
        book.seller = seller;

        System.out.println(seller.getName() + " offering item "
                + item + " for the asking price of " + price);

        return price;
    }

    public Float bid(String item, float price, Participant buyer)
            throws InvalidOfferException {
        if (price <= 0.0f) {
            throw new InvalidOfferException("Bid must be greater than zero.");
        }

        Book book = openTransactions.get(item);

        if (book == null) {
            book = new Book();
            openTransactions.put(item, book);
        }

        assert book.numberOfBids < maxAllowedBids;

        if (price > book.highestBid) {
            book.highestBid = price;
            book.highestBidder = buyer;
        }
        if ((++book.numberOfBids) == maxAllowedBids) {
            if (book.seller != null) {
                if (book.highestBid >= book.ask) {
                    book.seller.onAcceptance(this, item, book.highestBid);
                    book.highestBidder.onAcceptance(this, item, book.highestBid);
                } else {
                    book.seller.onRejection(this, item, book.highestBid);
                    book.highestBidder.onRejection(this, item, book.highestBid);
                }
            } else {
                book.highestBidder.onRejection(this, item, book.highestBid);
            }

            openTransactions.remove(item);
        } else {
            System.out.println(buyer.getName() + " bidding for item "
                    + item);
        }

        return null;
    }

}
