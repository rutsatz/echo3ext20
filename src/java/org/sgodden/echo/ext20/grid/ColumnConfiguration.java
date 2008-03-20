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

/**
 * Configuration of a column for a grid.
 * 
 * @author sgodden
 *
 */
public class ColumnConfiguration 
		implements Serializable {

	private static final long serialVersionUID = 20080310L;
	
	private String header;
	private Integer width;
	private Boolean sortable = true;
	private String dataIndex;
	
	/**
	 * Constructs a new column configuration.
	 * @param header the header text.
	 * @param width a specific width for the column.
	 * @param sortable whether the column is sortable.
	 * @param dataIndex the id of the column in the store to which this column maps.
	 */
	public ColumnConfiguration(String header, Integer width, Boolean sortable,
			String dataIndex) {
		super();
		this.header = header;
		this.width = width;
		this.sortable = sortable;
		this.dataIndex = dataIndex;
	}
	
	/**
	 * Constructs a new column configuration.
	 * @param header the header text.
	 * @param id the id of the column in the store to which this column maps.
	 */
	public ColumnConfiguration(String header, String id) {
		this(header, null, true, id);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public String getDataIndex() {
		return dataIndex;
	}

	public void setDataIndex(String dataIndex) {
		this.dataIndex = dataIndex;
	}

}
