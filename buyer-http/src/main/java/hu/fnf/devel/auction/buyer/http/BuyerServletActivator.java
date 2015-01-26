package hu.fnf.devel.auction.buyer.http;

import javax.servlet.ServletException;

import hu.fnf.devel.auction.api.Auction;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.c
 */
public class BuyerServletActivator { //implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private BidderServlet bidderServlet = new BidderServlet("Http Bidder");
    private HttpService httpServiceInstance;
    private Logger logger = LoggerFactory.getLogger( BuyerServletActivator.class );

    private Auction auctionServiceInstance;

    public Auction getAuctionServiceInstance() {
        return auctionServiceInstance;
    }

    public void setAuctionServiceInstance( Auction auctionServiceInstance ) {
        this.auctionServiceInstance = auctionServiceInstance;
    }

    public HttpService getHttpServiceInstance() {
        return httpServiceInstance;
    }

    public void setHttpServiceInstance( HttpService httpServiceInstance ) {
        this.httpServiceInstance = httpServiceInstance;
    }

    public void stop(BundleContext bundleContext) throws Exception {
        if ( httpServiceInstance != null) {
            httpServiceInstance.unregister( "/bidder" );
        }
    }

    public void init()
            throws ServletException, NamespaceException {
        logger.info( "Auction ServiceInstance: " + auctionServiceInstance.toString() );
        Auction auction = auctionServiceInstance;
        if (auction != null) {
            bidderServlet.setAuction(auction);
            logger.info( "Auction ServiceInstance: " + auction.toString() );

            logger.info( "Auction ServiceInstance: " + auction.toString() );

            logger.info( "HttpService ServiceInstance: " + httpServiceInstance.toString() );

            httpServiceInstance.registerServlet( "/bidder", bidderServlet, null, null );

            logger.info( "bidder is registeres!" );
        }
    }

}
