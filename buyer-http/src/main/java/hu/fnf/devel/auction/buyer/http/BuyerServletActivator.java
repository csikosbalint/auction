package hu.fnf.devel.auction.buyer.http;

import hu.fnf.devel.auction.api.Auction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class BuyerServletActivator implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private BidderServlet bidderServlet = new BidderServlet("Http Bidder");
    private HttpService httpService;

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
        bundleContext.removeServiceListener(this);
        if (httpService != null) {
            httpService.unregister("/bidder");
        }
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        try {
            switch (serviceEvent.getType()) {
                case ServiceEvent.REGISTERED: {
                    start(serviceEvent.getServiceReference());
                    break;
                }
                case ServiceEvent.UNREGISTERING: {
                    stop(bundleContext);
                    break;
                }
                default:
                    // ignore other service events
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start(ServiceReference serviceReference)
            throws ServletException, NamespaceException {
        Auction auction = (Auction)
                bundleContext.getService(serviceReference);

        if (auction != null) {
            bidderServlet.setAuction(auction);

            ServiceReference ref =
                    bundleContext.getServiceReference(HttpService.class.getName());

            httpService =
                    (HttpService) bundleContext.getService(ref);
            httpService.registerServlet("/bidder", bidderServlet, null, null);
        }
    }

}
