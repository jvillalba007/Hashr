package com.jvillalba.hashr.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *   Hashr - generate and compare hashes like MD5 or SHA-1 on Android.
 *   Copyright (C) 2015  Christian Handorf - kodejak at gmail dot com
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see http://www.gnu.org/licenses
 */

public class fileWork {
    private String TAG = "Hashr";

    public static void writeTextToFile(String path, String fileName, String text) {
        try {
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
            fos.write(text.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isExtStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public static boolean isExtStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static String createExternalAppDir() {
        File f = new File(android.os.Environment.getExternalStorageDirectory(), File.separator + "Hashr" + "/");
        if (f.exists()) {
            return f.toString();
        }

        f.mkdirs();

        if (f.exists()) {
            return f.toString();
        }
        return "";
    }

    public static String getFileNameWithPathByUri(ContentResolver cr, Uri fileURI)
    {
        String result = null;
        if (fileURI.getScheme().equals("content")) {
            Cursor cursor = cr.query(fileURI, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = fileURI.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String getFirstLineFromFile(Uri fileURI,ContentResolver cr) {
        String oneLine = null;
        InputStream is;


        StringBuilder text = new StringBuilder();

        try {
            is = cr.openInputStream(fileURI);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                Log.d(TAG, "Read Line: " + line);
            }
            br.close();
        }
        catch (Exception e) {
        }

        // we only need the first line
        Scanner scan = new Scanner(text.toString()); // I have named your StringBuilder object sb
        if (scan.hasNextLine() ){
            oneLine = scan.nextLine();
            Log.d(TAG, "Extract Line: " + oneLine);
        }

        return oneLine;
    }

}