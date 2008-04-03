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
package org.sgodden.echo.ext20;

import nextapp.echo.app.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract superclass for all ext components.
 * 
 * @author sgodden
 */
public abstract class ExtComponent 
        extends Component {
    
    private static final transient Log log = LogFactory.getLog(ExtComponent.class);
    
    public static final String ALIGNTO_PROPERTY = "alignTo";

    private Component alignToOtherComponent;
    private Alignment sourceAlignment;
    private Alignment targetAlignment;
    private int xAlignOffset;
    private int yAlignOffset;
    
    /**
     * Aligns this component to another component, anchoring the source
     * alignment to the target alignment.
     * <p/>
     * For example, to position the bottom right corner of comp1 to the
     * bottom left corner of comp2, with a horizontal
     * shift of 5 pixels to the left, you would do the following:
     * <pre>
     *   comp1.alignTo(
     *     comp2,
     *     Alignment.BOTTOM_RIGHT,
     *     Alignment.BOTTOM_LEFT,
     *     -5,
     *     0
     *   );
     * </pre>
     * @param other the component relative to which this component should
     * be positioned.
     * @param sourceAlignment the alignment (edge) of the source component.
     * @param targetAlignment the alignment (edge) of the target component.
     */
    public void alignTo(
            Component other, 
            Alignment sourceAlignment,
            Alignment targetAlignment,
            int xOffset,
            int yOffset) {
        
        if (other == null) {
            throw new NullPointerException("other component");
        }
        
        if (sourceAlignment == null) {
            throw new NullPointerException("source alignment");            
        }
        
        if (targetAlignment == null) {
            throw new NullPointerException("target alignment");
        }
        
        this.alignToOtherComponent = other;
        this.sourceAlignment = sourceAlignment;
        this.targetAlignment = targetAlignment;
        this.xAlignOffset = xOffset;
        this.yAlignOffset = yOffset;
    }
    
    public String getAlignToPropertyString() {
        
        String ret = null;
        
        if (alignToOtherComponent != null) {
            StringBuffer sb = new StringBuffer(alignToOtherComponent.getRenderId());
            sb.append(',');
            sb.append(convertToExtAlignment(sourceAlignment));
            sb.append('-');
            sb.append(convertToExtAlignment(targetAlignment));
            sb.append(',');
            sb.append(String.valueOf(xAlignOffset));
            sb.append(':');
            sb.append(String.valueOf(yAlignOffset));

            ret = sb.toString();
        }
        
        return ret;
    }
    
    private String convertToExtAlignment(Alignment alignment) {
        switch(alignment) {
            case TOP_LEFT:
                return "tl";
            case TOP:
                return "t";
            case TOP_RIGHT:
                return "tr";
            case BOTTOM_LEFT:
                return "bl";
            case BOTTOM:
                return "b";
            case BOTTOM_RIGHT:
                return "br";
            case LEFT:
                return "l";
            case RIGHT:
                return "r";
            default:
                throw new IllegalArgumentException("Unrecognised alignment constant");
        }
    }
    
}