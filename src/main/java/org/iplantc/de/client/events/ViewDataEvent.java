package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event represents a user viewing data. 
 */
public class ViewDataEvent extends GwtEvent<ViewDataEventHandler>
{
	// ////////////////////////////////////////
	// types
	public static final GwtEvent.Type<ViewDataEventHandler> TYPE = new GwtEvent.Type<ViewDataEventHandler>();

	/**
	 * Defines is the view is of raw data or the phylogenetic tree representation. 
	 */
	public static enum ViewType
	{
		Raw, Tree
	}

	// ////////////////////////////////////////
	// private variables
	private String name;
	private ViewType type;

	// ////////////////////////////////////////
	// constructor
	public ViewDataEvent(String name, ViewType type)
	{
		this.name = name;
		this.type = type;
	}

	@Override
	protected void dispatch(ViewDataEventHandler handler)
	{
		handler.onView(this);
	}

	// ////////////////////////////////////////
	// public methods
	@Override
	public Type<ViewDataEventHandler> getAssociatedType()
	{
		return TYPE;
	}

	// ////////////////////////////////////////
	public String getName()
	{
		return name;
	}

	// ////////////////////////////////////////
	public ViewType getViewType()
	{
		return type;
	}
}
