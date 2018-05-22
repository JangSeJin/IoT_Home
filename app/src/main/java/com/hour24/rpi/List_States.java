package com.hour24.rpi;

public class List_States {

	String name;
	String NickName;
	String state;
	boolean turn;
	boolean auto;

	public List_States(String name, String nickName, String state, boolean turn, boolean auto) {
		super();
		this.name = name;
		NickName = nickName;
		this.state = state;
		this.turn = turn;
		this.auto = auto;
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

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

}