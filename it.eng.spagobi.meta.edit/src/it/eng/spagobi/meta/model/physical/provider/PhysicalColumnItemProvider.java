/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.physical.provider;

import it.eng.spagobi.meta.initializer.properties.PhysicalModelPropertiesFromFileInitializer;
import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModelPackage;
import it.eng.spagobi.meta.model.physical.commands.edit.table.RemoveColumnsFromPhysicalTable;
import it.eng.spagobi.meta.model.provider.ModelObjectItemProvider;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.physical.PhysicalColumn} object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class PhysicalColumnItemProvider extends ModelObjectItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PhysicalColumnItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addCommentPropertyDescriptor(object);
			addDataTypePropertyDescriptor(object);
			addTypeNamePropertyDescriptor(object);
			addSizePropertyDescriptor(object);
			addOctectLengthPropertyDescriptor(object);
			addDecimalDigitsPropertyDescriptor(object);
			addRadixPropertyDescriptor(object);
			addDefaultValuePropertyDescriptor(object);
			addNullablePropertyDescriptor(object);
			addPositionPropertyDescriptor(object);
			addTablePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Comment feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addCommentPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_comment_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_comment_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__COMMENT, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Data Type feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDataTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_dataType_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_dataType_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__DATA_TYPE, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Type Name feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addTypeNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_typeName_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_typeName_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__TYPE_NAME, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Size feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_size_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_size_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__SIZE, false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Octect Length feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addOctectLengthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_octectLength_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_octectLength_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__OCTECT_LENGTH, false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Decimal Digits feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDecimalDigitsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_decimalDigits_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_decimalDigits_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__DECIMAL_DIGITS, false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Radix feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRadixPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_radix_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_radix_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__RADIX, false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Default Value feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDefaultValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_defaultValue_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_defaultValue_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__DEFAULT_VALUE, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Nullable feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNullablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_nullable_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_nullable_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__NULLABLE, false, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Position feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addPositionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_position_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_position_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__POSITION, false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Table feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addTablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_PhysicalColumn_table_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_PhysicalColumn_table_feature", "_UI_PhysicalColumn_type"),
				PhysicalModelPackage.Literals.PHYSICAL_COLUMN__TABLE, false, false, true, null, null, null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand} or {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_COLUMN__TYPE_NAME);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_COLUMN__SIZE);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_COLUMN__NULLABLE);
		}
		return childrenFeatures;
	}

	@Override
	public Collection<?> getChildren(Object object) {
		PhysicalColumn physicalColumn;
		FolderItemProvider columnInfoItemProvider;

		Collection children;

		physicalColumn = (PhysicalColumn) object;

		// setting info on node
		columnInfoItemProvider = new FolderItemProvider(adapterFactory, physicalColumn, null);
		columnInfoItemProvider.setImage("full/obj16/Arrow");
		columnInfoItemProvider.setText("Type: " + physicalColumn.getTypeName() + " Size: " + physicalColumn.getSize() + " Nullable: "
				+ physicalColumn.isNullable());

		children = new LinkedHashSet();
		// children.addAll( getChildrenFeatures(object) );
		children.add(columnInfoItemProvider);

		return children;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns PhysicalColumn.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	@Override
	public Object getImage(Object object) {
		PhysicalColumn physicalColumn = ((PhysicalColumn) object);

		if ((physicalColumn.getProperties().get(PhysicalModelPropertiesFromFileInitializer.IS_DELETED) != null)
				&& (Boolean.valueOf(physicalColumn.getProperties().get(PhysicalModelPropertiesFromFileInitializer.IS_DELETED).getValue()) == true)) {
			return overlayImage(object, getResourceLocator().getImage("full/obj16/PhysicalColumnDeleted"));
		} else {
			// if the column is a identifier display the appropriate icon
			if (physicalColumn.isPrimaryKey() || physicalColumn.isPartOfCompositePrimaryKey()) {
				return overlayImage(object, getResourceLocator().getImage("full/obj16/PhysicalPrimaryKey"));
			}
			return overlayImage(object, getResourceLocator().getImage("full/obj16/PhysicalColumn"));
		}

	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
	@Override
	public String getText(Object object) {
		/*
		 * String label = ((PhysicalColumn)object).getName(); return label == null || label.length() == 0 ? getString("_UI_PhysicalColumn_type") :
		 * getString("_UI_PhysicalColumn_type") + " " + label;
		 */
		String label = ((PhysicalColumn) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_PhysicalColumn_type") : label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating a viewer notification, which it passes
	 * to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(PhysicalColumn.class)) {
		case PhysicalModelPackage.PHYSICAL_COLUMN__COMMENT:
		case PhysicalModelPackage.PHYSICAL_COLUMN__DATA_TYPE:
		case PhysicalModelPackage.PHYSICAL_COLUMN__OCTECT_LENGTH:
		case PhysicalModelPackage.PHYSICAL_COLUMN__DECIMAL_DIGITS:
		case PhysicalModelPackage.PHYSICAL_COLUMN__RADIX:
		case PhysicalModelPackage.PHYSICAL_COLUMN__DEFAULT_VALUE:
		case PhysicalModelPackage.PHYSICAL_COLUMN__POSITION:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case PhysicalModelPackage.PHYSICAL_COLUMN__TYPE_NAME:
		case PhysicalModelPackage.PHYSICAL_COLUMN__SIZE:
		case PhysicalModelPackage.PHYSICAL_COLUMN__NULLABLE:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children that can be created under this object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add(createChildParameter(PhysicalModelPackage.Literals.PHYSICAL_COLUMN__TYPE_NAME, ""));

		newChildDescriptors.add(createChildParameter(PhysicalModelPackage.Literals.PHYSICAL_COLUMN__SIZE,
				EcoreFactory.eINSTANCE.createFromString(EcorePackage.Literals.EINT, "0")));

		newChildDescriptors.add(createChildParameter(PhysicalModelPackage.Literals.PHYSICAL_COLUMN__NULLABLE,
				EcoreFactory.eINSTANCE.createFromString(EcorePackage.Literals.EBOOLEAN, "false")));
	}

	/**
	 * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}

	@Override
	public Command createCustomCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter) {
		Command result;

		result = null;

		if (commandClass == RemoveColumnsFromPhysicalTable.class) {
			result = new RemoveColumnsFromPhysicalTable(domain, commandParameter);
		}

		return result;
	}

}
