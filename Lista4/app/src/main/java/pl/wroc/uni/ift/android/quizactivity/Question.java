package pl.wroc.uni.ift.android.quizactivity;

import android.os.Parcel;
import android.os.Parcelable;


// Parcelable - daje możliwość przesłania całej klasy do Bundle
public class Question implements Parcelable {

    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue)    {

        mTextResId=textResId;
        mAnswerTrue = answerTrue;
    }

    // Required methods from Parcelable interface
    @Override
    public int describeContents() { return 0; }

    // How to write our class to Parcel object
    // Order does matter!
    @Override
    public void writeToParcel(Parcel destination, int flag) {
        // write mTextResId
        destination.writeInt(mTextResId);

        // there is not default writeBoolean option
        // we will use int 0 for false, and 1 for true instead
        int value = mAnswerTrue ? 1 : 0;
        destination.writeInt(value);
    }

    // private constructor for Question to be created from Parcel object
    // remember order does matter see writeToParcel method
    // base on writeToParcel method we know that we have to read
    // two consecutive integer numbers from parcel. First is resource id for question
    // second one is the answer for question (true or false) stored here as int.
    private Question(Parcel in)
    {
        mTextResId = in.readInt();
        int value = in.readInt();
        if (value == 1) {
            mAnswerTrue = true;
        } else  { mAnswerTrue = false; }
    }

    // Parcelable interface requires to implement static field CREATOR which is used
    // internally. Here we are implementing this on the fly
    public static final Parcelable.Creator<Question> CREATOR =
            new Parcelable.Creator<Question>()
            {
                public Question createFromParcel(Parcel in)
                {
                    // this is the reason why we created private constructir in line 44
                    Question question = new Question(in);
                    return question;
                }

                // this is the second method required by Creator<Question> class
                // we are just returning Question[] array of size = size;
                public Question[] newArray(int size) { return new Question[size]; }

            };



    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
