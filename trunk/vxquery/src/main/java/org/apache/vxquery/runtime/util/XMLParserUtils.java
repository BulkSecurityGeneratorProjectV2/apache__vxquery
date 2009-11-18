/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.vxquery.runtime.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import org.apache.vxquery.datamodel.NameCache;
import org.apache.vxquery.datamodel.NodeConstructingEventAcceptor;
import org.apache.vxquery.datamodel.XDMNode;
import org.apache.vxquery.exceptions.ErrorCode;
import org.apache.vxquery.exceptions.SystemException;
import org.apache.vxquery.runtime.RuntimeControlBlock;

public class XMLParserUtils {
    public static XDMNode parseFile(RuntimeControlBlock rcb, File file) throws SystemException {
        try {
            InputSource isrc = new InputSource(new FileInputStream(file));
            return parseInputSource(rcb, isrc);
        } catch (FileNotFoundException e) {
            throw new SystemException(ErrorCode.FODC0002, e, file);
        }
    }

    public static XDMNode parseInputSource(RuntimeControlBlock rcb, InputSource in) throws SystemException {
        final NameCache nameCache = rcb.getNameCache();
        NodeConstructingEventAcceptor acceptor = rcb.getNodeFactory().createDocumentConstructor();
        acceptor.open();

        XMLReader parser;
        try {
            parser = XMLReaderFactory.createXMLReader();
            ParseHandler handler = new ParseHandler(nameCache, acceptor);
            parser.setContentHandler(handler);
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            parser.parse(in);
            acceptor.close();
            return acceptor.getConstructedNode();
        } catch (Exception e) {
            throw new SystemException(ErrorCode.FODC0002, e, in.getSystemId());
        }
    }

    private static class ParseHandler implements ContentHandler, LexicalHandler {
        private NameCache nameCache;
        private NodeConstructingEventAcceptor acceptor;

        public ParseHandler(NameCache nameCache, NodeConstructingEventAcceptor acceptor) {
            this.nameCache = nameCache;
            this.acceptor = acceptor;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            try {
                acceptor.text(String.valueOf(ch, start, length));
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void endDocument() throws SAXException {
            try {
                acceptor.endDocument();
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            try {
                acceptor.endElement();
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            try {
                acceptor.pi(nameCache, nameCache.intern("", "", target), data);
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void setDocumentLocator(Locator locator) {
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
        }

        @Override
        public void startDocument() throws SAXException {
            try {
                acceptor.startDocument();
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
            int idx = name.indexOf(':');
            String prefix = idx < 0 ? "" : name.substring(0, idx);
            try {
                acceptor.startElement(nameCache, nameCache.intern(prefix, uri, localName));
                final int nAttrs = atts.getLength();
                for (int i = 0; i < nAttrs; ++i) {
                    String aName = atts.getQName(i);
                    int aIdx = aName.indexOf(':');
                    String aPrefix = aIdx < 0 ? "" : aName.substring(0, aIdx);
                    String aLocalName = atts.getLocalName(i);
                    String aUri = atts.getURI(i);
                    String aValue = atts.getValue(i);
                    acceptor.attribute(nameCache, nameCache.intern(aPrefix, aUri, aLocalName), aValue);
                }
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }

        @Override
        public void comment(char[] ch, int start, int length) throws SAXException {
            try {
                acceptor.comment(String.valueOf(ch, start, length));
            } catch (SystemException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        @Override
        public void endCDATA() throws SAXException {
        }

        @Override
        public void endDTD() throws SAXException {
        }

        @Override
        public void endEntity(String name) throws SAXException {
        }

        @Override
        public void startCDATA() throws SAXException {
        }

        @Override
        public void startDTD(String name, String publicId, String systemId) throws SAXException {
        }

        @Override
        public void startEntity(String name) throws SAXException {
        }
    }
}