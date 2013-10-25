package com.example.adventurebook;


public class EditOptionActivity {

	/**
	 * @uml.property  name="sController"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="editOptionActivity:com.example.adventurebook.SController"
	 */
	private SController sController = new com.example.adventurebook.SController();

	/**
	 * Getter of the property <tt>sController</tt>
	 * @return  Returns the sController.
	 * @uml.property  name="sController"
	 */
	public SController getSController() {
		return sController;
	}

	/**
	 * Setter of the property <tt>sController</tt>
	 * @param sController  The sController to set.
	 * @uml.property  name="sController"
	 */
	public void setSController(SController sController) {
		this.sController = sController;
	}

}