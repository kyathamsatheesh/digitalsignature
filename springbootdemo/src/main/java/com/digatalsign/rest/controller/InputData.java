/**
 * 
 */
package com.digatalsign.rest.controller;

/**
 * @author sathish.kyatham
 *
 */
public class InputData {
	
	String inputBase64;
	String fileName;
	String companyCode;
	String signPosition;
	String pfxFilePath;
	String pfxFilePassword;
	float coordinatesX;
	float coordinatesY;
	float coordinatesW;
	float coordinatesH;
	String message;
	boolean ismultiSign;
	public String getInputBase64() {
		return inputBase64;
	}
	public void setInputBase64(String inputBase64) {
		this.inputBase64 = inputBase64;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getSignPosition() {
		return signPosition;
	}
	public void setSignPosition(String signPosition) {
		this.signPosition = signPosition;
	}
	public String getPfxFilePath() {
		return pfxFilePath;
	}
	public void setPfxFilePath(String pfxFilePath) {
		this.pfxFilePath = pfxFilePath;
	}
	public String getPfxFilePassword() {
		return pfxFilePassword;
	}
	public void setPfxFilePassword(String pfxFilePassword) {
		this.pfxFilePassword = pfxFilePassword;
	}
	public float getCoordinatesX() {
		return coordinatesX;
	}
	public void setCoordinatesX(float coordinatesX) {
		this.coordinatesX = coordinatesX;
	}
	public float getCoordinatesY() {
		return coordinatesY;
	}
	public void setCoordinatesY(float coordinatesY) {
		this.coordinatesY = coordinatesY;
	}
	public float getCoordinatesW() {
		return coordinatesW;
	}
	public void setCoordinatesW(float coordinatesW) {
		this.coordinatesW = coordinatesW;
	}
	public float getCoordinatesH() {
		return coordinatesH;
	}
	public void setCoordinatesH(float coordinatesH) {
		this.coordinatesH = coordinatesH;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isIsmultiSign() {
		return ismultiSign;
	}
	public void setIsmultiSign(boolean ismultiSign) {
		this.ismultiSign = ismultiSign;
	}
	
	

}
