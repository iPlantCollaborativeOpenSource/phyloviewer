package org.iplantc.phyloviewer.shared;

import java.io.Serializable;

/**
 * Models a portion of multi-part request submission.
 */
public class HTTPPart implements Serializable
{
	private static final long serialVersionUID = -2662589032061446564L;
	private String body = new String();
	private String disposition = new String();

	public HTTPPart()
	{

	}

	public HTTPPart(String body, String disposition)
	{
		this.body = body;
		this.disposition = disposition;
	}

	public String getBody()
	{
		return body;
	}

	public String getDisposition()
	{
		return disposition;
	}
}