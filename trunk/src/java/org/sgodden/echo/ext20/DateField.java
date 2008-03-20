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

import java.util.Date;
import java.util.Locale;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A date field.
 * <p/>
 * TODO - listeners on value change
 * 
 * @author sgodden
 *
 */
public class DateField
        extends Component {
    
    private static final transient Log log = LogFactory.getLog(DateField.class);

    public static final String DATE_FORMAT_PROPERTY = "dateFormat";
    public static final String DATE_CHANGED_PROPERTY = "date";
    public static final String FIELD_LABEL_PROPERTY = "fieldLabel";
    public static final String ALLOW_BLANK_PROPERTY = "allowBlank";

    public DateField() {
        super();
        setLocale(ApplicationInstance.getActive().getLocale());
    }

    public DateField(Date text) {
        this();
        setDate(text);
    }

    public DateField(Date text, String fieldLabel) {
        this();
        setDate(text);
        setFieldLabel(fieldLabel);
    }

    public void setDate(Date date) {
        setProperty(DATE_CHANGED_PROPERTY, date);
    }

    public Date getDate() {
        return (Date) getProperty(DATE_CHANGED_PROPERTY);
    }

    public void setFieldLabel(String fieldLabel) {
        setProperty(FIELD_LABEL_PROPERTY, fieldLabel);
    }

    public void setBlankAllowed(boolean blankAllowed) {
        setProperty(ALLOW_BLANK_PROPERTY, blankAllowed);
    }

    /**
     * BROKEN - Sets the locale, and derives the ext locale string from it.
     * <p/>
     * This is broken for all but the simplest cases - use {@link #setExtLocaleString(String)}
     * instead.
     * @param locale
     */
    @Override
    public void setLocale(Locale locale) {
        log.info(locale);
        super.setLocale(locale);
        // FIXME - we need a robust way of calculating the ext date format
        if (locale.getCountry().equals("GB")) {
            setProperty(DATE_FORMAT_PROPERTY, "d/m/y");
        }
        else {
            setProperty(DATE_FORMAT_PROPERTY, "m/d/y");
        }
    }
    
    /**
     * Directly sets the locale string, since the automatic derivation of
     * this from the java locale is currently broken.
     * @param localeString
     * @return
     */
    public void setExtLocaleString(String localeString) {
        setProperty(DATE_FORMAT_PROPERTY, localeString);
    }

    @Override
    public void processInput(String inputName, Object inputValue) {
        if (DATE_CHANGED_PROPERTY.equals(inputName)) {
            setDate((Date) inputValue);
        }
    }
}