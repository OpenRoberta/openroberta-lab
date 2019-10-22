package de.fhg.iais.roberta.main;

import java.io.IOException;
import java.net.InetAddress;

public interface IIpToCountry {

    public String getCountryCode(InetAddress address) throws IOException;

}
