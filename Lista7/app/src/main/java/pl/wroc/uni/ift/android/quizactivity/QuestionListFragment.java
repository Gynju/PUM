package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Gynju on 28.11.2017.
 */

public class QuestionListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private QuestionAdapter mAdapter;
    public QuestionBank mQuestionsBank = QuestionBank.getInstance();
    private int adapterIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_question_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        QuestionBank mQuestionsBank = QuestionBank.getInstance();
        mAdapter = new QuestionAdapter(mQuestionsBank.getQuestions());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mQuestionTextView;
        private Question mQuestion;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.question_list_fragment, parent, false));
            itemView.setOnClickListener(this);
            mQuestionTextView = (TextView) itemView.findViewById(R.id.questions);


        }

        public void bind(Question question) {
            mQuestion = question;
            if(mQuestion.getTextResId() == R.string.new_question) {
                mQuestionTextView.setText(mQuestion.getText());
            }
            else {
                mQuestionTextView.setText(mQuestion.getTextResId());
            }
        }

        public void onClick(View view) {
            Intent intent = QuizPager.newIntent(getActivity(), mQuestion.getTextResId());

            adapterIndex = mAdapter.mQuestions.indexOf(mQuestion);
            intent.putExtra("currentQuestion", adapterIndex);
            startActivity(intent);
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {
        private List<Question> mQuestions;

        public QuestionAdapter(List<Question> questions) {
            mQuestions = questions;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new QuestionHolder(layoutInflater, parent);

        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bind(question);
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_question:
                Intent intent = AddQuestionActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            case R.id.quit:
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateList() {
        mAdapter = new QuestionAdapter(mQuestionsBank.getQuestions());
        mRecyclerView.setAdapter(mAdapter);
    }
}