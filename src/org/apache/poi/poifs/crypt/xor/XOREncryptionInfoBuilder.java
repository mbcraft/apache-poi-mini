/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.poifs.crypt.xor;

import org.apache.poi.poifs.crypt.*;
import org.apache.poi.util.LittleEndianInput;

import java.io.IOException;

public class XOREncryptionInfoBuilder implements EncryptionInfoBuilder {

    public XOREncryptionInfoBuilder() {
    }

    @Override
    public void initialize(EncryptionInfo info, LittleEndianInput dis)
    throws IOException {
        info.setHeader(new XOREncryptionHeader());
        info.setVerifier(new XOREncryptionVerifier(dis));
        Decryptor dec = new XORDecryptor();
        dec.setEncryptionInfo(info);
        info.setDecryptor(dec);
        Encryptor enc = new XOREncryptor();
        enc.setEncryptionInfo(info);
        info.setEncryptor(enc);
    }

    @Override
    public void initialize(EncryptionInfo info,
        CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm,
        int keyBits, int blockSize, ChainingMode chainingMode) {
        info.setHeader(new XOREncryptionHeader());
        info.setVerifier(new XOREncryptionVerifier());
        Decryptor dec = new XORDecryptor();
        dec.setEncryptionInfo(info);
        info.setDecryptor(dec);
        Encryptor enc = new XOREncryptor();
        enc.setEncryptionInfo(info);
        info.setEncryptor(enc);
    }
}
