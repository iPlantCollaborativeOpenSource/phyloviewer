package org.iplantc.phyloviewer.viewer.client.services;

import org.iplantc.phyloviewer.shared.math.Box2D;
import org.iplantc.phyloviewer.shared.math.Vector2;
import org.iplantc.phyloviewer.viewer.client.model.RemoteNode;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("nodeLayout")
public interface CombinedService extends RemoteService
{	
	CombinedResponse getChildrenAndLayout(int parentID);
	CombinedResponse[] getChildrenAndLayout(int[] parentIDs);
	
	public class CombinedResponse implements IsSerializable
	{
		public int parentID;
		public LayoutResponse[] layouts;
		public RemoteNode[] nodes;
	}
	
	/**
	 * @param id
	 * @return the tree with the given ID. On the client, the tree will only have a root node and the
	 *         rest must be fetched using RemoteNode.getChildrenAsync()
	 * @throws TreeNotAvailableException 
	 */
	NodeResponse getRootNode(int treeId) throws TreeNotAvailableException;
	
	public class LayoutResponse implements IsSerializable {
		public int nodeID;
		public Box2D boundingBox;
		public Vector2 position;
	}
	
	public class NodeResponse implements IsSerializable
	{
		public RemoteNode node;
		public LayoutResponse layout;
	}
}
