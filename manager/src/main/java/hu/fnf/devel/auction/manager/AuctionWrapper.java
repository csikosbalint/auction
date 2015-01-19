package hu.fnf.devel.auction.manager;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.api.InvalidOfferException;
import hu.fnf.devel.auction.api.Participant;
import hu.fnf.devel.auction.spi.Auctioneer;
import hu.fnf.devel.auction.spi.Auditor;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class AuctionWrapper implements Auction {
    private Collection<Auditor> auditors;
    private Auctioneer delegate;
    private Map<String, List<Float>> bidsPerItem =
            new HashMap<String, List<Float>>();
    private float ask;

    class ParticipantWrapper implements Participant {

        private Participant delegate;

        public ParticipantWrapper(Participant delegate) {
            this.delegate = delegate;
        }

        public String getName() {
            return delegate.getName();
        }

        public void onAcceptance(Auction auction, String item, float price) {
            delegate.onAcceptance(auction, item, price);

            Float[] bids = bidsPerItem.get(item).toArray(new Float[0]);
            for (Auditor auditor : auditors) {
                auditor.onAcceptance(AuctionWrapper.this.delegate, delegate,
                        item, ask, price, bids);
            }
        }

        public void onRejection(Auction auction, String item,
                                float bestBid) {
            delegate.onRejection(auction, item, bestBid);

            Float[] bids = bidsPerItem.get(item).toArray(new Float[0]);
            for (Auditor auditor : auditors) {
                auditor.onRejection(AuctionWrapper.this.delegate, delegate, item,
                        ask, bids);
            }
        }
    }

    public AuctionWrapper(Auctioneer delegate, Collection<Auditor> auditors) {
        this.delegate = delegate;
        this.auditors = auditors;
    }

    public Float ask(String item, float price, Participant seller)
            throws InvalidOfferException {
        ask = price;
        return delegate.getAuction().ask(item, price,
                new ParticipantWrapper(seller));
    }

    public Float bid(String item, float price, Participant buyer)
            throws InvalidOfferException {
        List<Float> bids = bidsPerItem.get(item);
        if (bids == null) {
            bids = new LinkedList<Float>();
            bidsPerItem.put(item, bids);
        }
        bids.add(price);

        return delegate.getAuction().bid(item, price,
                new ParticipantWrapper(buyer));
    }

}
