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

import static com.google.common.truth.Truth.assertThat;

import javax.activation.DataHandler;

import org.apache.axiom.blob.Blobs;
import org.apache.axiom.mime.ContentType;
import org.apache.axiom.mime.MediaType;
import org.junit.Test;

public class DataHandlerContentTypeProviderTest {
    @Test
    public void testNotDataHandler() {
        assertThat(DataHandlerContentTypeProvider.INSTANCE
                .getContentType(Blobs.createBlob(new byte[10]))).isNull();
    }

    @Test
    public void testDataHandlerWithoutContentType() {
        DataHandler dh = new DataHandler(new BlobDataSource(Blobs.createBlob(new byte[10]), null));
        assertThat(
                DataHandlerContentTypeProvider.INSTANCE.getContentType(DataHandlerUtils.toBlob(dh)))
                        .isNull();
    }

    @Test
    public void testDataHandlerWithContentType() {
        DataHandler dh = new DataHandler("test", "text/plain");
        ContentType contentType = DataHandlerContentTypeProvider.INSTANCE
                .getContentType(DataHandlerUtils.toBlob(dh));
        assertThat(contentType).isNotNull();
        assertThat(contentType.getMediaType()).isEqualTo(MediaType.TEXT_PLAIN);
    }
}
