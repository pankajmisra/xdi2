package xdi2;

import java.io.Serializable;

/**
 * This interface represents a literal in an XDI graph.
 * 
 * @author markus
 */
public interface Literal extends Serializable, Comparable<Literal> {

	/*
	 * General methods
	 */

	/**
	 * Get the graph of this literal.
	 * @return The graph of this literal.
	 */
	public Graph getGraph();
	
	/**
	 * Every literal has a context node from which it originates.
	 * @return The context node of this literal.
	 */
	public ContextNode getContextNode();

	/**
	 * Deletes this literal.
	 */
	public void delete();

	/**
	 * Get the literal data.
	 * @return The literal data associated with the literal.
	 */
	public String getLiteralData();

	/**
	 * Set the literal data.
	 * @param literalData The literal data associated with the literal.
	 */
	public void setLiteralData(String literalData);
}
