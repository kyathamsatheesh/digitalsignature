package com.digatalsign.rest.exception;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import com.digatalsign.rest.constants.KeyConstants;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;

public class Test {
	
	
	public static void main(String[] args) throws DocumentException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
		PdfReader reader = new
				PdfReader((new File(KeyConstants.INPUTFOLDER+"sample_2PDF.pdf")).getAbsolutePath());

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				reader = new PdfReader(reader);
				// work with new revision each time 
				PdfStamper stp1 = new PdfStamper(reader, out, '\3', 
				true);

				PdfFormField sig = 
				PdfFormField.createSignature(stp1.getWriter());
				sig.setWidget(new Rectangle(300, 400, 400, 500), null);
				sig.setFlags(PdfAnnotation.FLAGS_PRINT);
				sig.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g"));
				sig.setFieldName("Signature4");
				sig.setPage(1);

				// Attached the blank signature field to the existing document
				stp1.addAnnotation(sig, 1);
				stp1.addAnnotation(sig, 2);

				stp1.close();
				out.close();  
				 
				FileOutputStream fout = new FileOutputStream
				("D:\\PDF\\Signed\\test.pdf");
				  
				reader = new PdfReader(out.toByteArray());  

				//  Fill the new signature fields in the correct pdf  revision 
				PdfStamper  stp = PdfStamper.createSignature(reader, fout, 
				'\3', null,
				true);
				PdfSignatureAppearance sap = 
				stp.getSignatureAppearance();  
				
				String filePath ="";
				//String filePasswrd="";
				
				KeyStore ks = KeyStore.getInstance("pkcs12");
				filePath=KeyConstants.COMMON_PFX_PATH+"HGINFRA/HGINFRA_Kailash.pfx";
				ks.load(new FileInputStream(filePath), "1234".toCharArray());
				String alias = (String)ks.aliases().nextElement();
				PrivateKey key = (PrivateKey)ks.getKey(alias, "1234".toCharArray());
				Certificate[] chain = ks.getCertificateChain(alias);
				Rectangle rectangle = null;
				float width = 148;
			    float height = 62;
				Rectangle cropBox = reader.getCropBox(1);
				
				rectangle = new Rectangle(cropBox.getLeft(), cropBox.getBottom(),
                        cropBox.getLeft(width), cropBox.getBottom(height));
				sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
				//sap.setLayer2Text("Texto"); 
				//sap.setAcro6Layers(true);
				// Set visible signature field 
				sap.setVisibleSignature("Signature4");
				 
				// Close PdfStamper and output
				stp.close();
				fout.close();  
	}

}
