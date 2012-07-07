/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.axiom.om.impl.common;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.OMXMLStreamReader;
import org.apache.axiom.om.OMXMLStreamReaderConfiguration;
import org.apache.axiom.om.impl.OMContainerEx;
import org.apache.axiom.om.impl.OMNodeEx;
import org.apache.axiom.om.impl.builder.OMFactoryEx;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.OMXMLStreamReaderValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class OMContainerHelper {
    private static final Log log = LogFactory.getLog(OMContainerHelper.class);
    
    private static final OMXMLStreamReaderConfiguration defaultReaderConfiguration = new OMXMLStreamReaderConfiguration();
    
    private OMContainerHelper() {}
    
    public static XMLStreamReader getXMLStreamReader(OMContainer container, boolean cache) {
        return getXMLStreamReader(container, cache, defaultReaderConfiguration);
    }
    
    public static XMLStreamReader getXMLStreamReader(OMContainer container, boolean cache, OMXMLStreamReaderConfiguration configuration) {
        OMXMLParserWrapper builder = container.getBuilder();
        if (builder != null && builder instanceof StAXOMBuilder) {
            if (!container.isComplete()) {
                if (((StAXOMBuilder) builder).isLookahead()) {
                    container.buildNext();
                }
            }
        }
        
        // The om tree was built by hand and is already complete
        OMXMLStreamReader reader;
        boolean done = container.isComplete();
        if ((builder == null) && done) {
            reader = new OMStAXWrapper(null, container, false, configuration.isPreserveNamespaceContext());
        } else {
            if ((builder == null) && !cache) {
                throw new UnsupportedOperationException(
                "This element was not created in a manner to be switched");
            }
            if (builder != null && builder.isCompleted() && !cache && !done) {
                throw new UnsupportedOperationException(
                "The parser is already consumed!");
            }
            reader = new OMStAXWrapper(builder, container, cache, configuration.isPreserveNamespaceContext());
        }
        
        if (configuration.isNamespaceURIInterning()) {
            reader = new NamespaceURIInterningXMLStreamReaderWrapper(reader);
        }
        
        // If debug is enabled, wrap the OMXMLStreamReader in a validator.
        // The validator will check for mismatched events to help determine if the OMStAXWrapper
        // is functioning correctly.  All problems are reported as debug.log messages
        
        if (log.isDebugEnabled()) {
            reader = 
                new OMXMLStreamReaderValidator(reader, // delegate to actual reader
                     false); // log problems (true will cause exceptions to be thrown)
        }
        
        return reader;
    }
    
    public static void addChild(OMContainerEx container, OMNode omNode, boolean fromBuilder) {
        OMNodeEx child;
        if (fromBuilder) {
            // If the new child was provided by the builder, we know that it was created by
            // the same factory
            child = (OMNodeEx)omNode;
        } else {
            // Careful here: if the child was created by another Axiom implementation, it doesn't
            // necessarily implement OMNodeEx
            if (omNode.getOMFactory().getMetaFactory() == container.getOMFactory().getMetaFactory()) {
                child = (OMNodeEx)omNode;
            } else {
                child = (OMNodeEx)((OMFactoryEx)container.getOMFactory()).importNode(omNode);
            }
            if (!container.isComplete()) {
                container.build();
            }
            if (child.getParent() == container && child == container.getLastKnownOMChild()) {
                // The child is already the last node. 
                // We don't need to detach and re-add it.
                return;
            }
        }
        if (child.getParent() != null) {
            child.detach();
        }
        
        child.setParent(container);

        if (container.getFirstOMChildIfAvailable() == null) {
            container.setFirstChild(child);
        } else {
            OMNode lastChild = container.getLastKnownOMChild();
            child.setPreviousOMSibling(lastChild);
            ((OMNodeEx)lastChild).setNextOMSibling(child);
        }
        container.setLastChild(child);

        // For a normal OMNode, the incomplete status is
        // propogated up the tree.  
        // However, a OMSourcedElement is self-contained 
        // (it has an independent parser source).
        // So only propogate the incomplete setting if this
        // is a normal OMNode
        if (!child.isComplete() && 
            !(child instanceof OMSourcedElement)) {
            container.setComplete(false);
        }
    }
}
