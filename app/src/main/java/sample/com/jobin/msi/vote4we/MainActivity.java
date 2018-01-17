package sample.com.jobin.msi.vote4we;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String img_name;
    EditText editText;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Bitmap bitmap;
    Uri picuri;
    public static  final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);


    }

    @SuppressLint("NewApi")

    public void profileimageBrowse(View view) {
        img_name = editText.getText().toString();
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // only for gingerbread and newer versions
        }
        else {

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique

                return;
            }
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST){
                picuri = data.getData();
                String str_picuri = picuri.toString();


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picuri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data2 = baos.toByteArray();

                    StorageReference storageRef = storage.getReference();

                    StorageReference mountainsRef = storageRef.child(img_name + ".jpg");

                    UploadTask uploadTask = mountainsRef.putBytes(data2);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Toast.makeText(MainActivity.this, String.valueOf(100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount()), Toast.LENGTH_SHORT).show();
                            Log.d("tag",String.valueOf(100.0 * taskSnapshot.getBytesTransferred()));
//                            String[] separated = String.valueOf(progress).split(".");
//                            System.out.println("Upload is " + separated[0] + "% done");
//                            Toast.makeText(MainActivity.this, "Upload is " + separated[0] + "% done", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                displaying selected image to imageview
//                imageView.setImageBitmap(bitmap);
//                filepath = getPath(picuri);
//                imageView.setImageURI(picuri);

            }

//                displaying selected image to imageview
//                imageView.setImageBitmap(bitmap);
//                filepath = getPath(picuri);
//                imageView.setImageURI(picuri);
            }
        }


    public void navigate(View view) {
        Intent intent = new Intent(MainActivity.this,DownloadActivity.class);
        startActivity(intent);
    }
}
