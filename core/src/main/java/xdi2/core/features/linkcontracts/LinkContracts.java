package xdi2.core.features.linkcontracts;

import java.util.Iterator;

import xdi2.core.ContextNode;
import xdi2.core.Graph;
import xdi2.core.constants.XDILinkContractConstants;
import xdi2.core.util.iterators.SelectingMappingIterator;
import xdi2.core.xri3.impl.XRI3Segment;


public class LinkContracts {


	private LinkContracts() {
	}

	/**
	 * Given a graph, lists all link contracts.
	 * 
	 * @param graph
	 *            The graph.
	 * @return An iterator over link contracts.
	 */
	public static Iterator<LinkContract> getAllLinkContracts(Graph graph) {

		ContextNode root = graph.getRootContextNode();
		Iterator<ContextNode> allContextNodes = root.getAllContextNodes();
		return new SelectingMappingIterator<ContextNode, LinkContract> (allContextNodes) {

			@Override
			public boolean select(ContextNode contextNode) {

				return LinkContract.isValid(contextNode);
			}

			@Override
			public LinkContract map(ContextNode contextNode) {

				return LinkContract.fromContextNode(contextNode,false);
			}
		};		
	}

	/**
	 * Returns an existing XDI link contract under a context node, or creates a
	 * new one.
	 * 
	 * @param create
	 *            Whether to create an XDI link contract if it does not exist.
	 * @return The existing or newly created XDI link contract.
	 */
	public static LinkContract getLinkContract(ContextNode contextNode,
			boolean create) {

		ContextNode linkContractContextNode = contextNode
				.getContextNode(XDILinkContractConstants.XRI_SS_DO);
		if (linkContractContextNode == null && create)
			linkContractContextNode = contextNode
					.createContextNode(XDILinkContractConstants.XRI_SS_DO);
		if (linkContractContextNode == null)
			return null;

		LinkContract lc = LinkContract.fromContextNode(linkContractContextNode,false);
		return lc;
	}
	/**
	 * Returns an existing XDI link contract under a context node, or creates a
	 * new one.
	 * 
	 * @param create
	 *            Whether to create an XDI link contract if it does not exist.
	 * @return The existing or newly created XDI link contract.
	 */
	public static LinkContract getLinkContract(ContextNode contextNode,
			boolean create , boolean withAuthFunc) {

		ContextNode linkContractContextNode = contextNode
				.getContextNode(XDILinkContractConstants.XRI_SS_DO);
		if (linkContractContextNode == null && create)
			linkContractContextNode = contextNode
					.createContextNode(XDILinkContractConstants.XRI_SS_DO);
		if (linkContractContextNode == null)
			return null;

		LinkContract lc = LinkContract.fromContextNode(linkContractContextNode,withAuthFunc);
		return lc;
	}
	/**
	 * Find a Link Contract object given a graph and the XRI of the object
	 * @param graph The graph where this Link Contract is supposed to exist
	 * @param address The fully qualified XRI of the Link Contract object
	 * @return The Link Contract object if found or null
	 */

	public static LinkContract findLinkContractByAddress(Graph graph,
			XRI3Segment address) {

		ContextNode root = graph.getRootContextNode();
		
		for(Iterator<ContextNode> cIter =   root.getAllContextNodes() ; cIter.hasNext();){
			
			ContextNode c = cIter.next();
			if(LinkContract.isValid(c)){
				if(c.getXri().equals(address)){
					return LinkContract.fromContextNode(c,false);
				}
			}
			
		}
		return null;
	}

}
