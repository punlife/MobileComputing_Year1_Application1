package com1032.cw1.ld00245.ld00245_todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeScreen extends AppCompatActivity {
    private Button loadButton;
    private Button createButton;
    DataStorage ds;
    FileOutputStream outputStream;
    AlertDialog.Builder builder;
    List<String> items;
    List<String> subitems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ds = new DataStorage();
        loadButton =(Button) findViewById(R.id.button_load_list);
        loadButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Upon clicking on the Load button, a dialog box with a list of all the files is loaded
             * so that the user can choose which list to load. Then the data is assigned to fields
             * and passed to List_Activity via intent.
             */
            public void onClick(View v){
                captureFileList();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                String[] temporary = ds.getFilenamelist().toArray(new String[0]);
                builder.setTitle(R.string.Load_list_dialog_title);
                if (ds.getFilelist().length != 0) {
                    builder.setItems(temporary, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            loadFiles(ds.getFilelist()[which].getName(), "items");
                            loadFiles("|details|" + ds.getFilelist()[which].getName(), "subitems");
                            Intent listActivity = new Intent(HomeScreen.this, List_Activity.class);
                            listActivity.putExtra("name", ds.getFilelist()[which].getName());
                            listActivity.putStringArrayListExtra("items", (ArrayList<String>) ds.getListItems());
                            listActivity.putStringArrayListExtra("subitems", (ArrayList<String>) ds.getListSubitems());
                            startActivity(listActivity);
                            finish();
                        }
                    });
                }
                else {
                    builder.setMessage(R.string.Load_list_dialog_message);
                    builder.setPositiveButton(R.string.Load_list_dialog_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Log.v("Button", "Return button pressed");
                        }
                    });
                }
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                    }
                });
                dialog.show();
                Log.v("List_Activity", "Load_list button pressed");
            }

        });
        createButton =(Button) findViewById(R.id.button_create_list);
        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                builder = new AlertDialog.Builder(HomeScreen.this);
                LayoutInflater inflater = HomeScreen.this.getLayoutInflater();
                Log.i("INFO", "Creating Dialog...");
                final View view = inflater.inflate(R.layout.createlist_dialog_layout, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(view)
                        .setPositiveButton(R.string.Create_list_dialog_positive, new DialogInterface.OnClickListener() {
                            /**
                             *When the Positive button is clicked data is assigned to the relevant fields and
                             * the files are created with relevant filenames, the information is then passed
                             * to the List_Activity via the use of intents.
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText input = (EditText) view.findViewById(R.id.editText);
                                Intent listActivity = new Intent(HomeScreen.this, List_Activity.class);
                                items = new ArrayList<>();
                                subitems = new ArrayList<>();
                                ds.setItemFilename(input.getText().toString());
                                ds.setSubitemFilename(input.getText().toString());
                                saveFiles();
                                listActivity.putExtra("name", input.getText().toString());
                                listActivity.putStringArrayListExtra("items", (ArrayList<String>) items);
                                listActivity.putStringArrayListExtra("subitems", (ArrayList<String>) subitems);
                                startActivity(listActivity);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.Create_list_dialog_negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do Nothing
                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                    }
                });
                dialog.show();
                Log.v("List_Activity", "Create_list button pressed");

            }

        });
        Log.v("HomeScreen", "HomeScreen created");
    }

    /**
     * The four methods below are used for logging the activity changing state.
     */
    @Override
    public void onResume(){
        super.onResume();
        Log.v("HomeScreen", "HomeScreen Resumed");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.v("HomeScreen", "HomeScreen Paused");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.v("HomeScreen", "HomeScreen Stopped");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("HomeScreen", "HomeScreen Destroyed");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    /**
     *
     * @param item Item of the option selected
     * @return
     *
     * The method allows for two types of deletion of files from the options menu available.
     * One type deletes just the specific file while the other deletes all stores lists.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.delete_list) {
            captureFileList();
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
            String[] temporary = ds.getFilenamelist().toArray(new String[0]);
            builder.setTitle(R.string.delete_list);
            if (ds.getFilelist().length != 0) {
                builder.setItems(temporary, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ds.getFilelist()[which].delete();
                        File file = new File(getFilesDir(),"|details|"+ds.getFilelist()[which].getName());
                        file.delete();
                        Toast.makeText(HomeScreen.this, R.string.list_Deleted_Message,Toast.LENGTH_SHORT);
                    }
                });
            }
            else {
                builder.setMessage(R.string.Load_list_dialog_message);
                builder.setPositiveButton(R.string.Load_list_dialog_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("Button", "Return button pressed");
                    }
                });
            }
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                }
            });
            dialog.show();
        }
        if (id == R.id.delete_all_lists) {
            captureFileList();
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
            builder.setTitle(R.string.delete_all_list)
                    .setMessage(R.string.delete_all_list_message)
                    .setPositiveButton(R.string.add_list_dialog_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (ds.getFilelist().length != 0){
                                for (File file : ds.getFilelist()) {
                                    file.delete();
                                    File detailfile = new File(getFilesDir(), "|details|" + file.getName());
                                    detailfile.delete();
                                    Toast.makeText(HomeScreen.this, R.string.lists_Deleted_Message, Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    })
                    .setNegativeButton(R.string.Create_list_dialog_negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method overrides the onBackPressed() method in order to allow for a confirmation before
     * exiting the application.
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setTitle(R.string.Backpresshomescreen_dialog_title)
                .setMessage(R.string.Backpresshomescreen_dialog_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.Black));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.Black));
            }
        });
        dialog.show();
    }

    /**
     * As the name suggests, this method is used for saving the files onto the internal storage.
     * Both the item and subitem files are saved in order to maintain synchronicity and avoid
     * difference in indexes between the two files.
     */
    public void saveFiles(){
        try {
            outputStream = openFileOutput(ds.getItemFilename(), getApplicationContext().MODE_PRIVATE);
            ds.setFinalItemString();
            Log.v("List_Activity", "Pre-file save");
            outputStream.write(ds.getFinalItemString().getBytes());
            Log.v("List_Activity", "Post-file save");
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outputStream = openFileOutput(ds.getSubitemFilename(), getApplicationContext().MODE_PRIVATE);
            ds.setFinalSubitemString();
            Log.v("List_Activity", "Pre-file save");
            outputStream.write(ds.getFinalSubitemString().getBytes());
            Log.v("List_Activity", "Post-file save");
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filename = Name of the file to load
     * @param type = Type of file to load (2 types of files available at the moment with potential for expansion)
     *
     * This method retrieves information from a file in the internal storage and then splits the file using the
     * "@" delimiter. Each of the "broken" strings are then added to their relevant list.
     */
    public void loadFiles(String filename, String type){
        StringBuilder text = new StringBuilder();
        String filePath = getApplicationContext().getFilesDir() + "/"+filename;
        File file = new File( filePath );
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.trimToSize();
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String[] dataLoaded = text.toString().split("@");
        if (type == "items") {
            for (String s : dataLoaded) {
                ds.addToListItems(s);
                Log.v("Data loaded", s);
            }
        }
        else if (type == "subitems") {
            for (String s : dataLoaded) {
                ds.addToListSubitems(s);
                Log.v("Data loaded", s);
            }
        }
    }

    /**
     * This method is used for retrieving the list of Files that have been created in the internal
     * storage previously. One array contains the filelist while the list contains the names of the
     * files in the same order as the filelist.
     *
     * temporaryFileList = Stores the list of all files in the internal storage (for this app).
     */
    public void captureFileList(){
        ds.setFilelist(null);
        ds.setFilenamelist(new ArrayList<String>());
        String filepath = getApplicationContext().getFilesDir().toString();
        Log.d("Files", "Path: " + filepath);
        File path = new File(filepath);
        File[] temporaryFileList = path.listFiles();

        Log.d("Files", "Size: " + temporaryFileList.length);
        for (int i=0; i < temporaryFileList.length; i++)
        {
            if (!temporaryFileList[i].getName().contains("|details|")){
                ds.addToFilenamelist(temporaryFileList[i].getName());
                ds.addToPreconversionfilelist(temporaryFileList[i]);
                Log.d("Files", "FileName:" + temporaryFileList[i].getName());
            }
            else {
                Log.d("Files", "Additional file found:" + temporaryFileList[i].getName());
            }

        }
        ds.convertToArray();
    }
}
