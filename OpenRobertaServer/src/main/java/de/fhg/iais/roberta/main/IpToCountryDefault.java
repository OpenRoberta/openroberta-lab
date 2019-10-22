package de.fhg.iais.roberta.main;

import java.net.InetAddress;

public class IpToCountryDefault implements IIpToCountry {

    @Override
    public String getCountryCode(InetAddress address) {
        return "ZZ";
    }

}
