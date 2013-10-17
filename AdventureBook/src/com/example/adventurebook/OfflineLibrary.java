package com.example.adventurebook;

import java.io.Serializable;
import java.util.Collection;


public class OfflineLibrary extends Model implements Serializable{

	/**
	 * @uml.property   name="controller"
	 * @uml.associationEnd   inverse="offlineLibrary:com.example.adventurebook.SController"
	 */
	private SController controller;

	/**
	 * Getter of the property <tt>controller</tt>
	 * @return  Returns the controller.
	 * @uml.property  name="controller"
	 */
	public SController getController() {
		return controller;
	}

	/**
	 * Setter of the property <tt>controller</tt>
	 * @param controller  The controller to set.
	 * @uml.property  name="controller"
	 */
	public void setController(SController controller) {
		this.controller = controller;
	}

	/**
	 * @uml.property   name="story"
	 * @uml.associationEnd   multiplicity="(0 -1)" aggregation="shared" inverse="offlineLibrary:com.example.adventurebook.Story"
	 */
	private Collection<Story> story;

	/** 
	 * Getter of the property <tt>story</tt>
	 * @return  Returns the story.
	 * @uml.property  name="story"
	 */
	public Collection<Story> getStory() {
		return story;
	}

	/** 
	 * Setter of the property <tt>story</tt>
	 * @param story  The story to set.
	 * @uml.property  name="story"
	 */
	public void setStory(Collection<Story> story) {
		this.story = story;
	}

}
