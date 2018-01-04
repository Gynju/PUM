package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Gynju on 19.12.2017.
 */

public class GameResultActivity extends AppCompatActivity {

    //ZMIENNE
    private final static String EXTRA_KEY_ANSWERS = "Answers";
    private final static String EXTRA_KEY_QUESTIONS = "Questions";
    private final static String EXTRA_KEY_TOKENS= "Tokens";

    private int mAnswers;
    private int mQuestions;
    private int mTokens;

    TextView mTextViewQuestions;
    TextView mTextViewAnswers;
    TextView mTextViewATokens;

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
}