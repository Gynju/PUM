package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import static android.app.Activity.RESULT_OK;

public class QuizFragment extends Fragment {

    public static final String KEY_INDEX = "index";
    public static final String TOKEN = "currentTokens";
    public static final String ARG_QUESTION_ID = "question_id";
    public static final String ARG_QUESTION_TEXT = "question_text";
    public static final String ARG_QUESTION_POSITION = "question_position";
    public static final int CHEAT_REQEST_CODE = 0;

    public String androidOS = Build.VERSION.RELEASE;

    public ImageButton mTrueButton;
    public ImageButton mFalseButton;
    public Button mCheatButton;
    public Button mQuestionsListButton;

    public TextView mQuestionTextView;
    public TextView mAnsweredTextView;
    public TextView mTokensTextView;
    public TextView mApiLevelTextView;

    public QuestionBank mQuestionsBank = QuestionBank.getInstance();

    public int mCurrentIndex;
    private int mQuestionId;
    public String mQuestionText;

    public static QuizFragment newInstance(int id, int position, String text)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUESTION_ID, id);
        args.putSerializable(ARG_QUESTION_POSITION, position);
        args.putString(ARG_QUESTION_TEXT, text);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
        } else {
        }
        mQuestionId = (int) getArguments().getSerializable(ARG_QUESTION_ID);
        mCurrentIndex = (int) getArguments().getSerializable(ARG_QUESTION_POSITION);
        mQuestionText = getArguments().getString(ARG_QUESTION_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment, container, false);

        mApiLevelTextView = (TextView) v.findViewById(R.id.api_level_text_view);
        mApiLevelTextView.setText("Android version: " + androidOS);

        mCheatButton = (Button) v.findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean currentAnswer = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();
                Intent intent = CheatActivity.newIntent(getActivity(), currentAnswer, ((QuizPager)getActivity()).getStatus(mCurrentIndex));
                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });

        if(((QuizPager)getActivity()).getCheatTokens() <= 0)
        {

            mTokensTextView = (TextView) v.findViewById(R.id.cheat_used_all_tokens);
            mTokensTextView.setText(R.string.used_all_tokens);
            mCheatButton.setVisibility(View.GONE);
        }

        mAnsweredTextView = (TextView) v.findViewById(R.id.answered_text_view);
        mQuestionTextView = (TextView) v.findViewById(R.id.question_text_view);

        mTrueButton = (ImageButton) v.findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(true);
                    }
                }
        );

        mFalseButton = (ImageButton) v.findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mQuestionsListButton = (Button) v.findViewById(R.id.questions_button);
        mQuestionsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),QuestionListActivity.class);
                startActivity(intent);
            }
        });

        updateQuestion();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == CHEAT_REQEST_CODE) {
            if (data != null)
            {
                boolean answerWasShown = CheatActivity.wasAnswerShown(data);
                if (answerWasShown) {

                    if(!((QuizPager)getActivity()).getStatus(mCurrentIndex)) {
                        Toast.makeText(getActivity(), R.string.message_for_cheaters, Toast.LENGTH_LONG).show();
                        ((QuizPager)getActivity()).setStatus(true, mCurrentIndex);
                        ((QuizPager)getActivity()).reduceCheatTokens();
                        if(((QuizPager)getActivity()).getCheatTokens() <= 0)
                        {
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

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void checkScore() {
        if (((QuizPager)getActivity()).getAnsweredQuestions() >= mQuestionsBank.size()) {
            Intent intent = GameResultActivity.newIntent(getActivity(), mQuestionsBank.size(), ((QuizPager)getActivity()).getScore(), ((QuizPager)getActivity()).getCheatTokens());
            startActivity(intent);
        }
    }

    private void updateQuestion() {
        if (!(((QuizPager)getActivity()).getQuestionStatus(mCurrentIndex) == true)) {
            mTrueButton.setVisibility(View.VISIBLE);
            mFalseButton.setVisibility(View.VISIBLE);
            mAnsweredTextView.setText("");

        } else {
            mTrueButton.setVisibility(View.GONE);
            mFalseButton.setVisibility(View.GONE);
            mAnsweredTextView.setText(R.string.answered);
        }

        int question = mQuestionId; //mQuestionsBank.getQuestion(mCurrentIndex).getTextResId();
        if(question == R.string.new_question)
            mQuestionTextView.setText(mQuestionText);
        else
            mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();

        ((QuizPager)getActivity()).setQuestionStatus(true, mCurrentIndex);
        mTrueButton.setEnabled(false);//setVisibility(View.GONE);
        mFalseButton.setEnabled(false);//setVisibility(View.GONE);
        if(userPressedTrue)
            mFalseButton.setAlpha(.3f);
        else
            mTrueButton.setAlpha(.3f);
        int toastMessageId = 0;

        if (userPressedTrue == answerIsTrue) {
            toastMessageId = R.string.correct_toast;
            ((QuizPager)getActivity()).increaseScore();
        } else {
            toastMessageId = R.string.incorrect_toast;
        }
        ((QuizPager)getActivity()).increaseAnsweredQuestions();
        mAnsweredTextView.setText(R.string.answered);
        Toast toast = Toast.makeText(getActivity(), toastMessageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();

        checkScore();
    }
}