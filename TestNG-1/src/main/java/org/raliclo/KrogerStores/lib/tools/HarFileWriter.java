/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.raliclo.KrogerStores.lib.tools;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.raliclo.KrogerStores.lib.HarLog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * HarFileWriter writes a HarLog object into a HAR file.
 *
 * @author <a href="mailto:cecchet@cs.umass.edu>Emmanuel Cecchet</a>
 * @version 1.0
 */
public class HarFileWriter {
    /**
     * Write a HarLog object into a file.
     *
     * @param log       the HarLog to write to a file
     * @param generator the Json generator
     * @throws IOException if an error occurs writing the file
     */
    public void writeHarFile(HarLog log, JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        log.writeHar(generator);
        generator.writeEndObject();
        generator.close();
    }

    /**
     * Write a HarLog object into a file.
     *
     * @param log  the HarLog to write to a file
     * @param file the file to write the HAR to
     * @throws IOException if an error occurs writing the file
     */
    public void writeHarFile(HarLog log, File file) throws IOException {
        JsonGenerator g = new JsonFactory().createGenerator(file, JsonEncoding.UTF8);
        g.useDefaultPrettyPrinter();

        g.writeStartObject();
        log.writeHar(g);
        g.writeEndObject();
        g.close();
    }

    /**
     * Write a HarLog object into an OutputStream.
     *
     * @param log the HarLog to write to an OutputStream
     * @param os  the OutputStream to write the HAR to
     * @throws IOException if an error occurs writing into the OutputStream
     */
    public void writeHarFile(HarLog log, OutputStream os) throws IOException {
        JsonFactory f = new JsonFactory();
        JsonGenerator g = f.createGenerator(os, JsonEncoding.UTF8);
        g.useDefaultPrettyPrinter();

        g.writeStartObject();
        log.writeHar(g);
        g.writeEndObject();
        g.close();
    }
}
