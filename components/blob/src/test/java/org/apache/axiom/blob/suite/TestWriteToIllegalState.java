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
package org.apache.axiom.blob.suite;

import static org.junit.Assert.assertThrows;

import org.apache.axiom.blob.WritableBlob;
import org.apache.axiom.blob.WritableBlobFactory;
import org.apache.commons.io.output.NullOutputStream;

public class TestWriteToIllegalState extends WritableBlobTestCase {
    public TestWriteToIllegalState(WritableBlobFactory<?> factory, State state) {
        super(factory, state);
        state.addTestParameters(this);
    }

    @Override
    protected void runTest(WritableBlob blob) throws Throwable {
        assertThrows(IllegalStateException.class, () -> blob.writeTo(NullOutputStream.NULL_OUTPUT_STREAM));
    }
}
