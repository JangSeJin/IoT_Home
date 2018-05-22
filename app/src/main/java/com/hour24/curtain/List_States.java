package com.hour24.curtain;

public class List_States {

	String name;
	int turn;
	String NickName;

	public List_States(String name, int turn, String NickName) {
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

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}
	
	

}