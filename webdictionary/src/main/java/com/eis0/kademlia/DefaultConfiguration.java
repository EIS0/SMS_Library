package com.eis0.kademlia;

import java.io.File;

/**
 * A set of Kademlia configuration parameters.
 * Default values are supplied and can be changed by the application as necessary.
 *
 */
public class DefaultConfiguration implements KadConfiguration {

    private final static int K = 5; // system-wide replication parameter
    private final static int RCSIZE = 3;
    private final static int STALE = 1;
    private static final String FOLDER = "kademlia";

    /**
     * Default constructor to support Gson Serialization
     */
    public DefaultConfiguration() {

    }

    /**
     *
     * @return int representing K parameter
     */
    public int k() {
        return K;
    }

    /**
     *
     * @return int representing the size of the replacement chache
     */
    public int replacementCacheSize() {
        return RCSIZE;
    }

    /**
     *
     * @return int representing how many time there was a stale
     */
    public int stale() {
        return STALE;
    }

    /**
     *
     * @param ownerId
     *
     * @return String representing a node data folder
     */
    public String getNodeDataFolder(String ownerId) {
        /* Setup the main storage folder if it doesn't exist */
        String path = System.getProperty("user.home") + File.separator + DefaultConfiguration.FOLDER;
        File folder = new File(path);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }

        /* Setup subfolder for this owner if it doesn't exist */
        File ownerFolder = new File(folder + File.separator + ownerId);
        if (!ownerFolder.isDirectory()) {
            ownerFolder.mkdir();
        }

        /* Return the path */
        return ownerFolder.toString();
    }
}