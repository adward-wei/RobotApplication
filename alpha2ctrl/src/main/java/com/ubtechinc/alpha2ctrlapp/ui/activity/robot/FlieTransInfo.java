package com.ubtechinc.alpha2ctrlapp.ui.activity.robot;

import java.io.Serializable;

public class FlieTransInfo implements Serializable {

	private static final long serialVersionUID = 3321422229192586065L;
	private String fileName;
	private Double  fileProgress ;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Double getFileProgress() {
		return fileProgress;
	}
	public void setFileProgress(Double fileProgress) {
		this.fileProgress = fileProgress;
	}

}
