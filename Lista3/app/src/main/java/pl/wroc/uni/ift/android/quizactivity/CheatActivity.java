package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class CheatActivity extends AppCompatActivity {

    private final static String EXTRA_KEY_ANSWER = "Answer";
    private final static String KEY_QUESTION = "QUESTION";
    private final static String EXTRA_KEY_SHOWN = "wasShown";
    private final static String EXTRA_KEY_CHEATER = "alreadyCheated";
    TextView mTextViewAnswer;
    Button mButtonShow;

    boolean mAnswer;
    boolean wasClicked;

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("wasClicked", wasClicked);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wasClicked = savedInstanceState.getBoolean("wasClicked");
        if(savedInstanceState.getBoolean("wasClicked")) {
            if (mAnswer) {
                mTextViewAnswer.setText("Prawda");
            } else {
                mTextViewAnswer.setText("Fałsz");
            }
        }
        setAnswerShown(savedInstanceState.getBoolean("wasClicked"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            setAnswerShown(savedInstanceState.getBoolean("wasShown"));
        } else {
            setAnswerShown(false);
        }


        setContentView(R.layout.activity_cheat);
        mAnswer = getIntent().getBooleanExtra(EXTRA_KEY_ANSWER,false);

        mTextViewAnswer = (TextView) findViewById(R.id.text_view_answer);
        mButtonShow = (Button) findViewById(R.id.button_show_answer);
        mButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswer) {
                    mTextViewAnswer.setText("Prawda");
                } else {
                    mTextViewAnswer.setText("Fałsz");
                }
                wasClicked = true;
                setAnswerShown(true);
            }
        });
        if(getIntent().getBooleanExtra(EXTRA_KEY_CHEATER, false)) {
            if (mAnswer) {
                mTextViewAnswer.setText("Prawda");
            } else {
                mTextViewAnswer.setText("Fałsz");
            }
        }
    }


    public static boolean wasAnswerShown(Intent data)
    {
        return data.getBooleanExtra(EXTRA_KEY_SHOWN, false);
    }

    public static Intent newIntent(Context context, boolean answerIsTrue, boolean alreadyCheated)
    {

        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_KEY_ANSWER, answerIsTrue);
        intent.putExtra(EXTRA_KEY_CHEATER, alreadyCheated);
        return intent;

    }

    private void setAnswerShown (boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_KEY_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }






}
