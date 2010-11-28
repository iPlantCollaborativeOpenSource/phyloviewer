package org.iplantc.phyloviewer.shared.render.style;

import org.iplantc.phyloviewer.shared.model.INode;

public interface IStyleMap
{
	abstract INodeStyle get(INode node);
	abstract void put(INode node, INodeStyle style);
	abstract void clear();
}