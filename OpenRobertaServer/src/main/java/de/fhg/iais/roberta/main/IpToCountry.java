package de.fhg.iais.roberta.main;

/*
This file is part of leafdigital IpToCountry.

IpToCountry is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

IpToCountry is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with IpToCountry.  If not, see <http://www.gnu.org/licenses/>.

Copyright 2010 Samuel Marshall.
*/

/*
This class is simplified for the use in the Open Roberta Lab project by Beate Jost. 
Mainly the whole download implementation is removed due to the fact that an updated 
IpToCountry.cvs can be supposed. For this an external script (likely a cronjob) is 
taking care of updating the database once a month or maybe more often.
 */

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * Converts IP addresses to country codes using the database available from
 * http://software77.net/geo-ip/
 * <p>
 * This class loads the database into memory, where it is stored in an efficient
 * format for instant lookups. It is capable of automatically updating the
 * database on a given time schedule.
 * <p>
 * A disk folder is required to store the latest database version so that it can
 * be loaded initially without requesting it over HTTP. This is important
 * because the list maintainers restrict the number of requests permitted.
 * <p>
 * Currently the database has about 100,000 entries. The in-memory
 * representation in this class uses three arrays to store two longs and a
 * pointer to a (reused) string for each entry, so typical memory consumption is
 * about 2MB. It would be possible to nearly halve this by using ints instead of
 * longs to store the numbers, but I haven't bothered as 2MB seems a reasonable
 * overhead for most servers.
 * <p>
 * For obvious reasons, the memory requirement temporarily doubles while loading
 * a new database version.
 * <p>
 * At present, this class only supports the IP4 database.
 */
public class IpToCountry implements IIpToCountry {
    private static final Logger LOG = LoggerFactory.getLogger(IpToCountry.class);

    private final static String FILE_NAME = "IpToCountry.csv";
    private final static Pattern CSV_LINE = Pattern.compile("^\"([0-9]{1,10})\",\"([0-9]{1,10})\",\"[^\"]*\",\"[0-9]*\",\"([A-Z]+)\"");
    private final static int INITIAL_ENTRIES = 65536;
    private final static long CHECK_FOR_UPDATE_DAYS = 10;

    private long fileLastModified;

    private Informer informer;

    private File file;

    Throwable lastError;

    AtomicReference<DBCountry> atomicDBCountry = new AtomicReference<DBCountry>();

    private class DBCountry {
        final long[] entryFrom, entryTo;
        final String[] entryCode;

        private DBCountry(long[] entryFrom, long[] entryTo, String[] entryCode) {
            this.entryFrom = entryFrom;
            this.entryTo = entryTo;
            this.entryCode = entryCode;
        }
    }

    /**
     * Interface for use to receive information about IpToCountry processing, such
     * as errors when downloading the file.
     */
    public interface Informer {
        /**
         * Called if the file downloads successfully, but cannot be loaded.
         * 
         * @param t Exception
         */
        public void loadFailed(Throwable t);

        /**
         * Called if a line in the file is not understood.
         * 
         * @param errorIndex 0 for first error line, etc
         * @param line Text of line
         */
        public void lineError(int errorIndex, String line);

        /**
         * Called once a file has been successfully loaded.
         * 
         * @param entries Number of entries processed
         * @param milliseconds Time it took to load the file (does not include download)
         * @param lineErrors Number of lines which caused errors
         */
        public void fileLoaded(int entries, int milliseconds, int lineErrors);
    }

    /**
     * Default informer that writes errors to standard error.
     */
    private class DefaultInformer implements Informer {

        @Override
        public void loadFailed(Throwable t) {
            LOG.error("Loading of IpToCountry.cvs failed", t);
        }

        @Override
        public void lineError(int errorIndex, String line) {
            if ( errorIndex == 0 ) {
                LOG.error("IpToCountry: File line not understood (displaying " + "first occurrence only): " + line);
            }
        }

        @Override
        public void fileLoaded(int entries, int milliseconds, int lineErrors) {
            LOG.info("IpToCountry: Loaded data file (" + entries + " entries) in " + milliseconds + " ms; " + lineErrors + " lines were not understood");
        }
    }

    private class UpdateTask implements Runnable {

        @Override
        public void run() {
            long modified = file.lastModified();
            if ( fileLastModified < modified ) {
                // although loading might fail, next attempt should not be before normal delay of the scheduler
                fileLastModified = modified;
                try {
                    loadFile(file);
                } catch ( IOException e ) {
                    LOG.error("IpToCountry: Loaded data file failed: " + e);
                }
            } else {
                Date date = new Date(fileLastModified);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
                formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
                LOG.info("IpToCountry: no update available, db last modified " + formatter.format(date));
            }
        }
    }

