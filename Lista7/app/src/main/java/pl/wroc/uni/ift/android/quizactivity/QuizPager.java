package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;

public class QuizPager extends FragmentActivity {

    public QuestionBank mQuestions;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int mIndex;
    private int mAnsweredQuestions;
    private int mCheatTokens = 3;
    private int mScore = 0;
    private boolean[] mLockedQuestions;
    private boolean[] mIsCheater;

    private static final String EXTRA_CURRENT_ID = "question_id";

    public static Intent newIntent(Context packageContext, int index) {
        Intent intent = new Intent(packageContext,QuizPager.class);
        intent.putExtra(EXTRA_CURRENT_ID,index);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_pager);

        mQuestions = QuestionBank.getInstance();

        mLockedQuestions = new boolean[mQuestions.size()];
        mLockedQuestions = initArray(mLockedQuestions, false);
        mIsCheater = new boolean[mQuestions.size()];
        mIsCheater = initArray(mIsCheater, false);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new QuizPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mIndex = getIntent().getIntExtra("currentQuestion",0);
        mPager.setCurrentItem(mIndex);
    }

    private class QuizPagerAdapter extends FragmentStatePagerAdapter {
        public QuizPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public  Fragment getItem(int position) {
            return QuizFragment.newInstance(mQuestions.getQuestions().get(position).getTextResId(), position, mQuestions.getQuestions().get(position).getText());
        }
        @Override
        public int getCount() {
            return mQuestions.size();
        }
    }

    public int getCheatTokens(){
        return mCheatTokens;
    }

    public int getScore(){
        return mScore;
    }

    public void increaseScore(){
        mScore ++;
    }

    public void reduceCheatTokens(){
        mCheatTokens --;
    }

    public int getAnsweredQuestions(){
        return mAnsweredQuestions;
    }

    public void increaseAnsweredQuestions(){
        mAnsweredQuestions ++;
    }

    public void setStatus(boolean status, int index)
    {
        mIsCheater[index] = status;
    }

    public void setQuestionStatus(boolean status, int index)
    {
        mLockedQuestions[index] = status;
    }

    public boolean getQuestionStatus(int index)
    {
        return mLockedQuestions[index];
    }

    public boolean getStatus(int index)
    {
        return mIsCheater[index];
    }

    private boolean[] initArray(boolean[] arrayToInit, boolean initValue) {
        for(int i = 0; i < arrayToInit.length; i++) {
            arrayToInit[i] = initValue;
        }
        return arrayToInit;
    }
}