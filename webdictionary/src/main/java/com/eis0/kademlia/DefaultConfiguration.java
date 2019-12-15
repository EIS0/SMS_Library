package com.eis0.kademlia;

/**
 * A set of Kademlia configuration parameters.
 * Default values are supplied and can be changed by the application as necessary.
 *
 * @author Edoardo Raimondi
 */
public class DefaultConfiguration implements KadConfiguration {

    private final static int K = 2; // system-wide replication parameter
    private final static int RCSIZE = 3; //replacement cache size
    private final static int STALE = 0;
    private static final String FOLDER = "kademlia";

    /**
     * Default constructor to support Gson Serialization
     */
    public DefaultConfiguration() {

    }

    /**
     * @return int representing K parameter
     */
    public int k() {
        return K;
    }

    /**
     * @return int representing the size of the replacement chache
     */
    public int replacementCacheSize() {
        return RCSIZE;
    }

    /**
     * @return int representing how many time there was a stale
     */
    public int stale() {
        return STALE;
    }

}