package hu.fnf.devel.acution.auctioneer.sealed;

import hu.fnf.devel.auction.spi.Auctioneer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SealedFirstPriceAuctioneerActivator implements BundleActivator {
    private ServiceRegistration serviceRegistration;
    private Logger logger = LoggerFactory.getLogger( SealedFirstPriceAuctioneer.class );

    public void start(BundleContext bundleContext) throws Exception {
        SealedFirstPriceAuctioneer auctioneer =
            new SealedFirstPriceAuctioneer();

        serviceRegistration =
            bundleContext.registerService(
                    Auctioneer.class.getName(),
                    auctioneer, auctioneer.getAuctionProperties());
        logger.info( "Service registered: " + auctioneer.toString() + " at " + serviceRegistration );
    }

    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
        logger.info( "Service unregistered: " + serviceRegistration );
    }

}
