package com.jvillalba.hashr.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashgen {
    private static final String TAG = "Hashr";

    public static String generateHashFromFile(Uri fileURI, ContentResolver cr, String type) {
        MessageDigest digest;
        String output;

        try {
                digest = MessageDigest.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = cr.openInputStream(fileURI);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[10240];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
            }
            byte[] hash;

            hash = digest.digest();
                BigInteger bigInt = new BigInteger(1, hash);
                output = bigInt.toString(16);
                // Fill to 32 chars
                output = String.format("%32s", output).replace(' ', '0');


            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for Hash", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing Hash input stream", e);
            }
        }
    }

}
