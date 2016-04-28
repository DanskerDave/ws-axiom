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
package org.apache.axiom.attachments;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;

import org.apache.axiom.mime.ContentType;

final class MIMEMessageAdapter extends AttachmentsDelegate {
    private final MIMEMessage message;

    MIMEMessageAdapter(MIMEMessage message) {
        this.message = message;
    }

    @Override
    ContentType getContentType() {
        return message.getContentType();
    }

    @Override
    DataHandler getDataHandler(String contentID) {
        return message.getDataHandler(contentID);
    }

    @Override
    void addDataHandler(String contentID, DataHandler dataHandler) {
        message.addDataHandler(contentID, dataHandler);
    }

    @Override
    void removeDataHandler(String blobContentID) {
        message.removeDataHandler(blobContentID);
    }

    @Override
    InputStream getRootPartInputStream(boolean preserve) {
        return message.getRootPartInputStream(preserve);
    }

    @Override
    String getRootPartContentID() {
        return message.getRootPartContentID();
    }

    @Override
    String getRootPartContentType() {
        return message.getRootPartContentType();
    }

    @Override
    IncomingAttachmentStreams getIncomingAttachmentStreams() {
        return message.getIncomingAttachmentStreams();
    }

    @Override
    Set<String> getContentIDs(boolean fetchAll) {
        return message.getContentIDs(fetchAll);
    }

    @Override
    Map<String, DataHandler> getMap() {
        return message.getMap();
    }

    @Override
    long getContentLength() throws IOException {
        return message.getContentLength();
    }
}