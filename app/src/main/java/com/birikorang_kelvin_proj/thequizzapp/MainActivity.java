package com.birikorang_kelvin_proj.thequizzapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.birikorang_kelvin_proj.thequizzapp.adapter.QuestionAdapter;
import com.birikorang_kelvin_proj.thequizzapp.model.Questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final String KEY_QUESTION_LIST = "questions";
    private final String KEY_USER_INPUT = "user_input";
    public static final String KEY_SHOW_ANSWERS = "show_answers";
    public static final String KEY_CURRENT_PAGE_NUMBER = "page_number";
    private ArrayList<Questions> questionsArrayList;
    private QuestionAdapter questionAdapter;
    private String[] userText = new String[10];
    private AlertDialog.Builder alertBuilder;
    private RecyclerView recyclerView;
    private TextView mPageNumber;
    private int currentPageNumber = 1;
    private String currentPageText;
    private boolean showAnswers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionsArrayList = new ArrayList<>();
        mPageNumber = findViewById(R.id.tv_question_number);
        Arrays.fill(userText, "");
        alertBuilder = new AlertDialog.Builder(this);
        initQuestions();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        questionAdapter = new QuestionAdapter();
        questionAdapter.setQuestionsList(questionsArrayList);
        questionAdapter.setUserText(userText);
        recyclerView.setAdapter(questionAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentPageNumber = getCurrentPosition();
                    currentPageNumber++;
                    currentPageText = "Question " + currentPageNumber;
                    mPageNumber.setText(currentPageText);
                }
            }
        });

        currentPageText = "Question " + currentPageNumber;
        mPageNumber.setText(currentPageText);
    }

    private int getCurrentPosition() {
        int position = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
        if (position < 0) return 1;
        return position;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionsArrayList);
        outState.putStringArray(KEY_USER_INPUT, questionAdapter.getUserText());
        outState.putInt(KEY_CURRENT_PAGE_NUMBER, currentPageNumber);
        outState.putBoolean(KEY_SHOW_ANSWERS,showAnswers);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(KEY_QUESTION_LIST)) {
            questionsArrayList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
        }
        if (savedInstanceState.containsKey(KEY_USER_INPUT)) {
            userText = savedInstanceState.getStringArray(KEY_USER_INPUT);
            questionAdapter.setUserText(userText);
        }
        if (savedInstanceState.containsKey(KEY_CURRENT_PAGE_NUMBER)) {
            currentPageNumber = savedInstanceState.getInt(KEY_CURRENT_PAGE_NUMBER);
            currentPageText = "Question " + currentPageNumber;
            mPageNumber.setText(currentPageText);
        }

        if (savedInstanceState.containsKey(KEY_SHOW_ANSWERS)){
            showAnswers = savedInstanceState.getBoolean(KEY_SHOW_ANSWERS);
            questionAdapter.setShowAnswers(showAnswers);
        }
    }

    // load quiz data
    private void initQuestions() {
        String[] solarQuestions = getQuestionList();
        String[] solarAnswers = getAnswerList();

        for (int i = 0; i < solarQuestions.length; i++) {
            questionsArrayList.add(new Questions(i, solarQuestions[i].trim(), solarAnswers[i].trim()));
        }
    }

    // contains all quiz questions
    private String[] getAnswerList() {
        return new String[]{
                getString(R.string.answer_1),
                getString(R.string.answer_2),
                getString(R.string.answer_3),
                getString(R.string.answer_4),
                getString(R.string.answer_5),
                getString(R.string.answer_6),
                getString(R.string.answer_7),
                getString(R.string.answer_8),
                getString(R.string.answer_9),
                getString(R.string.answer_10),
        };
    }

    // contains all quiz question-answers
    private String[] getQuestionList() {
        return new String[]{
                getString(R.string.question_1),
                getString(R.string.question_2),
                getString(R.string.question_3),
                getString(R.string.question_4),
                getString(R.string.question_5),
                getString(R.string.question_6),
                getString(R.string.question_7),
                getString(R.string.question_8),
                getString(R.string.question_9),
                getString(R.string.question_10)
        };
    }

    // assign score message based on user score
    private String getScoreMessage(int score) {
        if (score > 7) return getString(R.string.score_msg_excellent);
        if (score >= 5) return getString(R.string.score_msg_average);
        return getString(R.string.score_msg_low);
    }

    // fab callback
    public void gradeQuiz(View view) {
        int correctAnswers = 0;
        int wrongAnswers = 0;
        int total = getAnswerList().length;
        for (int i = 0; i < total; i++) {
            if (questionAdapter.getUserText()[i].toLowerCase().trim()
                    .equals(getAnswerList()[i].toLowerCase())) {
                correctAnswers++;
            } else {
                wrongAnswers++;
            }
        }
        Toast.makeText(this, "score " + correctAnswers + " / " + total, Toast.LENGTH_SHORT).show();
        showQuizResults(correctAnswers, wrongAnswers, total);
    }

    // show quiz result to the user
    private void showQuizResults(int correctAns, int wrongAns, int total) {
        alertBuilder.setTitle("Quiz results");
        alertBuilder.setMessage("Correct Answers: " + correctAns + "\nWrong Answers: " + wrongAns +
                "\nScore: " + correctAns+" / "+total + " \nMerit: " + getScoreMessage(correctAns));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showAnswers = false;
                questionAdapter.setShowAnswers(false);
            }
        });
        alertBuilder.setNegativeButton("reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAnswers = false;
                Arrays.fill(userText, "");
                questionAdapter.setUserText(userText);
                questionAdapter.setShowAnswers(false);
            }
        });
        alertBuilder.setNeutralButton("show answers", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAnswers = !showAnswers;
                questionAdapter.setShowAnswers(showAnswers);
            }
        });
        alertBuilder.show();
    }


}
