package pl.wroc.uni.ift.android.quizactivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gynju on 14.11.2017.
 */

class QuestionBank{

    private static QuestionBank instance;
    private static List<Question> mQuestionList = new ArrayList<Question>();

    private QuestionBank() {
        mQuestionList.add(new Question(R.string.question_stolica_polski, true));
        mQuestionList.add(new Question(R.string.question_stolica_dolnego_slaska, false));
        mQuestionList.add(new Question(R.string.question_sniezka, true));
        mQuestionList.add(new Question(R.string.question_wisla, true));
        mQuestionList.add(new Question(R.string.question_polska, false));
    }

    public static QuestionBank getInstance() {
        if(instance == null)
        {
            instance = new QuestionBank();
        }
        return instance;
    }

    public Question getQuestion(int index) {
        return mQuestionList.get(index);
    }

    public List<Question> getQuestions() {
        return mQuestionList;
    }

    public int size() {
        return mQuestionList.size();
    }

    public static void setQuestions() {
        mQuestionList.add(new Question(R.string.question_stolica_polski, true));
        mQuestionList.add(new Question(R.string.question_stolica_dolnego_slaska, false));
        mQuestionList.add(new Question(R.string.question_sniezka, true));
        mQuestionList.add(new Question(R.string.question_wisla, true));
        mQuestionList.add(new Question(R.string.question_polska, false));
    }

}
