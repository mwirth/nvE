/**
 */
package com.wimi.nve.model.notesmodel.impl;

import com.wimi.nve.model.notesmodel.Note;
import com.wimi.nve.model.notesmodel.NotesmodelPackage;
import com.wimi.nve.model.notesmodel.Tag;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Note</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.wimi.nve.model.notesmodel.impl.NoteImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link com.wimi.nve.model.notesmodel.impl.NoteImpl#getText <em>Text</em>}</li>
 *   <li>{@link com.wimi.nve.model.notesmodel.impl.NoteImpl#getLastChange <em>Last Change</em>}</li>
 *   <li>{@link com.wimi.nve.model.notesmodel.impl.NoteImpl#getLastCursorPos <em>Last Cursor Pos</em>}</li>
 *   <li>{@link com.wimi.nve.model.notesmodel.impl.NoteImpl#getTags <em>Tags</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NoteImpl extends MinimalEObjectImpl.Container implements Note
{
	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLastChange() <em>Last Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastChange()
	 * @generated
	 * @ordered
	 */
	protected static final Date LAST_CHANGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLastChange() <em>Last Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastChange()
	 * @generated
	 * @ordered
	 */
	protected Date lastChange = LAST_CHANGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLastCursorPos() <em>Last Cursor Pos</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastCursorPos()
	 * @generated
	 * @ordered
	 */
	protected static final int LAST_CURSOR_POS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLastCursorPos() <em>Last Cursor Pos</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastCursorPos()
	 * @generated
	 * @ordered
	 */
	protected int lastCursorPos = LAST_CURSOR_POS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTags() <em>Tags</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTags()
	 * @generated
	 * @ordered
	 */
	protected EList<Tag> tags;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NoteImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return NotesmodelPackage.Literals.NOTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTitle(String newTitle)
	{
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotesmodelPackage.NOTE__TITLE, oldTitle, title));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setText(String newText)
	{
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotesmodelPackage.NOTE__TEXT, oldText, text));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastChange()
	{
		return lastChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLastCursorPos()
	{
		return lastCursorPos;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastCursorPos(int newLastCursorPos)
	{
		int oldLastCursorPos = lastCursorPos;
		lastCursorPos = newLastCursorPos;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotesmodelPackage.NOTE__LAST_CURSOR_POS, oldLastCursorPos, lastCursorPos));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Tag> getTags()
	{
		if (tags == null)
		{
			tags = new EObjectResolvingEList<Tag>(Tag.class, this, NotesmodelPackage.NOTE__TAGS);
		}
		return tags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType)
	{
		switch (featureID)
		{
			case NotesmodelPackage.NOTE__TITLE:
				return getTitle();
			case NotesmodelPackage.NOTE__TEXT:
				return getText();
			case NotesmodelPackage.NOTE__LAST_CHANGE:
				return getLastChange();
			case NotesmodelPackage.NOTE__LAST_CURSOR_POS:
				return getLastCursorPos();
			case NotesmodelPackage.NOTE__TAGS:
				return getTags();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue)
	{
		switch (featureID)
		{
			case NotesmodelPackage.NOTE__TITLE:
				setTitle((String)newValue);
				return;
			case NotesmodelPackage.NOTE__TEXT:
				setText((String)newValue);
				return;
			case NotesmodelPackage.NOTE__LAST_CURSOR_POS:
				setLastCursorPos((Integer)newValue);
				return;
			case NotesmodelPackage.NOTE__TAGS:
				getTags().clear();
				getTags().addAll((Collection<? extends Tag>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID)
	{
		switch (featureID)
		{
			case NotesmodelPackage.NOTE__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case NotesmodelPackage.NOTE__TEXT:
				setText(TEXT_EDEFAULT);
				return;
			case NotesmodelPackage.NOTE__LAST_CURSOR_POS:
				setLastCursorPos(LAST_CURSOR_POS_EDEFAULT);
				return;
			case NotesmodelPackage.NOTE__TAGS:
				getTags().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID)
	{
		switch (featureID)
		{
			case NotesmodelPackage.NOTE__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case NotesmodelPackage.NOTE__TEXT:
				return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
			case NotesmodelPackage.NOTE__LAST_CHANGE:
				return LAST_CHANGE_EDEFAULT == null ? lastChange != null : !LAST_CHANGE_EDEFAULT.equals(lastChange);
			case NotesmodelPackage.NOTE__LAST_CURSOR_POS:
				return lastCursorPos != LAST_CURSOR_POS_EDEFAULT;
			case NotesmodelPackage.NOTE__TAGS:
				return tags != null && !tags.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (title: ");
		result.append(title);
		result.append(", text: ");
		result.append(text);
		result.append(", lastChange: ");
		result.append(lastChange);
		result.append(", lastCursorPos: ");
		result.append(lastCursorPos);
		result.append(')');
		return result.toString();
	}

} //NoteImpl
