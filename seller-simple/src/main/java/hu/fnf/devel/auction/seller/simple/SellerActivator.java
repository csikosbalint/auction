package hu.fnf.devel.auction.seller.simple;

import hu.fnf.devel.auction.api.Auction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SellerActivator implements BundleActivator, ServiceListener {
        private BundleContext bundleContext;
    private Seller seller = new Seller("Seller 1");

    public void start(BundleContext bundleContext) throws Exception {

        this.bundleContext = bundleContext;

        String filter =
            "(&(objectClass=" + Auction.class.getName()
            + ")(" + Auction.TYPE + "=Sealed-First-Price))";

        ServiceReference[] serviceReferences =
            bundleContext.getServiceReferences((String) null, filter);

        if (serviceReferences != null) {
            ask(serviceReferences[0]);
        } else {
            bundleContext.addServiceListener(this, filter);
        }
    }

    public Auction usingAServiceTracker(BundleContext bundleContext)
     throws InterruptedException
    {
        String filter =
            "(&(objectClass=" + Auction.class.getName()
            + ")(" + Auction.TYPE + "=Sealed-First-Price))";

        ServiceTracker tracker =
            new ServiceTracker(bundleContext, filter, null);

        tracker.open();

        Auction auction = (Auction) tracker.waitForService(0);

        return auction;
    }


    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                ask(serviceEvent.getServiceReference());
                break;
            }
            default:
                // do nothing
        }
    }
    private void ask(ServiceReference serviceReference) {
        Auction auction = (Auction)
            bundleContext.getService(serviceReference);

        if (auction != null) {
            seller.ask(auction);
            bundleContext.ungetService(serviceReference);
        }
    }

}
