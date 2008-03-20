/* =================================================================
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
#
# ================================================================= */
package org.sgodden.echo.ext20.grid;

import java.io.Serializable;
import java.util.List;

/**
 * An ext column model.
 * 
 * @author sgodden
 *
 */
public class ColumnModel 
		implements Serializable {

	private static final long serialVersionUID = 20080310L;
	
	private List<ColumnConfiguration> columns;
	private boolean defaultSortable = true;
	private Integer defaultWidth;

	/**
	 * Constructs a new column model using the specified
	 * column configurations.
	 * @param columns
	 */
	public ColumnModel(List<ColumnConfiguration> columns) {
		super();
		this.columns = columns;
	}

	public List<ColumnConfiguration> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnConfiguration> columns) {
		this.columns = columns;
	}

	public boolean isDefaultSortable() {
		return defaultSortable;
	}

	public void setDefaultSortable(boolean defaultSortable) {
		this.defaultSortable = defaultSortable;
	}

	public Integer getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(Integer defaultWidth) {
		this.defaultWidth = defaultWidth;
	}
	
}
