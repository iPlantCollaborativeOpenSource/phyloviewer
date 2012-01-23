package org.iplantc.phyloviewer.shared.model.metadata;

/**
 * An interface for node and tree annotations. 
 */
public interface Annotation
{

	public abstract String getKey();

	public abstract String getPredicateNamespace();

	public abstract Object getValue();
}