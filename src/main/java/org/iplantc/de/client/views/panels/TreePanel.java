package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.models.FileIdentifier;
import org.iplantc.de.client.tree.viewer.TreeWidget;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.user.client.Element;

public class TreePanel extends ProvenanceContentPanel 
{
	///////////////////////////////////////
	//protected variables
	protected String json;
	protected VerticalPanel panel = new VerticalPanel();
	
	///////////////////////////////////////
	//constructor
	public TreePanel(FileIdentifier file,String json) 
	{
		super(file);
		this.json = json;
	}

	///////////////////////////////////////
	@Override
	protected void onRender(Element parent,int index) 
	{  
		super.onRender(parent,index);
		
		panel.setSpacing(5);
		panel.setScrollMode(Scroll.AUTO);
		panel.setWidth("100%");		
		panel.setStyleAttribute("background-color","white");
		
		TreeWidget widget = new TreeWidget();
		
		widget.loadFromJSON(json);
		
		panel.add(widget);
		
		widget.requestRender();
		
		add(panel,centerData);	
	}
	
	///////////////////////////////////////
	//public methods
	@Override
	public String getTabHeader() 
	{
		return displayStrings.tree();
	}	
	
	///////////////////////////////////////
	@Override
	public void updateProvenance(String provenance)
	{
		super.updateProvenance(provenance);
				
		int height = (provenance != null && provenance.trim().length() > 0) ? 280 : 360; 
		panel.setHeight(height);
		
		layout();
	}
	
	///////////////////////////////////////
	@Override
	public int getTabIndex()
	{
		return -1;
	}
}
