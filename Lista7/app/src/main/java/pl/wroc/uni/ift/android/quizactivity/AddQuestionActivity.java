package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.UUID;

public class AddQuestionActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_ID = "question_id";
    EditText mEditTextView;
    Button mButton;
    Button mAnswerFalse;
    Button mAnswerTrue;
    String mQuestionText;
    boolean mAnswer;
    public static Intent newIntent(Context packageContext)
    {
        Intent intent = new Intent(packageContext,AddQuestionActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        mEditTextView = (EditText)findViewById(R.id.EDITTEXT);
        mEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction (TextView textView,int i, KeyEvent keyEvent){
                boolean handled = false;
                //if (i == EditorInfo.IME_ACTION_NEXT) {
                    mQuestionText = textView.getText().toString();
                //}
                return handled;
            }
        });

        mAnswerFalse = (Button) findViewById(R.id.button_false);
        mAnswerFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswer = false;
            }
        });
        mAnswerTrue = (Button) findViewById(R.id.button_true);
        mAnswerTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswer = true;
            }
        });
        mButton = (Button) findViewById(R.id.button_done);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionBank mQuestionsBank = QuestionBank.getInstance();
                mQuestionsBank.addQuestion(mQuestionText, mAnswer);
                finish();
            }
        });
    }
}
