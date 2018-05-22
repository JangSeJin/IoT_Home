package com.hour24.security;

public class List_States {

	String name;
	String NickName;
	String state;
	boolean flag;

	public List_States(String name, String nickName, String state, boolean flag) {
		super();
		this.name = name;
		this.state = state;
		this.NickName = nickName;
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}