/**
 */
package com.wimi.nve.model.notesmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.wimi.nve.model.notesmodel.Model#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.wimi.nve.model.notesmodel.NotesmodelPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
	/**
	 * Returns the value of the '<em><b>Notes</b></em>' containment reference list.
	 * The list contents are of type {@link com.wimi.nve.model.notesmodel.Note}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' containment reference list.
	 * @see com.wimi.nve.model.notesmodel.NotesmodelPackage#getModel_Notes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Note> getNotes();

} // Model
