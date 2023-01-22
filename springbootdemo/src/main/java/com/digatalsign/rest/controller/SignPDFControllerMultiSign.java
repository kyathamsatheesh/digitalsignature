/**
 * 
 */
package com.digatalsign.rest.controller;

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

import org.springframework.stereotype.Component;

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

/**
 * @author sathish.kyatham
 *
 */
@Component
public class SignPDFControllerMultiSign {

	static SignedDetails sign = new SignedDetails();
	public static final SignedDetails signPdfMultiSign(String inputFile,String fileName,String pfxFilepath,String pfxFilePassword,float coordinatesX,float coordinatesY,float coordinatesW,float coordinatesH) throws DocumentException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException
			
	{
		String fnameS = KeyConstants.OUTPUTFOLDER+fileName+"_sign.pdf" ;
		String filePath =KeyConstants.COMMON_PFX_PATH+pfxFilepath;
		
		PdfReader reader;
		try {
//			reader = new
//					PdfReader((new File(KeyConstants.INPUTFOLDER+"sample_2PDF.pdf")).getAbsolutePath());//inputFile
			
			reader = new
					PdfReader((new File(inputFile)).getAbsolutePath());
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			reader = new PdfReader(reader);
			// work with new revision each time 
			PdfStamper stp1 = new PdfStamper(reader, out, '\0', 
			true);
			PdfFormField sig = 
					PdfFormField.createSignature(stp1.getWriter());
					Rectangle rect = new Rectangle(coordinatesX, coordinatesY, coordinatesW, coordinatesH);
					sig.setWidget(new Rectangle(rect), null);
					//sig.setWidget(new Rectangle(300, 400, 400, 500), null);
					sig.setFlags(PdfAnnotation.FLAGS_PRINT);
					sig.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g"));
					sig.setFieldName("Signature4");
					sig.setPage(1);
					// Attached the blank signature field to the existing document
					int pagecount = reader.getNumberOfPages();
					for(int i=1;i<=pagecount;i++)
					{
						stp1.addAnnotation(sig, i);
					}
					//stp1.addAnnotation(sig, 2);

					stp1.close();
					out.close();  
					 
					FileOutputStream fout = new FileOutputStream
					(fnameS);
					  
					reader = new PdfReader(out.toByteArray());  

					//  Fill the new signature fields in the correct pdf  revision 
					PdfStamper  stp = PdfStamper.createSignature(reader, fout, 
					'\3', null,
					true);
					PdfSignatureAppearance sap = 
					stp.getSignatureAppearance();  
					
					KeyStore ks = KeyStore.getInstance("pkcs12");
					//filePath=KeyConstants.COMMON_PFX_PATH+"HGINFRA/HGINFRA_Kailash.pfx";
					ks.load(new FileInputStream(filePath), pfxFilePassword.toCharArray());
					String alias = (String)ks.aliases().nextElement();
					PrivateKey key = (PrivateKey)ks.getKey(alias, pfxFilePassword.toCharArray());
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
					
					sign.setFilename(fnameS);
					sign.setErrorMessage("Success");
					return sign;
		} catch (Exception e) {
			sign.setErrorMessage(e.toString());
			return sign;
		}
	}
}
