package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.util.Log;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTIONS = "questions";
    private static final String TOKEN = "currentTokens";
    private static final int CHEAT_REQEST_CODE = 0;

    private String androidOS = Build.VERSION.RELEASE;

    private ImageButton mTrueButton;
    private ImageButton mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mBackButton;
    private Button mCheatButton;

    private TextView mQuestionTextView;
    private TextView mAnsweredTextView;
    private TextView mTokensTextView;
    private TextView mApiLevelTextView;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true),
            new Question(R.string.question_stolica_dolnego_slaska, false),
            new Question(R.string.question_sniezka, true),
            new Question(R.string.question_wisla, true)
    };

    private int mCurrentIndex = 0;
    private int mScore = 0;
    private int mCheatTokens = 3;
    private int mAnsweredQuestions = 0;
    private int[] mLockedQuestions = {-1, -1, -1, -1};

    private boolean[] mIsCheater = {false, false, false, false};

    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");

        setTitle(R.string.app_name);
        // inflating view objects
        setContentView(R.layout.activity_quiz);

        // check for saved data
        if (savedInstanceState != null) {
            mCheatTokens = savedInstanceState.getInt(TOKEN);
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            Log.i(TAG, String.format("onCreate(): Restoring saved index: %d", mCurrentIndex));

            // here in addition we are restoring our Question array;
            // getParcelableArray returns object of type Parcelable[]
            // since our Question is implementing this interface (Parcelable)
            // we are allowed to cast the Parcelable[] to desired type which
            // is the Question[] here.
            mQuestionsBank = (Question []) savedInstanceState.getParcelableArray(KEY_QUESTIONS);
            // sanity check
            if (mQuestionsBank == null)
            {
                Log.e(TAG, "Question bank array was not correctly returned from Bundle");

            } else {
                Log.i(TAG, "Question bank array was correctly returned from Bundle");
            }

        }

        mApiLevelTextView = (TextView) findViewById(R.id.api_level_text_view);
        mApiLevelTextView.setText("Android version: " + androidOS);

        mCheatButton = (Button) findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, currentAnswer, mIsCheater[mCurrentIndex]);
                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });

        Log.i(TAG, String.format("Tyle jest teraz tokenów: %d", mCheatTokens));

        if(mCheatTokens <= 0)
        {

            mTokensTextView = (TextView) findViewById(R.id.cheat_used_all_tokens);
            mTokensTextView.setText(R.string.used_all_tokens);
            mCheatButton.setVisibility(View.GONE);
        }

        mAnsweredTextView = (TextView) findViewById(R.id.answered_text_view);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (ImageButton) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(true);
                    }
                }
        );

        mFalseButton = (ImageButton) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mCurrentIndex + 1;
                if (mCurrentIndex > mQuestionsBank.length-1) {
                    mCurrentIndex = 0;

                }
                updateQuestion();
            }
        });

        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex < 0) {
                    mCurrentIndex = mQuestionsBank.length-1;
                }
                updateQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == CHEAT_REQEST_CODE) {
            if (data != null)
            {
                boolean answerWasShown = CheatActivity.wasAnswerShown(data);
                if (answerWasShown) {

                    if(!mIsCheater[mCurrentIndex]) {
                        Toast.makeText(this,
                                R.string.message_for_cheaters,
                                Toast.LENGTH_LONG)
                                .show();
                        mIsCheater[mCurrentIndex] = true;
                        mCheatTokens = mCheatTokens - 1;
                        if(mCheatTokens <= 0)
                        {
                            mTokensTextView = (TextView) findViewById(R.id.cheat_used_all_tokens);
                            mTokensTextView.setText(R.string.used_all_tokens);
                            mCheatButton.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, String.format("onSaveInstanceState: current index %d ", mCurrentIndex) );
        Log.i(TAG, String.format("onSaveInstanceState: current tokens %d ", mCheatTokens) );

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(TOKEN, mCheatTokens);
        savedInstanceState.putParcelableArray(KEY_QUESTIONS, mQuestionsBank);
        savedInstanceState.putBooleanArray("isCheater", mIsCheater);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIsCheater = savedInstanceState.getBooleanArray("isCheater");
        mCheatTokens = savedInstanceState.getInt("currentTokens");
    }

    private void checkScore() {
        if (mAnsweredQuestions == 4) {
            String tText =  getString(R.string.answered_questions)+" "+mScore;
            Toast toast = Toast.makeText(this, (tText), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
    }

    private void updateQuestion() {
        if (!(mLockedQuestions[mCurrentIndex] == mCurrentIndex)) {
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
            mAnsweredTextView.setText("");

        } else {
            mTrueButton.setVisibility(View.GONE);
            mFalseButton.setVisibility(View.GONE);
            mAnsweredTextView.setText(R.string.answered);
        }

        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

        mLockedQuestions[mCurrentIndex] = mCurrentIndex;
        mTrueButton.setVisibility(View.GONE);
        mFalseButton.setVisibility(View.GONE);
        int toastMessageId = 0;

        if (userPressedTrue == answerIsTrue) {
            toastMessageId = R.string.correct_toast;
            mScore += 1;
        } else {
            toastMessageId = R.string.incorrect_toast;
        }
        mAnsweredQuestions += 1;
        mAnsweredTextView.setText(R.string.answered);
        Toast toast = Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();

        checkScore();
    }
}
