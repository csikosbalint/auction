package hu.fnf.devel.acution.auctioneer.sealed;

import hu.fnf.devel.auction.spi.Auctioneer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SealedFirstPriceAuctioneerActivator implements BundleActivator {
    private ServiceRegistration serviceRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        SealedFirstPriceAuctioneer auctioneer =
            new SealedFirstPriceAuctioneer();

        serviceRegistration =
            bundleContext.registerService(
                    Auctioneer.class.getName(),
                    auctioneer, auctioneer.getAuctionProperties());
    }

    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
    }

}
