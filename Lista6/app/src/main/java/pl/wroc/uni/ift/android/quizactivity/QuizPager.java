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

    List<Question> mQuestions;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int mIndex;
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

        mQuestions = QuestionBank.getInstance().getQuestions();

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
            return QuizFragment.newInstance(mQuestions.get(position).getTextResId());
        }
        @Override
        public int getCount() {
            return mQuestions.size();
        }
    }
}