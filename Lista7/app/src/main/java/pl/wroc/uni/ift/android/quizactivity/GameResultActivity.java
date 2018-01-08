package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Gynju on 19.12.2017.
 */

public class GameResultActivity extends AppCompatActivity {

    //ZMIENNE
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final static String EXTRA_KEY_ANSWERS = "Answers";
    private final static String EXTRA_KEY_QUESTIONS = "Questions";
    private final static String EXTRA_KEY_TOKENS= "Tokens";

    private int mAnswers;
    private int mQuestions;
    private int mTokens;

    TextView mTextViewQuestions;
    TextView mTextViewAnswers;
    TextView mTextViewATokens;

    ImageView mImageView;
    Button mButton;

    //FUNKCJE
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
        } else {
        }

        setContentView(R.layout.activity_game_result);

        mAnswers = getIntent().getIntExtra(EXTRA_KEY_ANSWERS, 0);
        mQuestions = getIntent().getIntExtra(EXTRA_KEY_QUESTIONS, 0);
        mTokens = getIntent().getIntExtra(EXTRA_KEY_TOKENS, 0);

        mTextViewQuestions = (TextView) findViewById(R.id.text_questions_quantity);
        mTextViewAnswers = (TextView) findViewById(R.id.text_answered_questions);
        mTextViewATokens = (TextView) findViewById(R.id.text_remaining_tokens);

        mButton = (Button) findViewById(R.id.button_photo);
        mImageView = (ImageView) findViewById(R.id.image_view);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAPhotoIntent();
            }
        });

        String tText =  getResources().getString(R.string.questions_quantity_label) + " " + mAnswers;
        mTextViewQuestions.setText(tText);
        tText =  getResources().getString(R.string.answered_questions_label) + " " + mQuestions;
        mTextViewAnswers.setText(tText);
        tText =  getResources().getString(R.string.remaining_tokens_label) + " " + mTokens;
        mTextViewATokens.setText(tText);
    }

    public static Intent newIntent(Context context,  int questionsQuantity, int answeredQuestions, int remainingTokens) {
        Intent intent = new Intent(context, GameResultActivity.class);
        intent.putExtra(EXTRA_KEY_ANSWERS, questionsQuantity);
        intent.putExtra(EXTRA_KEY_QUESTIONS, answeredQuestions);
        intent.putExtra(EXTRA_KEY_TOKENS, remainingTokens);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_game_result,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.back:
                Intent intent = new Intent(GameResultActivity.this,QuestionListActivity.class);
                startActivity(intent);
                return true;

            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_TEXT, "Asked questions: "+ mAnswers + "\n" + "Answered questions: "+ mQuestions + "\n" + "Remaining cheat Tokens: "+ mTokens);

                startActivity(Intent.createChooser(shareIntent, "Share"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void takeAPhotoIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }

    }
}