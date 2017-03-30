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
import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * This class defines a HarPage
 *
 * @author <a href="mailto:cecchet@cs.umass.edu>Emmanuel Cecchet</a>
 * @version 1.0
 */
public class HarPage {
    /**
     * Database table name where the data is stored
     */
    public static String TABLE_NAME = "pages";

    private String id;
    private Date startedDateTime;
    private String title;
    private HarPageTimings pageTimings;
    private String comment;

    // private long pageIdDB;
    private HarCustomFields customFields = new HarCustomFields();

    /**
     * Creates a new <code>HarPage</code> object
     *
     * @param id              Unique identifier of a page within the <log>. Entries use it to
     *                        refer to the parent page.
     * @param startedDateTime Date and timestamp for the beginning of the page
     *                        load (ISO 8601 - YYYY-MM-DDThh:mm:ss.SZ, e.g.
     *                        2010-05-19T13:50:21.505-04:00)
     * @param title           Page title
     * @param pageTimings     Detailed timing info about page load
     */
    public HarPage(String id, Date startedDateTime, String title,
                   HarPageTimings pageTimings) {
        this.id = id;
        this.startedDateTime = startedDateTime;
        this.title = title;
        this.pageTimings = pageTimings;
    }

    /**
     * Creates a new <code>HarPage</code> object from a JsonParser already
     * positioned at the beginning of the element content
     *
     * @param jp       a JsonParser already positioned at the beginning of the element
     *                 content
     * @param warnings null if parser should fail on first error, pointer to
     *                 warning list if warnings can be issued for missing fields
     * @throws JsonParseException
     * @throws IOException
     */
    public HarPage(JsonParser jp, List<HarWarning> warnings)
            throws IOException {
        // Read the content of the log element
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(jp, "{ missing after \"pages\" element");
        }

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String name = jp.getCurrentName();
            if ("id".equals(name))
                setId(jp.getText());
            else if ("startedDateTime".equals(name)) {
                try {
                    if (jp.getCurrentName().equals(jp.getText()))
                        jp.nextToken();
                    setStartedDateTime(ISO8601DateFormatter.parseDate(jp.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new JsonParseException(jp, "Invalid date format '" + jp.getText() + "'");
                }
            } else if ("title".equals(name))
                setTitle(jp.getText());
            else if ("pageTimings".equals(name))
                setPageTimings(new HarPageTimings(jp, warnings));
            else if ("comment".equals(name))
                setComment(jp.getText());
            else if (name != null && name.startsWith("_"))
                this.customFields.addHarCustomFields(name, jp);
            else
                throw new JsonParseException(jp, "Unrecognized field '" + name + "' in page element");
        }
        if (startedDateTime == null) {
            if (warnings != null)
                warnings.add(new HarWarning(
                        "Missing startedDateTime field in pages element", jp
                        .getCurrentLocation()));
            else
                throw new JsonParseException(jp,
                        "Missing startedDateTime field in pages element");
        }
        if (id == null) {
            if (warnings != null)
                warnings.add(new HarWarning("Missing id field in pages element", jp
                        .getCurrentLocation()));
            else
                throw new JsonParseException(jp, "Missing id field in pages element");
        }
        if (title == null) {
            if (warnings != null)
                warnings.add(new HarWarning("Missing title field in pages element", jp
                        .getCurrentLocation()));
            else
                throw new JsonParseException(jp, "Missing title field in pages element");
        }
        if (pageTimings == null) {
            if (warnings != null)
                warnings.add(new HarWarning(
                        "Missing pageTimings field in pages element", jp
                        .getCurrentLocation()));
            else
                throw new JsonParseException(jp, "Missing pageTimings field in pages element");
        }
    }

    /**
     * Creates a new <code>HarPage</code> object from a database. Retrieves the
     * HarPage objects that corresponds to the specified page id.
     *
     * @param config the database configuration to use
     * @param pageId the page id to read
     * @throws SQLException if a database error occurs
     */
    public HarPage(HarDatabaseConfig config, long pageId) throws SQLException {
        Connection c = config.getConnection();
        String tableName = config.getTablePrefix() + "pages";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = c.prepareStatement("SELECT start_date,id,title,comment FROM "
                    + tableName + " WHERE page_id=?");
            ps.setLong(1, pageId);
            rs = ps.executeQuery();
            if (!rs.next())
                throw new SQLException("No HarPage for page id " + pageId
                        + " found in database");
            setStartedDateTime(rs.getTimestamp(1));
            setId(rs.getString(2));
            setTitle(rs.getString(3));
            setComment(rs.getString(4));
            pageTimings = new HarPageTimings(config, pageId);
            this.customFields.readCustomFieldsJDBC(config,
                    HarCustomFields.Type.HARPAGE, pageId);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignore) {
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (Exception ignore) {
            }
            config.closeConnection(c);
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
        g.writeStringField("startedDateTime",
                ISO8601DateFormatter.format(startedDateTime));
        g.writeStringField("id", id);
        g.writeStringField("title", title);
        pageTimings.writeHar(g);
        if (comment != null)
            g.writeStringField("comment", comment);
        this.customFields.writeHar(g);
        g.writeEndObject();
    }

    /**
     * Write this object in the given database referencing the specified logId.
     *
     * @param logId    the logId this object refers to
     * @param pagePs   PreparedStatement to write page data
     * @param timingPs PreparedStatement to write page timings data
     * @throws SQLException if a database access error occurs
     */
    public void writeJDBC(HarDatabaseConfig config, long logId,
                          PreparedStatement pagePs, PreparedStatement timingPs) throws SQLException {
        ResultSet rs = null;
        try {
            pagePs.setTimestamp(1, new Timestamp(startedDateTime.getTime()));
            pagePs.setString(2, id);
            pagePs.setString(3, title);
            if (comment == null)
                pagePs.setNull(4, Types.LONGVARCHAR);
            else
                pagePs.setString(4, comment);
            pagePs.setLong(5, logId);
            pagePs.executeUpdate();
            rs = pagePs.getGeneratedKeys();
            if (!rs.next())
                throw new SQLException(
                        "The database did not generate a key for an HarPage entry");
            long pageId = rs.getLong(1);
            this.customFields.writeCustomFieldsJDBC(config,
                    HarCustomFields.Type.HARPAGE, pageId, logId);
            pageTimings.writeJDBC(pageId, config, timingPs, logId);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * Returns the id value.
     *
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id value.
     *
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the startedDateTime value.
     *
     * @return Returns the startedDateTime.
     */
    public Date getStartedDateTime() {
        return startedDateTime;
    }

    /**
     * Sets the startedDateTime value.
     *
     * @param startedDateTime The startedDateTime to set.
     */
    public void setStartedDateTime(Date startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    /**
     * Returns the title value.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title value.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the pageTimings value.
     *
     * @return Returns the pageTimings.
     */
    public HarPageTimings getPageTimings() {
        return pageTimings;
    }

    /**
     * Sets the pageTimings value.
     *
     * @param pageTimings The pageTimings to set.
     */
    public void setPageTimings(HarPageTimings pageTimings) {
        this.pageTimings = pageTimings;
    }

    /**
     * Returns the comment value.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment value.
     *
     * @param comment The comment to set.
     */
    public void setComment(String comment) {
        this.comment = comment;
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

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "  { \"startedDateTime\": \""
                + ISO8601DateFormatter.format(startedDateTime) + "\", \"id\": \"" + id
                + "\", \"title\": \"" + title + "\", " + pageTimings
                + ", \"comment\": " + "\"" + comment + "\", " + customFields + "}\n";
    }

}
