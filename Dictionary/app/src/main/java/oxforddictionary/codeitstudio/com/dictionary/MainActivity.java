package oxforddictionary.codeitstudio.com.dictionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

String url;

private EditText Word;
private Button myButton;
private String word1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Word = findViewById(R.id.word);
        myButton = findViewById(R.id.buttonfecth);
        Word.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String a = extras.getString("Wording");
            Word.setText(a);
            }
        else{
            Toast.makeText(getApplicationContext(),"no extras",Toast.LENGTH_LONG).show();
        }
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String words =  Word.getText().toString();
                if(words.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter a Word!",Toast.LENGTH_LONG).show();
                }
                else{word1=words; url = dictionaryEntries();requestApi();}
             }

        });


    }
        public void requestApi(){
                MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(this);
                myDictionaryRequest.execute(url);
        }

        public String dictionaryEntries() {
        final String language = "en";
        final String word = word1;
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
        }


}
