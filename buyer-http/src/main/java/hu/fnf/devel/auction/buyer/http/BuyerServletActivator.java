package hu.fnf.devel.auction.buyer.http;

import javax.servlet.ServletException;

import hu.fnf.devel.auction.api.Auction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.c
 */
public class BuyerServletActivator implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private BidderServlet bidderServlet = new BidderServlet("Http Bidder");
    private HttpService httpService;
    private Logger logger = LoggerFactory.getLogger( BuyerServletActivator.class );

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
        logger.info( "Auction ServiceReference: " + serviceReference.toString() );
        Auction auction = (Auction)
                bundleContext.getService(serviceReference);

        if (auction != null) {
            bidderServlet.setAuction(auction);
            logger.info( "Auction ServiceInstance: " + auction.toString() );

            ServiceReference ref =
                    bundleContext.getServiceReference(HttpService.class.getName());
            logger.info( "Auction ServiceInstance: " + auction.toString() );

            logger.info( "HttpService ServiceReference: " + ref.toString() );
            httpService =
                    (HttpService) bundleContext.getService(ref);

            logger.info( "HttpService ServiceInstance: " + httpService.toString() );

            httpService.registerServlet("/bidder", bidderServlet, null, null);

            logger.info( "bidder is registeres!" );
        }
    }

}
