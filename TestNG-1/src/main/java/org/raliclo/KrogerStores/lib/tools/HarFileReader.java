/**
 * BenchLab: Internet Scale Benchmarking.
 * Copyright (C) 2010-2011 Emmanuel Cecchet.
 * Contact: cecchet@cs.umass.edu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Initial developer(s): karlholl (sf.net)
 * Contributor(s): Emmanuel Cecchet. , Ralic Lo
 */
package org.raliclo.KrogerStores.lib.tools;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.raliclo.KrogerStores.lib.HarLog;
import org.raliclo.KrogerStores.lib.HarWarning;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * HarFileReader reads a HAR file into a HarLog object (building all the
 * necessary hierarchy of objects)
 *
 * @author <a href="mailto:cecchet@cs.umass.edu>Emmanuel Cecchet</a>
 * @version 1.0
 */
public class HarFileReader {

    /**
     * Read the given file and build the corresponding HarLog object hierarchy in
     * memory.
     *
     * @param jp       The Json parser
     * @param warnings null if parser should fail on first error, pointer to
     *                 warning list if warnings can be issued for missing fields
     * @return HarLog representation of the file
     * @throws JsonParseException if a parsing error occurs
     * @throws IOException        if an IO error occurs
     */
    public HarLog readHarFile(JsonParser jp, List<HarWarning> warnings) throws JsonParseException, IOException {
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(jp, "File does not start with {");
        }

        try {
            HarLog log = new HarLog(jp, warnings);
            return log;
        } finally {
            jp.close(); // ensure resources get cleaned up timely and properly
        }
    }

    /**
     * Read the given file and build the corresponding HarLog object hierarchy in
     * memory.
     *
     * @param file The file to read
     * @return HarLog representation of the file
     * @throws JsonParseException if a parsing error occurs
     * @throws IOException        if an IO error occurs
     */
    public HarLog readHarFile(File file) throws JsonParseException, IOException {
        return readHarFile(file, null);
    }

    /**
     * Read the given file and build the corresponding HarLog object hierarchy in
     * memory.
     *
     * @param file     The file to read
     * @param warnings null if parser should fail on first error, pointer to
     *                 warning list if warnings can be issued for missing fields
     * @return HarLog representation of the file
     * @throws JsonParseException if a parsing error occurs
     * @throws IOException        if an IO error occurs
     */
    public HarLog readHarFile(File file, List<HarWarning> warnings) throws JsonParseException, IOException {
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(file);
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(jp, "File does not start with {");
        }

        try {
            HarLog log = new HarLog(jp, warnings);
            return log;
        } finally {
            jp.close(); // ensure resources get cleaned up timely and properly
        }
    }

    /**
     * Read the given InputStream and build the corresponding HarLog object
     * hierarchy in memory.
     *
     * @param stream   The stream to read from
     * @param warnings null if parser should fail on first error, pointer to
     *                 warning list if warnings can be issued for missing fields
     * @return HarLog representation of the file
     * @throws JsonParseException if a parsing error occurs
     * @throws IOException        if an IO error occurs
     */
    public HarLog readHarFile(InputStream stream, List<HarWarning> warnings) throws JsonParseException, IOException {
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createParser(stream);
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(jp, "File does not start with {");
        }

        try {
            HarLog log = new HarLog(jp, warnings);
            return log;
        } finally {
            jp.close(); // ensure resources get cleaned up timely and properly
        }
    }

}