    /**
     * Initialises the database. The database will be loaded from a stored file in
     * the given folder. If there is no database present the server will not start.
     * <p>
     * This constructor returns immediately after loading the file from disk (if
     * present); any update will happen in another thread.
     * 
     * @param folder Folder for database storage (must be writable)
     * @throws IOException If there is any problem loading the file
     */
    @Inject
    public IpToCountry(String folder) throws IOException {
        this.file = new File(folder, FILE_NAME);
        this.informer = new DefaultInformer();
        this.fileLastModified = file.lastModified();
        if ( file.exists() && file.canRead() ) {
            loadFile(file);
        } else {
            throw new DbcException("IpToCountry.cs could not be found or is not readable. Server does NOT start");
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // use this for testing, check for update will happen every 30 sec
        // scheduler.scheduleAtFixedRate(new UpdateTask(), TimeUnit.SECONDS.toSeconds(30), TimeUnit.SECONDS.toSeconds(30), TimeUnit.SECONDS);
        // use this apart from that
        scheduler
            .scheduleAtFixedRate(new UpdateTask(), TimeUnit.DAYS.toDays(CHECK_FOR_UPDATE_DAYS), TimeUnit.DAYS.toDays(CHECK_FOR_UPDATE_DAYS), TimeUnit.DAYS);
    }

    /**
     * Gets the country code for an IP address.
     * <p>
     * Based on the list documentation, this is the ISO 3166 2-letter code of the
     * organisation to which the allocation or assignment was made, with the
     * following differences or unusual cases:
     * <ul>
     * <li>AP - non-specific Asia-Pacific location</li>
     * <li>CS - Serbia and Montenegro</li>
     * <li>YU - Serbia and Montenegro (Formally Yugoslavia) (Being phased out)</li>
     * <li>EU - non-specific European Union location</li>
     * <li>FX - France, Metropolitan</li>
     * <li>PS - Palestinian Territory, Occupied</li>
     * <li>UK - United Kingdom (standard says GB)</li>
     * <li>ZZ - IETF RESERVED address space.</li>
     * <li>.. - Unmatched address (specific to this system, not in list)</li>
     * </ul>
     * 
     * @param address Internet address
     * @return Country code
     * @throws IllegalArgumentException If the address is an IPv6 address that can't
     *         be expressed in 4 bytes, or something else
     * @throws IOException
     */
    @Override
    public String getCountryCode(InetAddress address) throws IOException {
        long addressLong = getAddressAsLong(get4ByteAddress(address));
        return getCountryCode(addressLong);
    }

    /**
     * Get a country code given the arrays which hold the details.
     * <p>
     * 
     * @param addressLong Address as long
     * @return Corresponding country code, or .. if not known
     */
    public String getCountryCode(long addressLong) {
        // Binary search for the highest entryFrom that's less than or equal to
        // the specified address.
        DBCountry dbCountry = this.atomicDBCountry.get();
        int min = 0, max = dbCountry.entryFrom.length == 0 ? 0 : dbCountry.entryFrom.length - 1;
        while ( min != max ) {
            int half = (min + max) / 2;
            if ( half == min ) {
                // This special case handles the situation where e.g. min=10, max=11;
                // there half=10 and we could get an endless loop.
                if ( dbCountry.entryFrom[max] <= addressLong ) {
                    min = max;
                } else {
                    max = min;
                }
            } else {
                // Standard case; check whether the halfway position is bigger or
                // smaller
                if ( dbCountry.entryFrom[half] <= addressLong ) {
                    min = half;
                } else {
                    max = half - 1;
                }
            }
        }

        if ( min >= dbCountry.entryFrom.length || dbCountry.entryTo[min] < addressLong || dbCountry.entryFrom[min] > addressLong ) {
            return "..";
        } else {
            return dbCountry.entryCode[min];
        }
    }

    /**
     * Given an internet address in network byte order, converts it into an unsigned
     * long.
     * <p>
     * This is a separate method with default access so it can be used in unit
     * testing.
     * 
     * @param bytes Bytes
     * @return Long value
     * @throws IllegalArgumentException If there aren't 4 bytes of address
     */
    static long getAddressAsLong(byte[] bytes) throws IllegalArgumentException {
        if ( bytes.length != 4 ) {
            throw new IllegalArgumentException("Input must be 4 bytes");
        }
        int i0 = (int) bytes[0] & 0xff, i1 = (int) bytes[1] & 0xff, i2 = (int) bytes[2] & 0xff, i3 = (int) bytes[3] & 0xff;
        return (((long) i0) << 24) | (((long) i1) << 16) | (((long) i2) << 8) | ((long) i3);
    }

    /**
     * Converts an {@link InetAddress} into a 4-byte address.
     * <p>
     * This is a separate method with default access so it can be used in unit
     * testing.
     * 
     * @param address Address
     * @return 4 bytes in network byte order
     * @throws IllegalArgumentException If the address is an IPv6 address that can't
     *         be expressed in 4 bytes, or something else
     */
    static byte[] get4ByteAddress(InetAddress address) throws IllegalArgumentException {
        byte[] actual = address.getAddress();
        if ( actual.length == 4 ) {
            return actual;
        }

        if ( address instanceof Inet6Address ) {
            if ( ((Inet6Address) address).isIPv4CompatibleAddress() ) {
                // For compatible addresses, use last 4 bytes
                byte[] bytes = new byte[4];
                System.arraycopy(actual, actual.length - 4, bytes, 0, 4);
                return bytes;
            } else {
                throw new IllegalArgumentException("IPv6 addresses not supported " + "unless IP4 compatible): " + address.getHostAddress());
            }
        }

        throw new IllegalArgumentException("Unknown address type: " + address.getHostAddress());
    }

    /**
     * Loads the existing file from disk into memory.
     * <p>
     * This is a separate method with default access so it can be used in unit
     * testing.
     * 
     * @param file File to load
     * @return
     * @throws IOException Any problem loading file
     */
    void loadFile(File file) throws IOException {

        BufferedReader reader = null;
        try {
            // String-sharing buffer
            HashMap<String, String> countryCodes = new HashMap<String, String>();

            // Get start time
            long start = getCurrentTime();

            // Prepare new index buffers
            long[] newEntryFrom = new long[INITIAL_ENTRIES];
            long[] newEntryTo = new long[INITIAL_ENTRIES];
            String[] newEntryCode = new String[INITIAL_ENTRIES];
            int entries = 0;

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "US-ASCII"));
            int lineErrors = 0;
            while ( true ) {
                // Read next line
                String line = reader.readLine();
                if ( line == null ) {
                    // No more lines
                    break;
                }
                // Skip comments (lines beginning # or whitespace) and empty lines
                if ( line.isEmpty() || line.startsWith("#") || Character.isWhitespace(line.charAt(0)) ) {
                    continue;
                }

                // Match line
                Matcher m = CSV_LINE.matcher(line);
                if ( !m.find() ) {
                    // Non-matching line; report as warning, the first time
                    informer.lineError(lineErrors++, line);
                    continue;
                }

                // Line matches!

                // Expand entry arrays if required
                if ( entries == newEntryFrom.length ) {
                    long[] temp = new long[entries * 2];
                    System.arraycopy(newEntryFrom, 0, temp, 0, entries);
                    newEntryFrom = temp;
                    temp = new long[entries * 2];
                    System.arraycopy(newEntryTo, 0, temp, 0, entries);
                    newEntryTo = temp;
                    String[] tempStr = new String[entries * 2];
                    System.arraycopy(newEntryCode, 0, tempStr, 0, entries);
                    newEntryCode = tempStr;
                }

                // Share country strings, as they are likely to be repeated many times
                String code = m.group(3);
                if ( countryCodes.containsKey(code) ) {
                    code = countryCodes.get(code);
                } else {
                    countryCodes.put(code, code);
                }

                // Store new entry
                newEntryFrom[entries] = Long.parseLong(m.group(1));
                newEntryTo[entries] = Long.parseLong(m.group(2));
                newEntryCode[entries] = code;
                entries++;
            }

            // Reallocate arrays to precise length
            long[] temp = new long[entries];
            System.arraycopy(newEntryFrom, 0, temp, 0, entries);
            newEntryFrom = temp;
            temp = new long[entries];
            System.arraycopy(newEntryTo, 0, temp, 0, entries);
            newEntryTo = temp;
            String[] tempStr = new String[entries];
            System.arraycopy(newEntryCode, 0, tempStr, 0, entries);
            newEntryCode = tempStr;

            DBCountry newDBCountry = new DBCountry(newEntryFrom, newEntryTo, newEntryCode);
            this.atomicDBCountry.set(newDBCountry);
            // Tell informer
            informer.fileLoaded(entries, (int) (getCurrentTime() - start), lineErrors);
        } finally {
            if ( reader != null ) {
                reader.close();
            }
        }
    }

    /**
     * Obtains current time.
     * <p>
     * This is a separate method with default access so it can be used in unit
     * testing.
     * 
     * @return Current time in milliseconds
     */
    long getCurrentTime() {
        return System.currentTimeMillis();
    }
}