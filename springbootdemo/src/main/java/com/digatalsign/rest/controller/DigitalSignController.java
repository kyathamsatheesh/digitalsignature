/**
 * 
 */
package com.digatalsign.rest.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digatalsign.rest.constants.KeyConstants;
import com.lowagie.text.DocumentException;

import sun.misc.BASE64Decoder;

//import com.digitalsign.model.InputData;

/**
 * @author sathish.kyatham
 *
 */
@RestController
public class DigitalSignController {

	@Autowired
	SignPDFController signPDF;
	
	@Autowired
	SignPDFControllerMultiSign signPDFMultiSign;
	
	@GetMapping(value = "/getsignedPDF")
	public String getEOIPaymentRecordTest(@RequestParam("applicationBookingId") String applicationBookingId) {
		System.out.println("*****");
		return "";
		
	}
	
	/*@PostMapping(value = "/postsignedPDFT", consumes = "application/json" , produces = "application/json")
	public InputData getEOIPaymentRecordT(@RequestBody InputData inputBase64) {
		//Base64 to PDF
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decodedBytes;
		FileOutputStream fop = null;
		try {
			decodedBytes = decoder.decodeBuffer(inputBase64.getInputBase64());
			File file = new File(KeyConstants.INPUTFOLDER+inputBase64.getFileName()+".pdf");
			fop = new FileOutputStream(file);
			fop.write(decodedBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(fop!=null)
				{
					fop.flush();
					fop.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		String b64 = null;
		InputData finalData = new InputData();
		try {
			//PDF to Signin
			String signedFilePath = signPDF.signPdf(KeyConstants.INPUTFOLDER+inputBase64.getFileName()+".pdf",inputBase64.getFileName(),inputBase64.getPfxFilePath(),inputBase64.getSignPosition(),inputBase64.getPfxFilePassword(),inputBase64.getCoordinatesX(),inputBase64.getCoordinatesY(),inputBase64.getCoordinatesW(),inputBase64.getCoordinatesH());
			//Returnbase64
			System.out.println(signedFilePath);
			File file = new File(signedFilePath);
		      byte [] bytes = Files.readAllBytes(file.toPath());

		      b64 = Base64.getEncoder().encodeToString(bytes);
		      System.out.println(b64);
		      
		      finalData.setInputBase64(b64);
		      finalData.setFileName(inputBase64.getFileName()+"_sign.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return b64;
	
	return finalData;	
	}*/
	
	@PostMapping(value = "/postsignedPDF", consumes = "application/json" , produces = "application/json")
	public List<InputData> getEOIPaymentRecord(@RequestBody List<InputData> inputBase64) {
		
		System.out.println("Size"+inputBase64.size());
		
		List<InputData> finalretunVal= new ArrayList<InputData>();
		for(int i=0;i<inputBase64.size();i++)
		{
			
		//Base64 to PDF
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] decodedBytes;
		FileOutputStream fop = null;
		try {
			decodedBytes = decoder.decodeBuffer(inputBase64.get(i).getInputBase64());
			File file = new File(KeyConstants.INPUTFOLDER+inputBase64.get(i).getFileName()+".pdf");
			fop = new FileOutputStream(file);
			fop.write(decodedBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(fop!=null)
				{
					fop.flush();
					fop.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		String b64 = null;
		InputData finalData = new InputData();
		try {
			//PDF to Signin
			SignedDetails signedFilePath = null;
			if(!inputBase64.get(i).isIsmultiSign())
				signedFilePath = signPDF.signPdf(KeyConstants.INPUTFOLDER+inputBase64.get(i).getFileName()+".pdf",inputBase64.get(i).getFileName(),inputBase64.get(i).getPfxFilePath(),inputBase64.get(i).getSignPosition(),inputBase64.get(i).getPfxFilePassword(),inputBase64.get(i).getCoordinatesX(),inputBase64.get(i).getCoordinatesY(),inputBase64.get(i).getCoordinatesW(),inputBase64.get(i).getCoordinatesH());
			else
				signedFilePath= signPDFMultiSign.signPdfMultiSign(KeyConstants.INPUTFOLDER+inputBase64.get(i).getFileName()+".pdf",inputBase64.get(i).getFileName(),inputBase64.get(i).getPfxFilePath(),inputBase64.get(i).getPfxFilePassword(),inputBase64.get(i).getCoordinatesX(),inputBase64.get(i).getCoordinatesY(),inputBase64.get(i).getCoordinatesW(),inputBase64.get(i).getCoordinatesH()); 
			//Returnbase64
			System.out.println(signedFilePath);
			if(signedFilePath.getErrorMessage().equals("Success"))
			{
			File file = new File(signedFilePath.getFilename());
		      byte [] bytes = Files.readAllBytes(file.toPath());

		      b64 = Base64.getEncoder().encodeToString(bytes);
		      System.out.println(b64);
		      
		      finalData.setInputBase64(b64);
		      finalData.setFileName(inputBase64.get(i).getFileName()+"_sign.pdf");
		      finalData.setMessage(signedFilePath.getErrorMessage());
		      finalData.setIsmultiSign(inputBase64.get(i).isIsmultiSign());
		      finalretunVal.add(finalData);
			}
			else
			{
				  finalData.setInputBase64("");
			      finalData.setFileName("");
			      finalData.setMessage(signedFilePath.getErrorMessage());
			      finalretunVal.add(finalData);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return b64;
		}
		
	System.out.println("Final Size"+finalretunVal);
	return finalretunVal;	
	}
}
