package hu.fnf.devel.auction.manager;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.api.InvalidOfferException;
import hu.fnf.devel.auction.api.Participant;
import hu.fnf.devel.auction.spi.Auctioneer;
import hu.fnf.devel.auction.spi.Auditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger = LoggerFactory.getLogger( AuctionWrapper.class );

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
        logger.info( "My delegate: " + delegate.toString() );
        logger.info( "My auditors: " + auditors.toString() );
        this.delegate = delegate;
        this.auditors = auditors;
    }

    public Float ask(String item, float price, Participant seller)
            throws InvalidOfferException {
        ask = price;
        logger.info( "I am " + this.toString() + " recv ask");
        logger.info( "My delegate service " + delegate.toString() + " recv ask");
        return delegate.getAuction().ask(item, price,
                new ParticipantWrapper(seller));
    }

    public Float bid(String item, float price, Participant buyer)
            throws InvalidOfferException {

        logger.info( "I am " + this.toString() + " recv bid");
        logger.info( "My delegate service " + delegate.toString() + " recv bid");
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
