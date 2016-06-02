package in.egan.pay.wx.bean;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyX509TrustManager implements  X509TrustManager{

public X509Certificate[] getAcceptedIssuers() {
// TODO Auto-generated method stub
return null;
}

public boolean isClientTrusted(X509Certificate[] arg0) {
// TODO Auto-generated method stub
return true;
}

public boolean isServerTrusted(X509Certificate[] arg0) {
// TODO Auto-generated method stub
return true;
}

@Override
public void checkClientTrusted(X509Certificate[] arg0, String arg1)
		throws CertificateException {
	// TODO Auto-generated method stub
	
}

@Override
public void checkServerTrusted(X509Certificate[] arg0, String arg1)
		throws CertificateException {
	// TODO Auto-generated method stub
	
}

}