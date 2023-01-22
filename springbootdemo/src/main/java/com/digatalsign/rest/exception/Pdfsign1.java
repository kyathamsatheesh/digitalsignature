package com.digatalsign.rest.exception;

import com.lowagie.text.pdf.*;
import com.lowagie.text.Rectangle;
import java.security.*;
import java.io.*;
import java.awt.*;
import java.lang.*;
import javax.net.ssl.*;
import javax.net.ssl.KeyManager;
import javax.naming.*;
import javax.naming.Context;
 
public class Pdfsign1{
  public static void main(String args[]) throws IOException{
try {
//KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); 
//KeyStore ks = KeyStore.getInstance("pkcs12");
//ks.load(new FileInputStream("D:\\pdf\\pdf2\\digital\\my_private_key.pfx"), "my_password".toCharArray());
//kmf.init(ks, "my_password".toCharArray());
 
SSLContext ctx=SSLContext.getInstance("SSL");
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); 
KeyStore ks = KeyStore.getInstance("pkcs12");
ks.load(new FileInputStream("D:\\PDF\\PFX\\HGINFRA\\HGINFRA_Kailash.pfx"), "1234".toCharArray());
kmf.init(ks, "1234".toCharArray());
ctx.init(kmf.getKeyManagers(),null,null);
 
//initSSLContext(kmf.getKeyManagers());
//KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//ks.load(new FileInputStream("keystore.ks"), "my_password".toCharArray());
String alias = (String)ks.aliases().nextElement();
PrivateKey key = (PrivateKey)ks.getKey(alias,"1234".toCharArray());
java.security.cert.Certificate[] chain = ks.getCertificateChain(alias);
PdfReader reader = new PdfReader("D:\\PDF\\sample_2PDF.pdf");
int pagecount = reader.getNumberOfPages();
FileOutputStream fout = new FileOutputStream("D:\\PDF\\Signed\\sample_2PDF_signed.pdf");
PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', null, true);
for(int i=1;i<=pagecount;i++)
{

PdfSignatureAppearance sap = stp.getSignatureAppearance();
//sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
sap.setReason("I'm the author");
sap.setLocation("Lisbon");
// comment next line to have an invisible signature
sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), i, "sig");
}
stp.close();
  }
catch(Exception e) {e.printStackTrace();}
}
}
