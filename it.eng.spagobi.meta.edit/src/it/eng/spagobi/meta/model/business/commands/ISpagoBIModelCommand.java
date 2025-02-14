/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;

/**
 * TODO implement some mechanism to handle command exception
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface ISpagoBIModelCommand extends Command, CommandActionDelegate {
	CommandParameter getParameter();
	void setParameter(CommandParameter parameter);
}
