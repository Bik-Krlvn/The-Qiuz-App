package com.birikorang_kelvin_proj.thequizzapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// Question model
public class Questions implements Parcelable {
    private int id;
    private String question;
    private String answer;

    public Questions(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    @NonNull
    @Override
    public String toString() {
        return "id: " + id + " question: " + question + " answer: " + answer;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(answer);
    }

    private Questions(Parcel in) {
        id = in.readInt();
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<Questions> CREATOR = new Creator<Questions>() {
        @Override
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };
}
