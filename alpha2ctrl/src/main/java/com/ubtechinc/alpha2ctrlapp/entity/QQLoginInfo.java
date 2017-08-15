package com.ubtechinc.alpha2ctrlapp.entity;

public class QQLoginInfo {

	public int ret;
	public String openid;
	public String access_token;
	public String pay_token;
	public int expires_in;
	public String pf;
	public String pfkey;
	public String msg;
	public int login_cost;
	public String query_authority_cost;
	public int authority_cost;
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getPay_token() {
		return pay_token;
	}
	public void setPay_token(String pay_token) {
		this.pay_token = pay_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getPfkey() {
		return pfkey;
	}
	public void setPfkey(String pfkey) {
		this.pfkey = pfkey;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getLogin_cost() {
		return login_cost;
	}
	public void setLogin_cost(int login_cost) {
		this.login_cost = login_cost;
	}
	public String getQuery_authority_cost() {
		return query_authority_cost;
	}
	public void setQuery_authority_cost(String query_authority_cost) {
		this.query_authority_cost = query_authority_cost;
	}
	public int getAuthority_cost() {
		return authority_cost;
	}
	public void setAuthority_cost(int authority_cost) {
		this.authority_cost = authority_cost;
	}
	

}
