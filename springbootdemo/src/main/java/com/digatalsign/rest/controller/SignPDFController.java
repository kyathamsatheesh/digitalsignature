/**
 * 
 */
package com.digatalsign.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import org.springframework.stereotype.Component;

import com.digatalsign.rest.constants.KeyConstants;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * @author sathish.kyatham
 *
 */
@Component
public class SignPDFController {

//	static String fnameS = "D:\\PDF\\signtest_sign.pdf" ;
	
	static SignedDetails sign = new SignedDetails();
	public static final SignedDetails signPdf(String inputFile,String fileName,String pfxFilepath,String signPosition,String pfxFilePassword,float coordinatesX,float coordinatesY,float coordinatesW,float coordinatesH)
			throws IOException, DocumentException, Exception
	{
		String fnameS = KeyConstants.OUTPUTFOLDER+fileName+"_sign.pdf" ;
		// Vous devez preciser ici le chemin d'acces a votre clef pkcs12
		
		/*String fileKey          = "D:/ssl.pfx" ;
		// et ici sa "passPhrase"
		String fileKeyPassword  = "90035366" ;*/
 
		try {
			// Creation d'un KeyStore
			KeyStore ks = KeyStore.getInstance("pkcs12");
//			KeyStore kall = PdfPKCS7.loadCacertsKeyStore();
			// Chargement du certificat p12 dans el magasin
			
			String filePath ="";
			//String filePasswrd="";
			
			/*if(companyCode.equals("1000"))
			{
				filePath=KeyConstants.ALLIANCE_PFX_FILE_PATH;
				filePasswrd = KeyConstants.ALLIANCE_PFX_PASSWRD;
			}
			else if(companyCode.equals("3000"))
			{
				filePath=KeyConstants.SIDHARTH_PFX_FILE_PATH;
				filePasswrd = KeyConstants.SIDHARTH_PFX_PASSWRD;
			}
			else if(companyCode.equals("2000"))
			{
				filePath=KeyConstants.STAR_PFX_FILE_PATH;
				filePasswrd = KeyConstants.STAR_PFX_PASSWRD;
			}*/
			
			filePath=KeyConstants.COMMON_PFX_PATH+pfxFilepath;
			//filePasswrd = pfxFilePassword;
			
			ks.load(new FileInputStream(filePath), pfxFilePassword.toCharArray());
			String alias = (String)ks.aliases().nextElement();
			PrivateKey key = (PrivateKey)ks.getKey(alias, pfxFilePassword.toCharArray());
			
			
			Certificate[] chain = ks.getCertificateChain(alias);
			// Lecture du document source
//			PdfReader pdfReader = new PdfReader((new File(fname)).getAbsolutePath());
			PdfReader pdfReader = new PdfReader((new File(inputFile)).getAbsolutePath());
			int pagecount = pdfReader.getNumberOfPages();
			File outputFile = new File(fnameS);
			// Creation du tampon de signature
			PdfStamper pdfStamper = PdfStamper.createSignature(pdfReader, null, '\0', outputFile);
			PdfSignatureAppearance sap = pdfStamper.getSignatureAppearance();
			//sap.setAcro6Layers(true);
			sap.setCrypto(key, chain, null, PdfSignatureAppearance.SELF_SIGNED);
			//sap.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_FORM_FILLING);
			/*sap.setReason("SignPDF");
			sap.setLocation("Jaipur");*/
			Object fails[] = PdfPKCS7.verifyCertificates(chain, ks, null, null);
			
			if (fails == null)
			       System.out.println("Certificates verified against the KeyStore");
			   else
			       System.out.println("Certificate failed: " + fails[1]);
			for(int i = 0;i<fails.length;i++)
			{

			System.out.println(fails[i]);
			}
//			float width = 108;
//		    float height = 32;
			
			float width = 148;
		    float height = 62;
			Rectangle cropBox = pdfReader.getCropBox(1);
			Rectangle rectangle = null;
			if(signPosition.equals("TL"))
			{
				// Top left
			    rectangle = new Rectangle(cropBox.getLeft(), cropBox.getTop(height),
			                              cropBox.getLeft(width), cropBox.getTop());
			}
			else if(signPosition.equals("TR"))
			{
				// Top right
			    rectangle = new Rectangle(cropBox.getRight(width), cropBox.getTop(height),
			                              cropBox.getRight(), cropBox.getTop());
			}
			else if(signPosition.equals("BL"))
			{
				// Bottom left
			    rectangle = new Rectangle(cropBox.getLeft(), cropBox.getBottom(),
		                              cropBox.getLeft(width), cropBox.getBottom(height));
			}
			else if(signPosition.equals("BR"))
			{
				// Bottom right
				rectangle = new Rectangle(cropBox.getRight(width), cropBox.getBottom(),
	                    cropBox.getRight(), cropBox.getBottom(height));
			}
			 
			//sap.setVisibleSignature(new Rectangle(36, 748, 144, 780), pagecount, null);
//			sap.setVisibleSignature(rectangle, pagecount, "sig");
			//sap.setVisibleSignature(new Rectangle(420, 732, 512, 780), pagecount, null);
			
			Rectangle rect = new Rectangle(coordinatesX, coordinatesY, coordinatesW, coordinatesH);
			sap.setVisibleSignature(rect, pagecount, null);
			
			// trigger creation of default layers contents
			sap.getAppearance();

			// Customize the layer contents
			PdfTemplate layer2 = sap.getLayer(2);
			Rectangle rectBoarder = sap.getRect();
			layer2.setRGBColorStroke(0, 0, 0);
			layer2.setLineWidth(2);
			layer2.rectangle(rectBoarder.getLeft(), rectBoarder.getBottom(), rectBoarder.getWidth(), rectBoarder.getHeight());
			layer2.stroke();
			
			Rectangle tmpRect = pdfReader.getPageSizeWithRotation(1);
			System.out.println(tmpRect);
			pdfStamper.setFormFlattening(true);
			pdfStamper.close();
			
			sign.setFilename(fnameS);
			sign.setErrorMessage("Success");
			return sign;
		}
		catch (Exception key) {
			sign.setErrorMessage(key.toString());
			return sign;
		}
		finally {
			
		}
	}
}
