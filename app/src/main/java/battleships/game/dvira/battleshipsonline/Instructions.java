package battleships.game.dvira.battleshipsonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Instructions extends AppCompatActivity implements View.OnClickListener{

    TextView instructionsBody;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        instructionsBody = (TextView) findViewById(R.id.instructionsTxt);
        instructionsBody.setMovementMethod(new ScrollingMovementMethod());

        backButton = (Button) findViewById(R.id.instructionsBackBtn);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == backButton.getId()){
            Intent i = new Intent(Instructions.this, Menu.class);
            startActivity(i);
        }
    }
}
