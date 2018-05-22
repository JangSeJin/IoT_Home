package com.hour24.grid;

public class Grid_States {

	int img;
	String device;
	String state;

	public Grid_States(int img, String device, String state) {
		super();
		this.img = img;
		this.device = device;
		this.state = state;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}