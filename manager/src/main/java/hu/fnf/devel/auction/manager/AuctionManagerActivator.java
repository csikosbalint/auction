package hu.fnf.devel.auction.manager;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.spi.Auctioneer;
import hu.fnf.devel.auction.spi.Auditor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class AuctionManagerActivator implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private Map<ServiceReference, ServiceRegistration> registeredAuctions =
            new HashMap<ServiceReference, ServiceRegistration>();
    private Map<ServiceReference, Auditor> registeredAuditors =
            new HashMap<ServiceReference, Auditor>();
    private Logger logger = LoggerFactory.getLogger( AuctionManagerActivator.class );

    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;

        String auctionOrAuctioneerFilter =
                "(|" +
                        "(objectClass=" + Auctioneer.class.getName() + ")" +
                        "(objectClass=" + Auditor.class.getName() + ")" +
                        ")";

        ServiceReference[] references =
                bundleContext.getServiceReferences((String) null, auctionOrAuctioneerFilter);

        if (references != null) {
            for (ServiceReference serviceReference : references) {
                logger.info( "Auctioneer or Auditor ServiceReference: " + references.toString() );
                registerService( serviceReference );
            }
        }

        bundleContext.addServiceListener(this,
                auctionOrAuctioneerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        ServiceReference serviceReference = serviceEvent.getServiceReference();

        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                registerService(serviceReference);
                break;
            }
            case ServiceEvent.UNREGISTERING: {
                String[] serviceInterfaces =
                        (String[]) serviceReference.getProperty("objectClass");
                if (Auctioneer.class.getName().equals(serviceInterfaces[0])) {
                    unregisterAuctioneer(serviceReference);
                } else {
                    unregisterAuditor(serviceReference);
                }
                bundleContext.ungetService(serviceReference);
                break;
            }
            default:
                // do nothing
        }
    }

    private void registerService(ServiceReference serviceReference) {

        Object serviceObject =
                bundleContext.getService(serviceReference);

        if (serviceObject instanceof Auctioneer)

        {
            registerAuctioneer(serviceReference, (Auctioneer) serviceObject);
        } else {
            registerAuditor(serviceReference, (Auditor) serviceObject);
        }

    }

    private void registerAuditor(ServiceReference auditorServiceReference, Auditor auditor) {
        registeredAuditors.put(auditorServiceReference, auditor);
        logger.info( "Auditor ServiceInstance added to map: " + auditor.toString() );
    }

    private void registerAuctioneer(ServiceReference auctioneerServiceReference,
                                    Auctioneer auctioneer) {
        Auction auction =
                new AuctionWrapper(auctioneer, registeredAuditors.values());

        ServiceRegistration auctionServiceRegistration =
                bundleContext.registerService(Auction.class.getName(),
                        auction, auctioneer.getAuctionProperties());
        logger.info( "Service registered: " + auction.toString() + " at " + auctionServiceRegistration );
        registeredAuctions.put(auctioneerServiceReference, auctionServiceRegistration);
    }

    private void unregisterAuditor(ServiceReference serviceReference) {
        registeredAuditors.remove(serviceReference);
    }

    private void unregisterAuctioneer(ServiceReference auctioneerServiceReference) {
        ServiceRegistration auctionServiceRegistration =
                registeredAuctions.remove(auctioneerServiceReference);

        if (auctionServiceRegistration != null) {
            auctionServiceRegistration.unregister();
            logger.info( "Service unregistered: " + auctionServiceRegistration );
        }
    }


}
