package hu.fnf.devel.auction.auditor.sealed;


import java.util.Map;

import hu.fnf.devel.auction.spi.Auditor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SealedFirstPriceAuditorActivator implements BundleActivator {
    private ServiceRegistration serviceRegistration;
    private Logger logger = LoggerFactory.getLogger( SealedFirstPriceAuditor.class );

    public void start(BundleContext bundleContext) throws Exception {
        SealedFirstPriceAuditor sealedFirstPriceAuditor = new SealedFirstPriceAuditor();
        serviceRegistration =
            bundleContext.registerService(
                    Auditor.class.getName(), sealedFirstPriceAuditor
                    , null);
        logger.info( "Service registered: " + sealedFirstPriceAuditor.toString() + " at " + serviceRegistration );
    }

    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
        logger.info( "Service unregistered: " + serviceRegistration );
    }

}
