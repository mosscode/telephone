/**
 * Copyright (C) 2013, Moss Computing Inc.
 *
 * This file is part of telephone.
 *
 * telephone is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * telephone is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with telephone; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package com.moss.telephone.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class JAXBHelper {
	private JAXBContext context;
	
	public JAXBHelper(JAXBContext context) {
		super();
		this.context = context;
	}
	public void writeToFile(String text, File file){
		try {
			Writer writer = new FileWriter(file);
			writer.write(text);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object readFromFile(File path) throws Exception {
		return context.createUnmarshaller().unmarshal(path);
	}
	public Object readFromXmlString(String xml) throws Exception{
		
		return context.createUnmarshaller().unmarshal(new ByteArrayInputStream(xml.getBytes()));
	}
	
	public String writeToXmlString(Object o) throws Exception{
		Marshaller m = context.createMarshaller();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		m.marshal(o,out);
		
		ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
		beautify(new ByteArrayInputStream(out.toByteArray()), xmlStream);
		String xml = new String(xmlStream.toByteArray());
		return xml;
	}
	
	public void beautify(InputStream in, OutputStream out) throws Exception{
		Source xmlSource = new StreamSource(in);
        Source xsltSource = new StreamSource(getClass().getResourceAsStream("/prettyprint.xsl"));

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact =
                TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);

        trans.transform(xmlSource, new StreamResult(out));
	}
}
