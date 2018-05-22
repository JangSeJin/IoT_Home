package com.hour24.oneclick;

public class List_States {

	boolean flag;
	String name;
	String jsonArray;

	public List_States(boolean flag, String name, String jsonArray) {
		super();
		this.flag = flag;
		this.name = name;
		this.jsonArray = jsonArray;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getjsonArray() {
		return jsonArray;
	}

	public void setjsonArray(String jsonArray) {
		this.jsonArray = jsonArray;
	}

}