package oxforddictionary.codeitstudio.com.dictionary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;

public class ocr_reader extends AppCompatActivity {

    private ImageView image;
    Button button, meaning, cam;
    private static final int CAMERA_REQUEST = 1888;
    private TextView result;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_reader);


        image = findViewById(R.id.capturedPicture);
        button = findViewById(R.id.button);
        cam = findViewById(R.id.camera);
        result = findViewById(R.id.Text);
        meaning = findViewById(R.id.definition);

        meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("Wording", result.getText().toString());
                startActivity(i);
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }catch(ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),"Your device doesn't support capturing images",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
            bitmap = photo;

            TextRecognizer txtRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!txtRecognizer.isOperational()) {
                result.setText(R.string.error_prompt);
            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray items = txtRecognizer.detect(frame);
                StringBuilder strBuilder = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock item = (TextBlock) items.valueAt(i);
                    strBuilder.append(item.getValue());
                    strBuilder.append("/");
                    for (Text line : item.getComponents()) {
                        //extract scanned text lines here
                        Log.v("lines", line.getValue());
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            Log.v("element", element.getValue());

                        }
                    }
                }
                result.setText(strBuilder.toString().substring(0, strBuilder.toString().length()));
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
