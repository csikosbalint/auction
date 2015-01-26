package hu.fnf.devel.auction.seller.simple;

import hu.fnf.devel.auction.api.Auction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SellerActivator {//implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private Auction auctionServiceInstance;
    private Seller seller = new Seller("Seller 1");

    private Logger logger = LoggerFactory.getLogger( SellerActivator.class );

    public Auction getAuctionServiceInstance() {
        return auctionServiceInstance;
    }

    public void setAuctionServiceInstance( Auction auctionServiceInstance ) {
        this.auctionServiceInstance = auctionServiceInstance;
    }

//    public void start(BundleContext bundleContext) throws Exception {
//
//        this.bundleContext = bundleContext;
//
//        String filter =
//            "(&(objectClass=" + Auction.class.getName()
//            + ")(" + Auction.TYPE + "=Sealed-First-Price))";
//
//        ServiceReference[] serviceReferences =
//            bundleContext.getServiceReferences((String) null, filter);
//
//        if (serviceReferences != null) {
//            init( serviceReferences[0] );
//        } else {
//            bundleContext.addServiceListener(this, filter);
//        }
//    }

//    public Auction usingAServiceTracker(BundleContext bundleContext)
//     throws InterruptedException
//    {
//        String filter =
//            "(&(objectClass=" + Auction.class.getName()
//            + ")(" + Auction.TYPE + "=Sealed-First-Price))";
//
//        ServiceTracker tracker =
//            new ServiceTracker(bundleContext, filter, null);
//
//        tracker.open();
//
//        Auction auction = (Auction) tracker.waitForService(0);
//
//        return auction;
//    }


//    public void stop(BundleContext bundleContext) throws Exception {
//        bundleContext.removeServiceListener(this);
//    }
//
//    public void serviceChanged(ServiceEvent serviceEvent) {
//        switch (serviceEvent.getType()) {
//            case ServiceEvent.REGISTERED: {
//                init( serviceEvent.getServiceReference() );
//                break;
//            }
//            default:
//                // do nothing
//        }
//    }
    public void init() {
        logger.info( "Auction auctionServiceInstance: " + auctionServiceInstance.toString() );
        Auction auction = auctionServiceInstance;
        logger.info( "Auction ServiceInstance: " + auction.toString() );
        if (auction != null) {
            seller.ask(auction);
        }
    }

}
