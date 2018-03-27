package com.jvillalba.hashr.Fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jvillalba.hashr.R;
import com.jvillalba.hashr.utils.fileWork;
import com.jvillalba.hashr.utils.hashgen;

import java.io.File;

public class fragmentHashFromFile extends android.support.v4.app.Fragment {
    View rootView;
    Context mContext;
    ContentResolver cr;

    private static final String TAG = "Hashr";
    private EditText inputEdit;
    private EditText compareEdit;
    private TextView tvMatch = null;

    private String fragTitle;
    private String fragJobStr;
    private Uri fileURI;
    private int fragJob;
    private String fragJobExt;
    private boolean trimHash = true;

    final int ACTIVITY_CHOOSE_FILE = 1;

    private String lastFile;
    private int fileJob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hash_from_file, container, false);
        this.mContext = container.getContext();
        this.cr = mContext.getContentResolver();
        this.fragJob = getArguments().getInt("job");

        switch (this.fragJob) {
            case 1:
                fragTitle = "MD5 from file";
                fragJobStr = "MD5";
                fragJobExt = ".md5";
                break;
            case 2:
                fragTitle = "SHA-1 from file";
                fragJobStr = "SHA-1";
                fragJobExt = ".sha1";
                break;
            case 3:
                fragTitle = "SHA-256 from file";
                fragJobStr = "SHA-256";
                fragJobExt = ".sha256";
                break;
        }

        Log.d(TAG, fragTitle + " / " + fragJobStr);

        TextView tvHead = rootView.findViewById(R.id.tvHeader);
        tvHead.setText(fragTitle);
        Button btnGen = rootView.findViewById(R.id.btnGenerate);
        btnGen.setText("Generate ".concat(fragJobStr).concat(" hash"));
        Button btnComp = rootView.findViewById(R.id.btnCompare);
        btnComp.setText("Compare ".concat(fragJobStr).concat(" hashes"));
        CheckBox checkToFile = rootView.findViewById(R.id.checkToFile);
        checkToFile.setText("Save ".concat(fragJobStr).concat(" to ").concat(fragJobExt).concat(" file"));

        tvMatch = rootView.findViewById(R.id.tvCompare);
        inputEdit = rootView.findViewById(R.id.edInput);
        inputEdit.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvMatch.setText("");
                tvMatch.setVisibility(View.GONE);
            }
        });
        compareEdit = rootView.findViewById(R.id.edCompare);
        compareEdit.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvMatch.setText("");
                tvMatch.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void OnButtonGenerateClick()  {
        String hashed;
        EditText txtInput = getView().findViewById(R.id.edInput);
        String strInput = txtInput.getText().toString();
        String hashTextToFile;
        File file;

        if (strInput.length() < 1) {
            Toast.makeText(mContext, "Input File is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        file = new File(strInput);
        hashed = hashgen.generateHashFromFile(fileURI,cr,fragJobStr);

        TextView txtOutput = getView().findViewById(R.id.edOutput);
        txtOutput.setText(hashed);

        CheckBox toFileCheck = getView().findViewById(R.id.checkToFile);

        if (toFileCheck.isChecked()) {
            if (file == null) {
                return;
            }

            boolean esAvail;
            boolean esReadOnly;
            esAvail = fileWork.isExtStorageAvailable();
            esReadOnly = !fileWork.isExtStorageReadOnly();

            if (!esAvail) {
                Toast.makeText(mContext, "External Storage not available!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!esReadOnly) {
                Toast.makeText(mContext, "External Storage is readonly!", Toast.LENGTH_SHORT).show();
                return;
            }

            hashTextToFile = hashed + "  " + file.getName();
            String outputDir = fileWork.createExternalAppDir();
            if (!(outputDir.equals(""))) {
                fileWork.writeTextToFile(outputDir, file.getName() + fragJobExt, hashTextToFile);
                Toast.makeText(mContext, fragJobExt + " file written to disk.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void OnButtonCompareClick() {
        TextView edOutput = getView().findViewById(R.id.edOutput);
        String strOutput = edOutput.getText().toString();
        EditText edCompare = getView().findViewById(R.id.edCompare);
        String strCompare = edCompare.getText().toString();
        TextView tvMatch = getView().findViewById(R.id.tvCompare);

        if (strOutput.length() < 1 || strCompare.length() < 1) {
            Toast.makeText(mContext,"Output or Compare is Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        if (strOutput.equalsIgnoreCase(strCompare)) {
            tvMatch.setText("Match !");
            tvMatch.setTextColor(Color.WHITE);
        } else {
            tvMatch.setText("No match !");
            tvMatch.setTextColor(Color.WHITE);
        }

        tvMatch.setVisibility(View.VISIBLE);
    }

    public void OnButtonToClipboardClick() {
        TextView txtOutput = getView().findViewById(R.id.edOutput);
        String strOutput = txtOutput.getText().toString();

        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("Hash",strOutput);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(mContext, fragJobStr + " copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void OnButtonFromClipboardClick() {
        EditText txtOutput = getView().findViewById(R.id.edCompare);

        try {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item hash = clipboard.getPrimaryClip().getItemAt(0);
            String out = hash.getText().toString();
            if (trimHash)
                out = out.trim();

            txtOutput.setText(out);
        }
        catch (Exception e)
        {
            Toast.makeText(mContext,  "Clipboard is Empty", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void OnButtonChooseFileClick() {
        fileJob = 0; // open to generate
        FileManager();

    }

    public void OnButtonGetHashFromFileClick() {
        fileJob = 1; // open to generate
        FileManager();

    }

    private void FileManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile.setType("*/*");
            Intent intent = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
        } else {
            Intent chooseFile;
            Intent intent;
            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("file/*");
            intent = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_CHOOSE_FILE: {
                if (resultCode == Activity.RESULT_OK){
                    fileURI = data.getData();
                    String filePath = fileWork.getFileNameWithPathByUri(cr,fileURI);
                    lastFile = filePath;

                    switch (fileJob) {
                        case 0:
                            EditText txtInput = getView().findViewById(R.id.edInput);
                            txtInput.setText(filePath);
                            break;
                        case 1:
                            getHashFromFile(fileURI,cr);
                            break;
                    }
                }
            }
        }
    }

    private String getFirstWord(String text) {
        if (text.indexOf(' ') > -1) {
            return text.substring(0, text.indexOf(' '));
        } else {
            return text;
        }
    }

    private void getHashFromFile(Uri fileURI,ContentResolver cr) {
        String firstLine;
        fileWork fw = new fileWork();

        firstLine = fw.getFirstLineFromFile(fileURI,cr);

        if (firstLine == null) {
            Toast.makeText(mContext, "No hash from file found", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView edComp = getView().findViewById(R.id.edCompare);
        edComp.setText(getFirstWord(firstLine));
    }


}
