package com.hour24.light;

public class List_States {

	String name;
	boolean turn;
	String NickName;

	public List_States(String name, boolean turn, String NickName) {
		super();
		this.name = name;
		this.turn = turn;
		this.NickName = NickName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

}