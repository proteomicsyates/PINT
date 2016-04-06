package edu.scripps.yates.client.gui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.events.DiagramLoadedEvent;
import org.reactome.web.diagram.handlers.DiagramLoadedHandler;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.client.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.model.util.Ancestors;
import org.reactome.web.pwp.model.util.Path;

import com.google.gwt.core.client.Scheduler;

public class DiagramLoader implements DatabaseObjectCreatedHandler, AncestorsCreatedHandler, DiagramLoadedHandler {

	public interface SubpathwaySelectedHandler {
		void onSubPathwaySelected(String identifier);
	}

	private final Set<SubpathwaySelectedHandler> handlers = new HashSet<>();
	private final DiagramViewer diagram;

	private String loadedDiagram;
	private String selectedPathway;
	private String target;

	public DiagramLoader(DiagramViewer diagram) {
		this.diagram = diagram;
		this.diagram.addDiagramLoadedHandler(this);
	}

	public void load(String identifier) {
		if (!Objects.equals(identifier, selectedPathway)) {
			DatabaseObjectFactory.get(identifier, this);
		}
	}

	public String getTarget() {
		return target;
	}

	public void addSubpathwaySelectedHandler(SubpathwaySelectedHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
		if (databaseObject instanceof Event) {
			target = databaseObject.getIdentifier();
			Event event = (Event) databaseObject;
			RESTFulClient.getAncestors(event, this);
			return;
		}
		target = null;
	}

	@Override
	public void onDatabaseObjectError(Throwable exception) {
		// TODO
	}

	@Override
	public void onAncestorsLoaded(Ancestors ancestors) {
		if (ancestors != null) {
			Iterator<Path> it = ancestors.iterator();
			if (it.hasNext()) {
				Path path = it.next();
				String toLoad = path.getLastPathwayWithDiagram().getIdentifier();
				if (toLoad != null && !toLoad.equals(loadedDiagram)) {
					diagram.loadDiagram(toLoad);
				} else if (target != null && !target.equals(toLoad)) {
					onDiagramLoaded();
				}
				return;
			}
		}
		// TODO Console.error(
		// "No ancestors found. Please check whether the provided identifier
		// belongs to a Pathway or Reaction.");
	}

	@Override
	public void onAncestorsError(Throwable exception) {
		// TODO Console.error(exception.getMessage());
	}

	@Override
	public void onDiagramLoaded(DiagramLoadedEvent event) {
		loadedDiagram = event.getContext().getContent().getStableId();
		selectedPathway = loadedDiagram;
		onDiagramLoaded();
	}

	public void onDiagramLoaded() {
		if (!loadedDiagram.equals(target)) {
			diagram.selectItem(target);
			selectedPathway = target;
			for (SubpathwaySelectedHandler handler : handlers) {
				handler.onSubPathwaySelected(target);
			}
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					target = null;
				}
			});
		} else {
			target = null;
		}
	}
}
