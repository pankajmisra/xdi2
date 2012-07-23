package xdi2.xri2xdi;

import xdi2.core.ContextNode;
import xdi2.core.Graph;
import xdi2.core.constants.XDIConstants;
import xdi2.core.constants.XDIDictionaryConstants;
import xdi2.core.features.dictionary.Dictionary;
import xdi2.core.features.multiplicity.AttributeSingleton;
import xdi2.core.features.multiplicity.Multiplicity;
import xdi2.core.features.remoteroots.RemoteRoots;
import xdi2.core.xri3.impl.XRI3Segment;
import xdi2.messaging.MessageResult;
import xdi2.messaging.Operation;
import xdi2.messaging.exceptions.Xdi2MessagingException;
import xdi2.messaging.target.ExecutionContext;
import xdi2.messaging.target.impl.AbstractMessagingTarget;
import xdi2.xri2xdi.resolution.XriResolutionException;
import xdi2.xri2xdi.resolution.XriResolutionResult;
import xdi2.xri2xdi.resolution.XriResolver;

public class Xri2XdiMessagingTarget extends AbstractMessagingTarget {

	public static final String XRI_URI = "$uri";
	public static final String STRING_TYPE_XDI = "$xdi$*($v)$!1";
	public static final XRI3Segment XRI_TYPE_XDI = new XRI3Segment(STRING_TYPE_XDI);

	private XriResolver xriResolver;

	@Override
	public void init() throws Exception {

		super.init();

		if (this.xriResolver == null) this.xriResolver = new XriResolver();
	}

	@Override
	public XRI3Segment getOwner() {

		return null;
	}

	@Override
	public boolean executeGetOnAddress(XRI3Segment targetAddress, Operation operation, MessageResult messageResult, ExecutionContext executionContext) throws Xdi2MessagingException {

		// is this a remote root context XRI?

		XRI3Segment xri;

		if (RemoteRoots.isRemoteRootXri(targetAddress)) {

			xri = RemoteRoots.getXriOfRemoteRootXri(targetAddress);
		} else {

			xri = targetAddress;
		}

		// resolve the XRI

		XRI3Segment inumber;
		String uri;

		try {

			XriResolutionResult resolutionResult = this.xriResolver.resolve(xri.toString());
			inumber = new XRI3Segment(resolutionResult.getInumber());
			uri = resolutionResult.getXdiUri();
		} catch (XriResolutionException ex) {

			throw new Xdi2MessagingException("XRI Resolution error: " + ex.getMessage(), ex, null);
		}

		// prepare result graph

		Graph graph = messageResult.getGraph();
		ContextNode rootContextNode = graph.getRootContextNode();

		// add "self" remote root context nodes

		ContextNode selfRemoteRootContextNode = RemoteRoots.findRemoteRootContextNode(graph, XDIConstants.XRI_S_ROOT, true);
		selfRemoteRootContextNode.createRelation(XDIDictionaryConstants.XRI_S_IS, rootContextNode);

		// add I-Number remote root context nodes

		ContextNode inumberRemoteRootContextNode = RemoteRoots.findRemoteRootContextNode(graph, inumber, true);

		// add URIs

		if (uri != null) {

			AttributeSingleton uriAttributeSingleton = Multiplicity.getAttributeSingleton(inumberRemoteRootContextNode, XRI_URI, true);
			Dictionary.addContextNodeType(uriAttributeSingleton.getContextNode(), XRI_TYPE_XDI);
			uriAttributeSingleton.getContextNode().createLiteral(uri);
		}

		// add I-Number and original XRI

		ContextNode inumberContextNode = graph.findContextNode(inumber, true);

		if (! xri.equals(inumber)) {

			ContextNode xriContextNode = graph.findContextNode(xri, true);
			xriContextNode.createRelation(XDIDictionaryConstants.XRI_S_IS, inumberContextNode);
		}

		// done

		return true;
	}

	public XriResolver getXriResolver() {

		return this.xriResolver;
	}

	public void setXriResolver(XriResolver xriResolver) {

		this.xriResolver = xriResolver;
	}
}
