package com.college.collegeconnect.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.college.collegeconnect.R;
import com.college.collegeconnect.datamodels.Constants;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.college.collegeconnect.datamodels.Upload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadNotes extends AppCompatActivity {

    final static int PICK_PDF_CODE = 2342;
    private Intent Data = null;
    private EditText fileName, author;
    Intent receiverdIntent;
    TextView tv8, tv_title;
    ImageView imageView;
    String receivedAction;
    String receivedType;
    Uri recievedUri;
    //these are the views
    TextView textViewStatus;
    //    EditText editTextFilename,author;
    ProgressBar progressBar;
    Button upload;
    Spinner semester, branch, course, unit;
    //the firebase objects for storage and database
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        tv8 = findViewById(R.id.textView11);
        imageView = findViewById(R.id.imageView11);

        //getting firebase objects
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_title = findViewById(R.id.tvtitle);
        tv_title.setText("Upload Notes");
        if (SaveSharedPreference.getCheckedItem(this) == 0)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if (SaveSharedPreference.getCheckedItem(this) == 1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if (SaveSharedPreference.getCheckedItem(this) == 2)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //getting the views
        textViewStatus = findViewById(R.id.textViewStatus);
        semester = findViewById(R.id.spinnerSem);
        branch = findViewById(R.id.spinnerBranch);
        course = findViewById(R.id.spinnerCourse);
        unit = findViewById(R.id.spinnerUnit);
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (semester.getSelectedItem().toString().equals("Syllabus")) {
                    unit.setVisibility(View.INVISIBLE);
                    tv8.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    unit.setSelection(0);
                } else {
                    unit.setVisibility(View.VISIBLE);
                    tv8.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        editTextFilename = findViewById(R.id.FileName);
        progressBar = findViewById(R.id.UploadNotesProgressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        upload = findViewById(R.id.selectNotes);
        if (onSharedIntent()) {
            if (recievedUri != null) {
                Log.i("Upload Notes", "onCreate: " + recievedUri);
                UploadNotes.this.Data = receiverdIntent;
                UploadNotes.this.Data.setData(recievedUri);
                upload.setText("Upload");
            }
        }
//        author=findViewById(R.id.author);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();

            }
        });

    }

    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(UploadNotes.this, new String[]{"Manifest.permission.READ_EXTERNAL_STORAGE"},100);
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            return;
//        }
        if (UploadNotes.this.Data == null) {
            if (ContextCompat.checkSelfPermission(UploadNotes.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                // Requesting the permission
                ActivityCompat.requestPermissions(UploadNotes.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            } else {

                //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
            }
        } else {
            StringBuilder filenaam = new StringBuilder();
            String str = recievedUri.getLastPathSegment();
            int slash = -1;
            if (str.contains("/")) {
                slash = str.indexOf("/");
                filenaam.append(str.substring(slash, str.length() - 1));
            }
            String str1 = str.substring(slash + 1, str.length() - 1);
            if (str1.contains(".")) {
                int dot = str1.indexOf(".");
                filenaam.append(str1.substring(0, dot));
            }
            alertDialog(filenaam.toString());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPDF();

        } else {
            Toast.makeText(UploadNotes.this,
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            StringBuilder filenaam = new StringBuilder();
            if (data.getData() != null) {
                String str = data.getData().getLastPathSegment();
                int slash = -1;
                if (str.contains("/")) {
                    slash = str.indexOf("/");
                    filenaam.append(str.substring(slash, str.length() - 1));
                }
                String str1 = str.substring(slash + 1, str.length() - 1);
                if (str1.contains(".")) {
                    int dot = str1.indexOf(".");
                    filenaam.append(str1.substring(0, dot));
                }

//                    editTextFilename.setText(str);
                //uploading the file
                UploadNotes.this.Data = data;
//                NotesDialog notesDialog = new NotesDialog();
//                notesDialog.show(getSupportFragmentManager(),"Notes Dialog");
                alertDialog(filenaam.toString());


//                findViewById(R.id.uploadNotes).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        uploadFile(data.getData());
//                    }
//                });

            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void alertDialog(String filenaam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNotes.this);

        LayoutInflater inflater = getLayoutInflater().from(UploadNotes.this);
        final View view = inflater.inflate(R.layout.layout_dialog_notes, null);


        builder.setView(view)
                .setTitle("Set File Name")
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UploadNotes.this.Data = null;
                    }
                });
        fileName = view.findViewById(R.id.fileName);
        author = view.findViewById(R.id.authorName);


        final AlertDialog dialog = builder.create();
        dialog.show();
        fileName.setText(filenaam);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        boolean isError = false;
                String file = fileName.getText().toString();
                String authorName = author.getText().toString();
//                        isError=true;
                if (file.isEmpty() && authorName.isEmpty()) {
                    fileName.setError("Filename cannot be empty");
                    author.setError("Author name cannot be empty");
                } else if (file.isEmpty()) {
//                            isError = true;
                    fileName.setError("Filename cannot be empty");
                } else if (authorName.isEmpty()) {
//                            isError = true;
                    author.setError("Author name cannot be empty");
                } else {
                    applyTexts(file, authorName);
                    UploadNotes.this.Data = null;
                    dialog.dismiss();
                }
            }
        });
        fileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fileName.setError(null);
            }
        });
        author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                author.setError(null);
            }
        });
    }


    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
    private void uploadFile(Uri data, final String filename, final String authorname) {

        progressBar.setVisibility(View.VISIBLE);
        StorageReference sRef = mStorageReference.child(Constants.STORAGE_PATH_UPLOADS + course.getSelectedItem().toString() + "/" + branch.getSelectedItem().toString() + "/" + semester.getSelectedItem().toString() + "/" + unit.getSelectedItem().toString() + "/" + System.currentTimeMillis());
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.GONE);
//                        final Uri downloadi;
                        textViewStatus.setText("File Uploaded Successfully");
                        Task<Uri> downlaoduri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Upload upload = new Upload(filename,
                                        course.getSelectedItem().toString(),
                                        semester.getSelectedItem().toString(),
                                        branch.getSelectedItem().toString(),
                                        unit.getSelectedItem().toString(),
                                        authorname, 0, uri.toString(),
                                        System.currentTimeMillis(),
                                        SaveSharedPreference.getUserName(UploadNotes.this), new ArrayList() {
                                });
                                mDatabaseReference.child(upload.getTimestamp() + "").setValue(upload);
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setProgress(0);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        textViewStatus.setText((int) progress + "% Uploading...");
                        textViewStatus.setText("");
                        progressBar.setProgress((int) progress);

                    }
                });

    }

    public void applyTexts(String filename, String authorname) {

        if (UploadNotes.this.Data.getData() == null) {
            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
        } else
            uploadFile(UploadNotes.this.Data.getData(), filename, authorname);
    }

    public boolean onSharedIntent() {
        receiverdIntent = getIntent();
        Bundle bundle = receiverdIntent.getExtras();
        if (bundle != null) {
            receivedAction = receiverdIntent.getAction();
            receivedType = receiverdIntent.getType();
            if (receiverdIntent != null) {
                Log.i("Upload Notes", "onSharedIntent: " + receivedType + "::::" + receivedAction);
                if (receivedType.contains("pdf")) {
                    recievedUri = receiverdIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if (recievedUri != null) {
                        Log.i("Upload Notes", "onSharedIntent: " + recievedUri.toString());
                        return true;
                    }

                }
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

