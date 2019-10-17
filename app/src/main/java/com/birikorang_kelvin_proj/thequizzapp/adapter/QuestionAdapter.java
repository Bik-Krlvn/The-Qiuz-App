package com.birikorang_kelvin_proj.thequizzapp.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.birikorang_kelvin_proj.thequizzapp.R;
import com.birikorang_kelvin_proj.thequizzapp.model.Questions;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Questions> questionsList;
    private String[] userText; //user answer input
    private boolean showAnswers = false;
    private static final int KEY_EDIT_TEXT_LIST = 1;
    private static final int KEY_RADIO_QUESTIONS = 2;
    private static final int KEY_CHECKBOX_QUESTIONS = 3;

    public QuestionAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == KEY_CHECKBOX_QUESTIONS)
            return new CheckBoxQuestionsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.check_quiz_questions, parent, false));
        if (viewType == KEY_RADIO_QUESTIONS)
            return new RadioQuestionsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.radio_quiz_questions, parent, false));
        return new EditTextQuestionViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_text_quiz_questions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Questions questions = questionsList.get(position);
        if (getItemViewType(position) == KEY_CHECKBOX_QUESTIONS) {
            ((CheckBoxQuestionsViewHolder) holder).bind(questions, position, showAnswers);
        }
        if (getItemViewType(position) == KEY_RADIO_QUESTIONS) {
            ((RadioQuestionsViewHolder) holder).bind(questions, position, showAnswers);
        }

        if (getItemViewType(position) == KEY_EDIT_TEXT_LIST) {
            ((EditTextQuestionViewHolder) holder).bind(questions, position, showAnswers);
        }

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (questionsList.get(position).getId() == 0) return KEY_RADIO_QUESTIONS;
        if (questionsList.get(position).getId() == 1) return KEY_CHECKBOX_QUESTIONS;
        return KEY_EDIT_TEXT_LIST;
    }

    // Questions layout with edit text
    private class EditTextQuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView mQuestion;
        private TextView mSolution;
        private EditText mAnswer;
        private EditTextListener editTextListener;

        private EditTextQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            mQuestion = itemView.findViewById(R.id.tv_question_text);
            mAnswer = itemView.findViewById(R.id.et_question_answer);
            mSolution = itemView.findViewById(R.id.tv_answer_label);
            mSolution.setVisibility(View.GONE);
            editTextListener = new EditTextListener();
        }

        private void bind(Questions questions, final int position, boolean showAnswers) {
            editTextListener.updatePosition(position);
            mAnswer.setText(userText[position]);
            mQuestion.setText(questions.getQuestion());
            mAnswer.addTextChangedListener(editTextListener);
            String solution = "Answer: " + questions.getAnswer();
            mSolution.setText(solution);
            //show all answers for quiz
            if (showAnswers) {
                mSolution.setVisibility(View.VISIBLE);
                mAnswer.setVisibility(View.GONE);
            } else {
                mAnswer.setVisibility(View.VISIBLE);
                mSolution.setVisibility(View.GONE);
            }
        }
    }

    // Questions layout with checkboxes
    private class CheckBoxQuestionsViewHolder extends RecyclerView.ViewHolder {
        private TextView mQuestion;
        private TextView mSolution;
        private CheckBox mChecked_1;
        private CheckBox mChecked_2;

        private CheckBoxQuestionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mQuestion = itemView.findViewById(R.id.tv_question_text);
            mSolution = itemView.findViewById(R.id.tv_answer_label);
            mChecked_1 = itemView.findViewById(R.id.cb_answer_1);
            mChecked_2 = itemView.findViewById(R.id.cb_answer_2);
            mSolution.setVisibility(View.GONE);
        }

        private void bind(Questions questions, final int position, boolean showAnswers) {
            mQuestion.setText(questions.getQuestion());
            String solution = "Answer: " + questions.getAnswer();
            mSolution.setText(solution);
            if (userText[position].isEmpty()) {
                mChecked_1.setChecked(false);
                mChecked_2.setChecked(false);
            } else {
                if (mChecked_1.getText().toString().equals(userText[position])) {
                    mChecked_1.setChecked(true);
                }
                if (mChecked_2.getText().toString().equals(userText[position])) {
                    mChecked_2.setChecked(true);
                }
            }

            //show all answers for quiz
            if (showAnswers) {
                mSolution.setVisibility(View.VISIBLE);
                mChecked_1.setVisibility(View.GONE);
                mChecked_2.setVisibility(View.GONE);
            } else {
                mChecked_1.setVisibility(View.VISIBLE);
                mChecked_2.setVisibility(View.VISIBLE);
                mSolution.setVisibility(View.GONE);
            }

            mChecked_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        userText[position] = buttonView.getText().toString();
                    } else {
                        userText[position] = "";
                    }
                }
            });

            mChecked_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        userText[position] = buttonView.getText().toString();
                    } else {
                        userText[position] = "";
                    }
                }
            });

        }
    }

    // Question layout with radio buttons
    private class RadioQuestionsViewHolder extends RecyclerView.ViewHolder {
        private TextView mQuestion;
        private TextView mSolution;
        private RadioGroup mRadioGroup;
        private RadioButton mRadioButtonAnswer_1;
        private RadioButton mRadioButtonAnswer_2;


        private RadioQuestionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mQuestion = itemView.findViewById(R.id.tv_question_text);
            mSolution = itemView.findViewById(R.id.tv_answer_label);
            mRadioGroup = itemView.findViewById(R.id.radio_group);
            mSolution.setVisibility(View.GONE);
        }

        private void bind(Questions questions, final int position, boolean showAnswers) {
            mQuestion.setText(questions.getQuestion());
            String solution = "Answer: " + questions.getAnswer();
            mSolution.setText(solution);
            //show all answers for quiz
            if (showAnswers) {
                mSolution.setVisibility(View.VISIBLE);
                mRadioGroup.setVisibility(View.GONE);
            } else {
                mRadioGroup.setVisibility(View.VISIBLE);
                mSolution.setVisibility(View.GONE);
            }

            mRadioButtonAnswer_1 = itemView.findViewById(R.id.radioButton_Answer_1);
            mRadioButtonAnswer_2 = itemView.findViewById(R.id.radioButton_Answer_2);

            if (userText[position] != null) {
                if (mRadioButtonAnswer_1.getText().toString().equals(userText[position])) {
                    mRadioButtonAnswer_1.setChecked(true);
                }
                if (mRadioButtonAnswer_2.getText().toString().equals(userText[position])) {
                    mRadioButtonAnswer_2.setChecked(true);
                }
            }

            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radioButton_Answer_1:
                            userText[position] = itemView.getContext().getString(R.string.radio_answer_1);
                            break;
                        case R.id.radioButton_Answer_2:
                            userText[position] = itemView.getContext().getString(R.string.radio_answer_2);
                            break;
                        default:
                            break;
                    }
                }
            });

        }
    }

    // get user typed data and update userText array, helps in restore edit text in recycler view
    public void setQuestionsList(List<Questions> questionsList) {
        this.questionsList = questionsList;
    }

    public String[] getUserText() {
        return userText;
    }

    public void setUserText(String[] userText) {
        this.userText = userText;
        notifyDataSetChanged();
    }

    public void setShowAnswers(boolean showAnswers) {
        this.showAnswers = showAnswers;
        notifyDataSetChanged();
    }

    private class EditTextListener implements TextWatcher {
        private int position;

        private void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userText[position] = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
