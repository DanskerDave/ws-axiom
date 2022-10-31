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
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.apache.axiom.blob.Blob;

/**
 * Contains utility methods to work with {@link DataHandler} objects.
 */
public final class DataHandlerUtils {
    private DataHandlerUtils() {}

    /**
     * Check if the given {@link DataHandler} will produce a byte stream that is longer than a given
     * limit. It will first attempt to determine the size using
     * {@link DataSourceUtils#getSize(DataSource)}. If that fails, it will use
     * {@link DataHandler#writeTo(OutputStream)} to determine if the size is larger than the limit.
     * 
     * @param dh
     *            the {@link DataHandler} to check
     * @param limit
     *            the limit
     * @return {@code true} if the size is larger than {@code limit}, {@code false} otherwise
     * @throws IOException
     *             if {@link DataHandler#writeTo(OutputStream)} produced an unexpected exception
     */
    public static boolean isLargerThan(DataHandler dh, long limit) throws IOException {
        long size = DataSourceUtils.getSize(dh.getDataSource());
        if (size != -1) {
            return size > limit;
        } else {
            // In all other cases, we prefer DataHandler#writeTo over DataSource#getInputStream.
            // The reason is that if the DataHandler was constructed from an Object rather than
            // a DataSource, a call to DataSource#getInputStream() will start a new thread and
            // return a PipedInputStream. This is so for Geronimo's as well as Sun's JAF
            // implementation. The reason is that DataContentHandler only has a writeTo and no
            // getInputStream method. Obviously starting a new thread just to check the size of
            // the data is an overhead that we should avoid.
            try {
                dh.writeTo(new CountingOutputStream(limit));
                return false;
            } catch (SizeLimitExceededException ex) {
                return true;
            }
        }
    }

    /**
     * Get a {@link DataHandler} for the given {@link Blob}. If the blob was obtained from {@link
     * #toBlob(DataHandler)}, the original {@link DataHandler} is returned.
     * 
     * @param blob the {@link Blob} to convert
     * @return a {@link DataHandler} representing the {@link Blob}
     */
    public static DataHandler toDataHandler(Blob blob) {
        if (blob instanceof DataHandlerBlob) {
            return ((DataHandlerBlob)blob).getDataHandler();
        }
        return new DataHandler(new BlobDataSource(blob, "application/octet-stream"));
    }

    /**
     * Get a {@link Blob} for the given {@link DataHandler}. If the {@link DataHandler} was obtained from {@link
     * #toDataHandler(Blob)}, the original {@link Blob} is returned.
     * 
     * @param dh the {@link DataHandler} to convert
     * @return a {@link Blob} representing the {@link DataHandler}
     */
    public static Blob toBlob(DataHandler dh) {
        DataSource ds = dh.getDataSource();
        if (ds instanceof BlobDataSource) {
            return ((BlobDataSource)ds).getBlob();
        }
        return new DataHandlerBlob(dh);
    }
}
