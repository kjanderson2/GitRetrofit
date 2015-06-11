package kjanderson2.gitretrofit.ui;

import com.activeandroid.query.Select;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import kjanderson2.gitretrofit.model.GitModel;
import kjanderson2.gitretrofit.rest.GitApi;
import kjanderson2.gitretrofit.R;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity {

    Button click;
    TextView tv;
    EditText edit_user;
    ProgressBar pbar;
    String API = "https://api.github.com";                //BASE URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.tv);
        edit_user = (EditText) findViewById(R.id.edit);
        pbar = (ProgressBar) findViewById(R.id.pb);
        pbar.setVisibility(View.INVISIBLE);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edit_user.getText().toString();
                pbar.setVisibility(View.VISIBLE);

                //Retrofit section start from here...
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(API).build();                    //create an adapter for retrofit with base url

                GitApi git = restAdapter.create(GitApi.class);        //creating a service for adapter with our GET class

                //Now ,we need to call for response
                //Retrofit using gson for JSON-POJO conversion

                GitModel item = new Select().from(GitModel.class).where("Username = ?", user).executeSingle();

                if(item!=null){
                    populateTV(item);
                    Log.d("MAINACTIVITY","from db");
                } else {

                git.getFeed(user,new Callback<GitModel>() {
                    @Override
                    public void success(GitModel gitmodel, Response response) {
                        //we get json object from github server to our POJO or model class
                        populateTV(gitmodel);
                        pbar.setVisibility(View.INVISIBLE);           //disable progressbar

                        GitModel user = new GitModel();
                        user.setLogin(gitmodel.getLogin());
                        user.setIdentification(gitmodel.getIdentification());
                        user.setName(gitmodel.getName());
                        user.setCompany(gitmodel.getCompany());
                        user.setBlog(gitmodel.getBlog());
                        user.setEmail(gitmodel.getEmail());
                        user.save();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tv.setText(error.getMessage());
                        pbar.setVisibility(View.INVISIBLE);           //disable progressbar
                    }
                });
            }}
        });
    }

    private void populateTV(GitModel gitmodel){
        tv.setText("Github ID: " + gitmodel.getIdentification()+"\nGithub Name :"+gitmodel.getName()+"\nWebsite :"+gitmodel.getBlog()+"\nCompany Name :"+gitmodel.getCompany());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}