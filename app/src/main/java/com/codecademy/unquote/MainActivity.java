package com.codecademy.unquote;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    // Integer member variables here
    int currentQuestionIndex;
    int totalCorrect;
    int totalQuestions;

    // ArrayList member variable here
    ArrayList<Question> questions;

    // Declare View member variables
    ImageView questionImageView;
    TextView questionTextView;
    TextView questionsRemainingTextView;
    TextView questionsLeftTextView;
    Button answer0Button;
    Button answer1Button;
    Button answer2Button;
    Button answer3Button;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show app icon in ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_unquote_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);

        setContentView(R.layout.activity_main);

        // assign View member variables
        questionImageView = findViewById(R.id.iv_main_question_image);
        questionTextView = findViewById(R.id.tv_main_question_title);
        questionsRemainingTextView = findViewById(R.id.tv_main_questions_remaining_count);
        questionsLeftTextView = findViewById(R.id.tv_main_questions_remaining);
        answer0Button = findViewById(R.id.btn_main_answer_0);
        answer1Button = findViewById(R.id.btn_main_answer_1);
        answer2Button = findViewById(R.id.btn_main_answer_2);
        answer3Button = findViewById(R.id.btn_main_answer_3);
        submitButton = findViewById(R.id.btn_main_submit_answer);

        answer0Button.setOnClickListener(v -> onAnswerSelected(0));
        answer1Button.setOnClickListener(v -> onAnswerSelected(1));
        answer2Button.setOnClickListener(v -> onAnswerSelected(2));
        answer3Button.setOnClickListener(v -> onAnswerSelected(3));
        submitButton.setOnClickListener(view -> onAnswerSubmission());

        startNewGame();
    }

    void displayQuestion(Question question) {
        questionImageView.setImageResource(question.imageId);
        questionTextView.setText(question.questionText);
        answer0Button.setText(question.answer0);
        answer1Button.setText(question.answer1);
        answer2Button.setText(question.answer2);
        answer3Button.setText(question.answer3);
    }

    void displayQuestionsRemaining(int questionsRemaining) {
        questionsRemainingTextView.setText(String.valueOf(questionsRemaining));
    }

    @SuppressLint("SetTextI18n")
    void onAnswerSelected(int answerSelected) {
        Question currentQuestion = getCurrentQuestion();
        currentQuestion.playerAnswer = answerSelected;
        answer0Button.setText(currentQuestion.answer0);
        answer1Button.setText(currentQuestion.answer1);
        answer2Button.setText(currentQuestion.answer2);
        answer3Button.setText(currentQuestion.answer3);
        if (answerSelected == 0) {answer0Button.setText("✔ " + currentQuestion.answer0);}
        else if (answerSelected == 1) {answer1Button.setText("✔ " + currentQuestion.answer1);}
        else if (answerSelected == 2) {answer2Button.setText("✔ " + currentQuestion.answer2);}
        else if (answerSelected == 3) {answer3Button.setText("✔ " + currentQuestion.answer3);}
    }

    // StartNewGame() here
    void startNewGame() {
        Question question0 = new Question(R.drawable.img_quote_0, "Pretty good advice, and perhaps a scientist did say it... Who actually did?", "Albert Einstein", "Isaac Newton", "Rita Mae Brown", "Rosalind Franklin", 2);
        Question question1 = new Question(R.drawable.img_quote_1, "Was honest Abe honestly quoted? Who authored this pithy bit of wisdom?", "Edward Stieglitz", "Maya Angelou", "Abraham Lincoln", "Ralph Waldo Emerson", 0);
        Question question2 = new Question(R.drawable.img_quote_2, "Easy advice to read, difficult advice to follow — who actually said it?", "Martin Luther King Jr.", "Mother Teresa", "Fred Rogers", "Oprah Winfrey", 1);
        Question question3 = new Question(R.drawable.img_quote_3, "Insanely inspiring, insanely incorrect (maybe). Who is the true source of this inspiration?", "Nelson Mandela", "Harriet Tubman", "Mahatma Gandhi", "Nicholas Klein", 3);
        Question question4 = new Question(R.drawable.img_quote_4, "A peace worth striving for — who actually reminded us of this?", "Malala Yousafzai", "Martin Luther King Jr.", "Liu Xiaobo", "Dalai Lama", 1);
        Question question5 = new Question(R.drawable.img_quote_5, "Unfortunately, true — but did Marilyn Monroe convey it or did someone else?", "Laurel Thatcher Ulrich", "Eleanor Roosevelt", "Marilyn Monroe", "Queen Victoria", 0);

        questions = new ArrayList<>();

        questions.add(question0);
        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);

        totalCorrect = 0;
        totalQuestions = questions.size();

        Question firstQuestion = chooseNewQuestion();

        displayQuestionsRemaining(questions.size());

        displayQuestion(firstQuestion);
    }

    Question chooseNewQuestion() {
        currentQuestionIndex = generateRandomNumber(questions.size());
        return questions.get(currentQuestionIndex);
    }

    Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    @SuppressLint("SetTextI18n")
    void onAnswerSubmission() {
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion.isCorrect()) {
            totalCorrect ++;
        }
        if (currentQuestion.playerAnswer == -1) return;
        questions.remove(currentQuestion);
        displayQuestionsRemaining(questions.size());
        if (questions.size() == 1 ) questionsLeftTextView.setText("Question Left");
        if (questions.size() == 0) {
            questionsLeftTextView.setText("Questions Left");
            String gameOverMessage = getGameOverMessage(totalCorrect, totalQuestions);
            // Show a popup
            AlertDialog.Builder gameOverDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            gameOverDialogBuilder.setCancelable(false);
            gameOverDialogBuilder.setTitle("Game Over!");
            gameOverDialogBuilder.setMessage(gameOverMessage);
            gameOverDialogBuilder.setPositiveButton("Play Again!", (dialog, which) -> startNewGame());
            gameOverDialogBuilder.create().show();
        } else {
            chooseNewQuestion();
            displayQuestion(getCurrentQuestion());
        }
    }

    int generateRandomNumber(int max) {
        double randomNumber = Math.random();
        double result = max * randomNumber;
        return (int) result;
    }

    String getGameOverMessage(int totalCorrect, int totalQuestions) {
        if (totalCorrect == totalQuestions) {
            return "You got all " + totalQuestions + " right! You won!";
        } else {
            return "You got " + totalCorrect + " right out of " + totalQuestions + ". Better luck next time!";
        }
    }
}