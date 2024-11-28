package com.safe.storage.security;

import com.safe.storage.common.Login;
import com.safe.storage.models.SafeFile;

public class SafeFileCode implements Code<SafeFile> {

    @Override
    public SafeFile encode(SafeFile object) {
        object.setName(Crypto.encryptString(Login.getUser().getPassword(), object.getName()).get());
        object.setData(Crypto.encryptBytes(Login.getUser().getPassword(), object.getData()).get());
        object.setOriginalName(Crypto.encryptString(Login.getUser().getPassword(), object.getOriginalName()).get());
        object.setNote(Crypto.encryptString(Login.getUser().getPassword(), object.getNote()).get());
        object.setSize(Crypto.obfuscate(object.getSize()));

        return object;
    }

    @Override
    public SafeFile decode(SafeFile object) {
        object.setName(Crypto.decryptString(Login.getUser().getPassword(), object.getName()).get());
        object.setData(Crypto.decryptBytes(Login.getUser().getPassword(), object.getData()).get());
        object.setOriginalName(Crypto.decryptString(Login.getUser().getPassword(), object.getOriginalName()).get());
        object.setNote(Crypto.decryptString(Login.getUser().getPassword(), object.getNote()).get());
        object.setSize(Crypto.deobfuscate(object.getSize()));

        return object;
    }
    
}
