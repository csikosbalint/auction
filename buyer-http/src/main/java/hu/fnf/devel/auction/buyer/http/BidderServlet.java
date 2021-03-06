package hu.fnf.devel.auction.buyer.http;

import hu.fnf.devel.auction.api.Auction;
import hu.fnf.devel.auction.api.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class BidderServlet implements Servlet, Participant {
    private Auction auction;
    private String name;
    private PrintWriter writer;
    private Logger logger = LoggerFactory.getLogger( BidderServlet.class );

    public BidderServlet(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public void destroy() {
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public String getServletInfo() {
        return null;
    }

    public void init(ServletConfig config) throws ServletException {
    }

    public void service(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {

        logger.info( "serving with Auction ServiceInstance: " + auction.toString() );

        String bidValue =
                req.getParameter("bid");

        String item =
                req.getParameter("item");

        try {
            if (bidValue == null || item == null) {
                throw new IllegalArgumentException("Invalid bid");
            } else {
                writer = resp.getWriter();
                Float price = new Float(bidValue);
                auction.bid(item, price, this);

                logger.info("Accepted bid of "
                        + bidValue + " for item " + item);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void onAcceptance(Auction auction, String item, float price) {
        logger.info(this.name + " was awarded " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        logger.info("Bid for " + item + " from " + name + " was rejected");
    }

}
