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
    private Button mQuestionsListButton;

    private TextView mQuestionTextView;
    private TextView mAnsweredTextView;
    private TextView mTokensTextView;
    private TextView mApiLevelTextView;

    private QuestionBank mQuestionsBank = QuestionBank.getInstance();



    private int mCurrentIndex = 0;
    private int mScore = 0;
    private int mCheatTokens = 3;
    private int mAnsweredQuestions = 0;

    private boolean[] mLockedQuestions;
    private boolean[] mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");

        setTitle(R.string.app_name);
        // inflating view objects
        setContentView(R.layout.activity_quiz);

        // check for saved data
        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt("score");
            mAnsweredQuestions = savedInstanceState.getInt("answeredQuestions");
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mCheatTokens = savedInstanceState.getInt(TOKEN);
            mIsCheater = savedInstanceState.getBooleanArray("isCheater");
            mLockedQuestions = savedInstanceState.getBooleanArray("lockedQuestions");
        } else {
            mQuestionsBank.setQuestions();
            mIsCheater = new boolean[mQuestionsBank.size()];
            mLockedQuestions = new boolean[mQuestionsBank.size()];
            mIsCheater = initArray(mIsCheater, false);
            mLockedQuestions = initArray(mLockedQuestions, false);
        }

        mApiLevelTextView = (TextView) findViewById(R.id.api_level_text_view);
        mApiLevelTextView.setText("Android version: " + androidOS);

        mCheatButton = (Button) findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean currentAnswer = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, currentAnswer, mIsCheater[mCurrentIndex]);
                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });

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
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.size();
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
                if (mCurrentIndex >= mQuestionsBank.size()) {
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
                    mCurrentIndex = mQuestionsBank.size()-1;
                }
                updateQuestion();
            }
        });

        mQuestionsListButton = (Button) findViewById(R.id.questions_button);
        mQuestionsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this,QuestionListActivity.class);
                startActivity(intent);
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

        savedInstanceState.putInt("answeredQuestions", mScore);
        savedInstanceState.putInt("score", mAnsweredQuestions);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(TOKEN, mCheatTokens);
        savedInstanceState.putBooleanArray("isCheater", mIsCheater);
        savedInstanceState.putBooleanArray("lockedQuestions", mLockedQuestions);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mScore = savedInstanceState.getInt("score");
        mAnsweredQuestions = savedInstanceState.getInt("answeredQuestions");
        mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
        mCheatTokens = savedInstanceState.getInt(TOKEN);
        mIsCheater = savedInstanceState.getBooleanArray("isCheater");
        mLockedQuestions = savedInstanceState.getBooleanArray("lockedQuestions");


    }

    private void checkScore() {
        if (mAnsweredQuestions == mQuestionsBank.size()) {
            String tText =  getString(R.string.answered_questions)+" "+mScore;
            Toast toast = Toast.makeText(this, (tText), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
    }

    private void updateQuestion() {
        if (!(mLockedQuestions[mCurrentIndex] == true)) {
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
            mAnsweredTextView.setText("");

        } else {
            mTrueButton.setVisibility(View.GONE);
            mFalseButton.setVisibility(View.GONE);
            mAnsweredTextView.setText(R.string.answered);
        }

        int question = mQuestionsBank.getQuestion(mCurrentIndex).getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();

        mLockedQuestions[mCurrentIndex] = true;
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

    private boolean[] initArray(boolean[] arrayToInit, boolean initValue) {
        for(int i = 0; i < arrayToInit.length; i++) {
            arrayToInit[i] = initValue;
        }
        return arrayToInit;
    }
}

