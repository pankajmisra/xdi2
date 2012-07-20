package xdi2.core.features.linkcontracts;

import java.util.Iterator;

import xdi2.core.ContextNode;
import xdi2.core.constants.XDILinkContractConstants;

public class AndExpression extends PolicyExpressionComponent {
	private static final long serialVersionUID = 5732150498065911411L;

	public AndExpression(ContextNode c) {
		super(c);
	}

	public static boolean isValid(ContextNode node) {
		return XDILinkContractConstants.XRI_SS_AND.equals(node.getArcXri());
	}

	public static AndExpression fromContextNode(ContextNode c) {
		if (!isValid(c)) {
			return null;
		}
		return new AndExpression(c);
	}

	public boolean evaluate() {
		boolean evalResult = true;
		Iterator<ContextNode> allChildrenNodes = contextNode.getContextNodes();

		for (; allChildrenNodes.hasNext();) {
			ContextNode childNode = allChildrenNodes.next();
			boolean childExprEvalResult = evaluateChildBranch(childNode);
			evalResult = evalResult && childExprEvalResult;
		}
		return evalResult;
	}
}
