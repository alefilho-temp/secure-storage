package com.safe.storage.security;

import com.safe.storage.common.Login;
import com.safe.storage.models.SafePassword;

public class SafePasswordCode implements Code<SafePassword> {

    @Override
    public SafePassword encode(SafePassword object) {
        object.setName(Crypto.encryptString(Login.getUser().getPassword(), object.getName()).get());
        object.setAccessUrl(Crypto.encryptString(Login.getUser().getPassword(), object.getAccessUrl()).get());
        object.setImagePath(Crypto.encryptString(Login.getUser().getPassword(), object.getImagePath()).get());
        object.setNote(Crypto.encryptString(Login.getUser().getPassword(), object.getNote()).get());
        object.setUsername(Crypto.encryptString(Login.getUser().getPassword(), object.getUsername()).get());
        object.setPassword(Crypto.encryptString(Login.getUser().getPassword(), object.getPassword()).get());
        
        return object;
    }

    @Override
    public SafePassword decode(SafePassword object) {
        object.setName(Crypto.decryptString(Login.getUser().getPassword(), object.getName()).get());
        object.setAccessUrl(Crypto.decryptString(Login.getUser().getPassword(), object.getAccessUrl()).get());
        object.setImagePath(Crypto.decryptString(Login.getUser().getPassword(), object.getImagePath()).get());
        object.setNote(Crypto.decryptString(Login.getUser().getPassword(), object.getNote()).get());
        object.setUsername(Crypto.decryptString(Login.getUser().getPassword(), object.getUsername()).get());
        object.setPassword(Crypto.decryptString(Login.getUser().getPassword(), object.getPassword()).get());
        
        return object;
    }
    
}
