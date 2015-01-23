package hu.fnf.devel.auction.api;

/**
 * Created by Balint Csikos (csikos.balint@fnf.hu) on 18/01/15.
 */
public class InvalidOfferException extends Exception {

    public InvalidOfferException(String message) {
        super(message);
    }
}
