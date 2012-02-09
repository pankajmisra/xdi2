package xdi2.impl.keyvalue;

import java.io.IOException;

import xdi2.Graph;
import xdi2.GraphFactory;
import xdi2.impl.AbstractGraphFactory;

/**
 * GraphFactory that creates key/value graphs.
 * 
 * @author markus
 */
public abstract class AbstractKeyValueGraphFactory extends AbstractGraphFactory implements GraphFactory {

	private boolean supportGetContextNodes;
	private boolean supportGetRelations;

	public AbstractKeyValueGraphFactory(boolean supportGetContextNodes, boolean supportGetRelations) {

		this.supportGetContextNodes = supportGetContextNodes;
		this.supportGetRelations = supportGetRelations;
	}

	public final Graph openGraph() throws IOException {

		KeyValueStore keyValueStore = this.getKeyValueStore();

		return new KeyValueGraph(keyValueStore, this.supportGetContextNodes, this.supportGetRelations);
	}

	protected abstract KeyValueStore getKeyValueStore() throws IOException;

	public boolean isSupportGetContextNodes() {

		return this.supportGetContextNodes;
	}

	public void setSupportGetContextNodes(boolean supportGetContextNodes) {

		this.supportGetContextNodes = supportGetContextNodes;
	}

	public boolean isSupportGetRelations() {

		return this.supportGetRelations;
	}

	public void setSupportGetRelations(boolean supportGetRelations) {

		this.supportGetRelations = supportGetRelations;
	}
}
