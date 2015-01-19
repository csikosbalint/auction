package hu.fnf.devel.auction.auditor.sealed;

import hu.fnf.devel.auction.spi.Auditor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class SealedFirstPriceAuditorActivator implements BundleActivator {
    private ServiceRegistration serviceRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        serviceRegistration =
            bundleContext.registerService(
                    Auditor.class.getName(),
                    new SealedFirstPriceAuditor(), null);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
    }

}
