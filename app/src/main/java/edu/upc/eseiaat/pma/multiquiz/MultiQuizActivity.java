package edu.upc.eseiaat.pma.multiquiz;

import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

public class MultiQuizActivity extends AppCompatActivity {

    private int id_answers[]= {
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    private int correct_answer;
    private int current_question;
    private String[] all_questions;
    private boolean[] answer_is_correct;
    private TextView text_question;
    private RadioGroup group;
    private Button btn_next, btn_previous;
    private int[] ans;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiquiz);

        // Les creem com a 'fields' per a poder accedir a les variables des de qualsevol punt del codi
        // (ho fem per accedir-hi des del mètode)
        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup) findViewById(R.id.radiogroup);

        btn_next = (Button) findViewById(R.id.btn_check);
        btn_previous = (Button) findViewById(R.id.btn_previous);

        all_questions= getResources().getStringArray(R.array.all_questions);
        answer_is_correct = new boolean [all_questions.length];
        ans=new int[all_questions.length];
        for (int i=0; i< ans.length; i++){
            ans[i] = -1; //Emplenem la taula amb -1 per a indicar que l'usuari encara no ha respost
        }
        current_question=0;

        // Mètode que creem per a assignar el contingut de text de la pregunta i les respostes.
        showquestion();


        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer();

                if(current_question < all_questions.length-1){
                    current_question++;
                }else{
                    int corrects =0, incorrects =0;
                    for(boolean b: answer_is_correct){
                        if(b) corrects++;
                        else incorrects++;
                    }
                    boolean contains = false;

                    for (int i=0; i<ans.length;i++){
                        if(ans[i]== -1){ contains = true;}
                    }
                    // En cas de trobar respostes no seleccionades ensenya un Toast indicant que has de secleccionar algo.
                    if (contains){
                        Toast.makeText(MultiQuizActivity.this, R.string.select, Toast.LENGTH_LONG).show();
                    }else {
                        String ok = getResources().getString(R.string.ok);
                        String notok = getResources().getString(R.string.notok);
                        //Soluciono el problema de que per expresar els resultats no tens traduccions d'idiomes en el video.
                        String results = String.format(Locale.getDefault(), "%s: %d    %s: %d", ok, corrects, notok, incorrects);
                        Toast.makeText(MultiQuizActivity.this, results, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                showquestion();

            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer();
                if (current_question >0){
                    current_question--;
                    showquestion();
                }
            }

        });


    }

    private void checkAnswer() {
        int id = group.getCheckedRadioButtonId();
        //Log.i("Mcoll",String.format("valor id %d",id));
        int index = -1;
        for(int i=0; i< id_answers.length; i++){
            if (id_answers[i] == id){
                index = i;
            }
        }
        answer_is_correct[current_question]= (index == correct_answer);
        ans[current_question] = index;
    }

    private void showquestion() {

        String q = all_questions[current_question];
        String[] parts = q.split(";");
        group.clearCheck();
        text_question.setText(parts[0]);

        for (int i=0; i<id_answers.length; i++) {
            RadioButton rb = (RadioButton) findViewById(id_answers[i]);
            String answer = parts[i+1];
            if ( answer.charAt(0) == '*'){ // Assignem la resposta correcta a la que te l'*
                correct_answer = i ;
                answer = answer.substring(1); //Arregla que no apareixi l' *
            }
            rb.setText(answer);
            if(ans[current_question]== i){
                rb.setChecked(true);
            }
        }

        if (current_question==0){
            btn_previous.setVisibility(View.GONE);
        } else btn_previous.setVisibility(View.VISIBLE);


        if (current_question == all_questions.length-1){
            btn_next.setText(R.string.finish);
        } else btn_next.setText(R.string.next);
    }
}
