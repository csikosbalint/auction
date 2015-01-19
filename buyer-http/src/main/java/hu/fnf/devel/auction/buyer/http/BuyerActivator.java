package hu.fnf.devel.auction.buyer.http;

import hu.fnf.devel.auction.api.Auction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class BuyerActivator implements BundleActivator, ServiceListener{
    private static final int BUYER_SERVER_PORT = 9090;

    private BundleContext bundleContext;
    private BidderServer server = new BidderServer("Buyer 1");

    public void start(BundleContext bundleContext) throws Exception {

        this.bundleContext = bundleContext;

        String filter =
            "(&(objectClass=" + Auction.class.getName()
            + ")(" + Auction.TYPE + "=Sealed-First-Price))";

        ServiceReference[] serviceReferences =
            bundleContext.getServiceReferences((String)null, filter);

        if (serviceReferences != null) {
            start(serviceReferences[0]);
        } else {
            bundleContext.addServiceListener(this, filter);
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        server.stop();
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                start(serviceEvent.getServiceReference());
                break;
            }
            case ServiceEvent.UNREGISTERING: {
                server.stop();
                break;
            }
        }
    }

    private void start(ServiceReference serviceReference) {
        Auction auction = (Auction)
            bundleContext.getService(serviceReference);

        if (auction != null) {
            server.setAuction(auction);
            server.start(BUYER_SERVER_PORT);
        }
    }

}
