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

package org.apache.axiom.util.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.axiom.blob.Blob;
import org.apache.axiom.ext.activation.SizeAwareDataSource;

/**
 * Data source backed by a {@link Blob}.
 */
// TODO(AXIOM-506): this should not be public
public class BlobDataSource implements SizeAwareDataSource {
    private final Blob blob;
    private final String contentType;
    
    public BlobDataSource(Blob blob, String contentType) {
        this.blob = blob;
        this.contentType = contentType;
    }

    Blob getBlob() {
        return blob;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return blob.getInputStream();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getSize() {
        return blob.getSize();
    }
}
