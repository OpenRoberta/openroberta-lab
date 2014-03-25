package de.fraunhofer.roberta;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnector
{

	public ServerConnector()
	{
		//
	}

  public byte[] downloadProgram(URL serverUrl)
  throws IOException, ClassNotFoundException
  {
    return requestToServer(openConnection(serverUrl));
  }

  private HttpURLConnection openConnection(URL url)
  throws IOException, ClassNotFoundException
  {
    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    httpURLConnection.setDoOutput(true);
    httpURLConnection.setRequestMethod("GET");
    return httpURLConnection;
  }
  
  private byte[] requestToServer(HttpURLConnection httpURLConnection)
  throws IOException, ClassNotFoundException
  {
    InputStream is = httpURLConnection.getInputStream();
    ObjectInputStream ois = new ObjectInputStream(is);
    byte[] ServletAnswer = (byte[]) ois.readObject();
    ois.close();
    is.close();
    return ServletAnswer;
  }
}
