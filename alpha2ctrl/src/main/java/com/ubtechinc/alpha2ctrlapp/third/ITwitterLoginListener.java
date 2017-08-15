package com.ubtechinc.alpha2ctrlapp.third;

import twitter4j.auth.AccessToken;

public interface ITwitterLoginListener {
	public void OnTwitterLoginComplete(AccessToken accessToken);

}
