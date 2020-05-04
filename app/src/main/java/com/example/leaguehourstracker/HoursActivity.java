package com.example.leaguehourstracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;


public class HoursActivity extends AppCompatActivity {
    private String username;
    private String accountId;
    private double seconds;
    private int totalGames;
    final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    //private Summoner summoner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoursactivity);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        try {
            getHours(username);
        }
        catch (Exception e) {
        }
    }

    public void getHours(String user) throws RiotApiException {
        SendfeedbackJob job = new SendfeedbackJob();
        job.execute(user);
        Log.d("Error", "get hours executed");
    }
    public void startFailActivity(String name) {
        Intent i = new Intent(this, ErrorActivity.class);
        i.putExtra("USERNAME", name);
        startActivity(i);

    }
    public double round(double input) {
        return Math.round(input * 100.0) / 100.0;
    }
    public void convertToHours() {
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        TextView m = findViewById(R.id.message);
        m.setText("Over the last month (" + totalGames
                + " matches), you have spent " + round(seconds) + " seconds, "
                + round(minutes)  + " minutes, and "
                + round(hours) + " hours on League."
                + "\n\nThis is equivalent to " + round(days) + " days. Wow!");
    }

    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            seconds = 0;
            String user = params[0];
            ApiConfig config = new ApiConfig().setKey("YOUR-API-KEY-HERE");
            RiotApi api = new RiotApi(config);
            Summoner summoner = new Summoner();
            try {
                summoner = api.getSummonerByName(Platform.NA, user);
            }
            catch(Exception e) {
                Log.d("Error", "Error is " + e.toString());
            }
            Log.d("Error", "This is summoner toString()" + summoner.toString());
            accountId = summoner.getAccountId();
            try {
                MatchList matchlist = api.getMatchListByAccountId(Platform.NA, accountId);
                totalGames = 0;
                try {
                    for (MatchReference match : matchlist.getMatches()) {
                        long gameId = match.getGameId();
                        Match realMatch = api.getMatch(Platform.NA, gameId);
                        // checks if the match was played in the last month
                        if (realMatch.getGameCreation() >= (System.currentTimeMillis() - (31 * DAY_IN_MS))) {
                            totalGames++;
                            seconds += realMatch.getGameDuration();
                        }
                    }
                } catch (RiotApiException e) {
                }
                convertToHours();
                Log.d("Error", "Total number is " + matchlist.getTotalGames());
                return Integer.toString(matchlist.getTotalGames());
            }
            catch(Exception e) {

                return "failure";
            }
        }

        @Override
        protected void onPostExecute(String message) {
            if (message.equals("failure")) {
                Log.d("Error", "Complete failure");
                startFailActivity(username);
            } else {
                Button but = findViewById(R.id.hoursSearch);
                but.setText("Search again");
                but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            }

        }
        }
    }

