/**
 * BenchLab: Internet Scale Benchmarking.
 * Copyright (C) 2010-2011 Emmanuel Cecchet.
 * Contact: cecchet@cs.umass.edu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Initial developer(s): karlholl (sf.net)
 * Contributor(s): Emmanuel Cecchet. , Ralic Lo
 */
package org.raliclo.KrogerStores.lib;

import com.fasterxml.jackson.core.*;
import org.raliclo.KrogerStores.lib.tools.HarFileWriter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * This class defines a HarHeader
 *
 * @author <a href="mailto:cecchet@cs.umass.edu>Emmanuel Cecchet</a>
 * @version 1.0
 */
public class HarHeader extends AbstractNameValueComment {

    private HarCustomFields customFields = new HarCustomFields();

    /**
     * Creates a new <code>HarHeader</code> object
     *
     * @param name    name of the header object
     * @param value   value of the header object
     * @param comment optional comment provided by the user or the application
     * @throws SQLException
     */
    public HarHeader(String name, String value, String comment) {
        super(name, value, comment);
    }

    /**
     * Creates a new <code>HarHeader</code> object from the database
     *
     * @param config
     * @param headerId
     * @param name
     * @param value
     * @param comment
     * @throws SQLException
     */
    public HarHeader(HarDatabaseConfig config, long headerId, String name,
                     String value, String comment) throws SQLException {
        super(name, value, comment);
        this.customFields.readCustomFieldsJDBC(config,
                HarCustomFields.Type.HARHEADER, headerId);
    }

    /**
     * Creates a new <code>HarHeader</code> object
     *
     * @param name  name of the header object
     * @param value value of the header object
     */
    public HarHeader(String name, String value) {
        super(name, value);
    }

    /**
     * Creates a new <code>HarHeader</code> object from a JsonParser already
     * positioned at the beginning of the element content
     *
     * @param jp       a JsonParser already positioned at the beginning of the element
     *                 content
     * @param warnings null if parser should fail on first error, pointer to
     *                 warning list if warnings can be issued for missing fields
     * @throws JsonParseException
     * @throws IOException
     */
    public HarHeader(JsonParser jp, List<HarWarning> warnings)
            throws IOException {
        super(jp);

        // Read the content of the log element
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(jp, "{ missing after \"headers\" element");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String name = jp.getCurrentName();
            if ("name".equals(name))
                setName(jp.getText());
            else if ("value".equals(name))
                setValue(jp.getText());
            else if ("comment".equals(name))
                setComment(jp.getText());
            else if (name != null && name.startsWith("_"))
                this.customFields.addHarCustomFields(name, jp);
            else
                throw new JsonParseException(jp, "Unrecognized field '" + name
                        + "' in headers element");
        }
        if (getName() == null) {
            if (warnings != null)
                warnings.add(new HarWarning("Missing name field in header element", jp
                        .getCurrentLocation()));
            else
                throw new JsonParseException(jp, "Missing name field in header element");
        }
        if (getValue() == null) {
            if (warnings != null)
                warnings.add(new HarWarning("Missing value field in header element", jp
                        .getCurrentLocation()));
            else
                throw new JsonParseException(jp, "Missing value field in header element");
        }
    }

    /**
     * Write this object on a JsonGenerator stream
     *
     * @param g a JsonGenerator
     * @throws IOException             if an IO error occurs
     * @throws JsonGenerationException if the generator fails
     * @see HarFileWriter#writeHarFile(HarLog, java.io.File)
     */
    public void writeHar(JsonGenerator g) throws
            IOException {
        g.writeStartObject();
        g.writeStringField("name", getName());
        g.writeStringField("value", getValue());
        if (getComment() != null)
            g.writeStringField("comment", getComment());
        this.customFields.writeHar(g);
        g.writeEndObject();
    }

    /**
     * Write this object in the given database referencing the specified id.
     *
     * @param id        the id this object refers to
     * @param ps        PreparedStatement to write data
     * @param isRequest true if these cookies belong to a request, false if they
     *                  belong to a response
     * @throws SQLException if a database access error occurs
     */
    public void writeJDBC(HarDatabaseConfig config, long id,
                          PreparedStatement ps, boolean isRequest, long logId) throws SQLException {
        ps.setString(1, getName());
        ps.setString(2, getValue());
        if (getComment() == null)
            ps.setNull(3, Types.LONGVARCHAR);
        else
            ps.setString(3, getComment());
        ps.setInt(4, isRequest ? 1 : 0);
        ps.setLong(5, id);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (!rs.next())
            throw new SQLException(
                    "The database did not generate a key for an HarPage entry");
        long hearderId = rs.getLong(1);
        this.customFields.writeCustomFieldsJDBC(config,
                HarCustomFields.Type.HARHEADER, hearderId, logId);
    }

    /**
     * Returns the customFields value.
     *
     * @return Returns the customFields.
     */
    public HarCustomFields getCustomFields() {
        return customFields;
    }

    /**
     * Sets the customFields value.
     *
     * @param customFields The customFields to set.
     */
    public void setCustomFields(HarCustomFields customFields) {
        this.customFields = customFields;
    }

    @Override
    public String toString() {
        return "{ \"name\": \"" + getName() + "\", \"value\": \"" + getValue()
                + "\", \"comment\": \"" + getComment() + "\", " + customFields + " }";
    }

}
