/**
 */
package org.wimi.nve.model.notesmodel;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Note</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.wimi.nve.model.notesmodel.Note#getTitle <em>Title</em>}</li>
 *   <li>{@link org.wimi.nve.model.notesmodel.Note#getText <em>Text</em>}</li>
 *   <li>{@link org.wimi.nve.model.notesmodel.Note#getLastChange <em>Last Change</em>}</li>
 *   <li>{@link org.wimi.nve.model.notesmodel.Note#getLastCursorPos <em>Last Cursor Pos</em>}</li>
 *   <li>{@link org.wimi.nve.model.notesmodel.Note#getTags <em>Tags</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.wimi.nve.model.notesmodel.NotesmodelPackage#getNote()
 * @model
 * @generated
 */
public interface Note extends EObject
{
	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.wimi.nve.model.notesmodel.NotesmodelPackage#getNote_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.wimi.nve.model.notesmodel.Note#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.wimi.nve.model.notesmodel.NotesmodelPackage#getNote_Text()
	 * @model
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link org.wimi.nve.model.notesmodel.Note#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

	/**
	 * Returns the value of the '<em><b>Last Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Change</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Change</em>' attribute.
	 * @see org.wimi.nve.model.notesmodel.NotesmodelPackage#getNote_LastChange()
	 * @model changeable="false"
	 * @generated
	 */
	Date getLastChange();

	/**
	 * Returns the value of the '<em><b>Last Cursor Pos</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Last Cursor Pos</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Last Cursor Pos</em>' attribute.
	 * @see #setLastCursorPos(int)
	 * @see org.wimi.nve.model.notesmodel.NotesmodelPackage#getNote_LastCursorPos()
	 * @model
	 * @generated
	 */
	int getLastCursorPos();

	/**
	 * Sets the value of the '{@link org.wimi.nve.model.notesmodel.Note#getLastCursorPos <em>Last Cursor Pos</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Cursor Pos</em>' attribute.
	 * @see #getLastCursorPos()
	 * @generated
	 */
	void setLastCursorPos(int value);

	/**
	 * Returns the value of the '<em><b>Tags</b></em>' reference list.
	 * The list contents are of type {@link org.wimi.nve.model.notesmodel.Tag}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tags</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tags</em>' reference list.
	 * @see org.wimi.nve.model.notesmodel.NotesmodelPackage#getNote_Tags()
	 * @model
	 * @generated
	 */
	EList<Tag> getTags();

} // Note
