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
package org.apache.axiom.soap.impl.common;

import org.apache.axiom.core.CoreNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.impl.common.AxiomElement;
import org.apache.axiom.om.impl.common.serializer.push.OutputException;
import org.apache.axiom.om.impl.common.serializer.push.Serializer;

public aspect AxiomSOAPMessageSupport {
    public Class<? extends CoreNode> AxiomSOAPMessage.coreGetNodeClass() {
        return AxiomSOAPMessage.class;
    }

    // TODO: this violates OO design principles and should disappear in a future Axiom version
    public final void AxiomSOAPMessage.internalSerialize(Serializer serializer, OMOutputFormat format,
            boolean cache, boolean includeXMLDeclaration) throws OutputException {
        ((AxiomElement)getOMDocumentElement()).internalSerialize(serializer, format, cache);
    }
}