package com.ubtech.utilcode.utils.network.http;

public class InvalidHttpURIException extends IllegalArgumentException
{

	/**
	 * 序列版本ID
	 */
	private static final long	serialVersionUID	= -5766058678007111790L;

	public InvalidHttpURIException()
	{
		// TODO Auto-generated constructor stub
	}

	public InvalidHttpURIException(String detailMessage)
	{
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public InvalidHttpURIException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidHttpURIException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
