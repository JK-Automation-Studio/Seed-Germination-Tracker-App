package com.jkstudio.germinationtrackerapp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private PieChart chart;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private PieDataSet dataSet;
    private final String FILENAME = "SeedStarter.json";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // set toolbar for app
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // set toolbar menu button color
        toolbar.getOverflowIcon().setTint(getResources().getColor(R.color.white)); // Only thing that has worked to change color of overflow icon

        createChartUI();
        readFromFile();

        //chart.invalidate(); // redraw chart



        // Detect slice clicks using chart gesture
        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Highlight h = chart.getHighlightByTouchPoint(me.getX(), me.getY());
                if (h != null) {
                    int index = (int) h.getX();
                    // Open your dialog here

                    showSlotDialog(index); // custom dialog to pick color + plant name

                }
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });



    } // End of oncreate

    @Override
    protected void onPause() {
        super.onPause();
        writeToFile();
    }
    private void createChartUI() {
        // set default entries in arraylist if empty,
        // create dataset from entries + colors arraylists
        // set chart data to dataset
        // set chart formatting

        //get chart spinner
        chart = findViewById(R.id.germSlotsChart);

        // Default entry listings
        if (entries.isEmpty()) {
            entries = new ArrayList<>();
            entries.add(new PieEntry(1, "Slot 1"));
            entries.add(new PieEntry(1, "Slot 2"));
            entries.add(new PieEntry(1, "Slot 3"));
            entries.add(new PieEntry(1, "Slot 4"));
            entries.add(new PieEntry(1, "Slot 5"));
            entries.add(new PieEntry(1, "Slot 6"));
        }
        // set default grays
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));
        colors.add(ContextCompat.getColor(this, R.color.gray_dark));


        //create dataset with slot entries, colors, slice spacing, etc
        dataSet = new PieDataSet(entries, "Cannakan Slots"); // new dataset with values, label
        dataSet.setColors(colors); // set chart dataset colors to colors ArrayList
        dataSet.setValues(entries); // set chart value set to entries
        dataSet.setSliceSpace(8f); // adds space between slices
        dataSet.setSelectionShift(2f); // adds distance for highlighted selection
        PieData data = new PieData(dataSet); // new data object from dataset

        // set chart data
        chart.setData(data);


        // chart formatting for pretty UI
        chart.setDrawEntryLabels(true); // show labels in chart for plant varietals
        chart.getDescription().setEnabled(false); // do not show description
        chart.getLegend().setEnabled(false); // do not show legend
        chart.getData().setDrawValues(false); // do not show values as "value" for each slot == 1
        chart.setDrawHoleEnabled(false); // remove center circle
        chart.setHighlightPerTapEnabled(false); // true for highlight version, false for onclick version

        chart.invalidate();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create toolbar with menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item clicks for toolbar menu

        int id = item.getItemId();

        if (id == R.id.menuSettings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
            /* TODO
                Create new SettingsActivity.java class
                and activity_settings.xml

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
             */
        }

        return super.onOptionsItemSelected(item);
    }
    private void showSlotDialog(int slotIndex) {
        // create array of color options
        // inflate dialog layout
        // build alert dialog, set view to dialogview
        // get dialog elements
        // create radio buttons for each color, add to radioGroup element
        // listen for confirm or cancel button clicks

        final String[] colorNames = {"Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Gray"};
        final int[] colorValues = {
                getColor(R.color.red),
                getColor(R.color.orange),
                getColor(R.color.yellow),
                getColor(R.color.green),
                getColor(R.color.blue),
                getColor(R.color.purple),
                getColor(R.color.gray_dark)
        };

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_slot_edit, null);

        // Build the AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();


        // variables for dialog elements
        EditText input = dialogView.findViewById(R.id.editPlantName);
        RadioGroup colorGroup = dialogView.findViewById(R.id.colorGroup);

        // Set existing plant name
        input.setText(entries.get(slotIndex).getLabel());
        input.setTextColor(getResources().getColor(R.color.white)); // makes label show color visually
        input.setShadowLayer(2,4,2, R.color.black);

        // Build color options dynamically
        int currentColor = colors.get(slotIndex); // color from saved list
        int checkedIndex = -1;

        // for each color in array, create button with correct name and shadow color, add to radioGroup
        // if current color's button == colors arraylist at index, set button selected
        for (int i = 0; i < colorNames.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setText(colorNames[i]);
            rb.setShadowLayer(2, 4,2, colorValues[i]); // makes label show color visually
            rb.setTextColor(getResources().getColor(R.color.white)); // makes text color white
            rb.setPadding(0,0,6,0); // give each button's text some extra room for shadow
            colorGroup.addView(rb); // add button to group

            // Check the one that matches current color
            if (colorValues[i] == currentColor) {
                checkedIndex = i;
            }
        }

        if (checkedIndex != -1) {
            // if checkedIndex set, get that index's button in colorGroup and set it to checked
            ((RadioButton) colorGroup.getChildAt(checkedIndex)).setChecked(true);
        }


        // variables for dialog buttons
        Button btnYes = dialogView.findViewById(R.id.buttonAddDialog);
        Button btnNo = dialogView.findViewById(R.id.buttonCancelDialog);

        // onclick listener for confirm button
        btnYes.setOnClickListener(view -> {
            // on Confirm click, get input string
            String plantName = input.getText().toString().trim();

            // if name is empty, set to default slot number
            if (plantName.isBlank()){
                plantName = "Slot " + (slotIndex + 1);
            }
            // get which color is selected
            int selectedId = colorGroup.getCheckedRadioButtonId();
            int selectedColor = currentColor;
            if (selectedId != -1) {
                int index = colorGroup.indexOfChild(dialogView.findViewById(selectedId));
                selectedColor = colorValues[index];
            }

            // call updateSlot with proper index, name, color
            updateSlot(slotIndex, plantName, selectedColor);
            // close dialog
            dialog.dismiss();
        });

        // onclick listener for cancel button
        btnNo.setOnClickListener(view -> {
            // close dialog without any actions
            dialog.dismiss();
        });

        // Set dialog bounding box to invisible
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Set background dim amount
        dialog.getWindow().setDimAmount(0.65f);

        // Show the dialog
        dialog.show();
    }

    private void updateSlot(int slotIndex, String plantName, int selectedColor) {

        updateSlotName(slotIndex, plantName); // add to entries
        updateSlotColor(slotIndex, selectedColor); // add to colors
        writeToFile(); // write to file
        //chart.highlightValues(null); // un-highlight any selected

    }

    private void writeToFile() {

        // Create JSONObject root, store rotation angle, list of "slot" arrays each holding a plant name and color
        // then write it to json file FILENAME

        try {
            // Create a JSON object
            JSONObject root = new JSONObject();
            root.put("rotation", chart.getRotationAngle());

            // create plant slots array
            JSONArray slotsArray = new JSONArray();
            for (int i = 0; i < entries.size(); i++) {
                JSONObject slot = new JSONObject();
                slot.put("plant", entries.get(i).getLabel());
                slot.put("color", colors.get(i));
                slotsArray.put(slot);
                Log.i("i", "Plant written: "+slot.getString("plant"));

            }
            root.put("slots", slotsArray); // add slots array to json object, call it "slots"

            // Write to internal storage
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            fos.write(root.toString().getBytes());
            fos.close();

            Log.d("Save", "Chart state saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readFromFile() {

        // Read string from file, create json object from string and
        // finally set up data in app view

        try {

            // read file using file input stream
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            // create json using string builder toString()
            JSONObject root = new JSONObject(sb.toString());

            // set rotation of spinner using rotation stored in root json
            float rotation = (float) root.getDouble("rotation");
            chart.setRotationAngle(rotation);

            // json array for iterating
            JSONArray slots = root.getJSONArray("slots");
            entries.clear();
            colors.clear();
            for (int i = 0; i < slots.length(); i++) {
                JSONObject slot = slots.getJSONObject(i);

                // add items from json to arraylists
                entries.add(new PieEntry(1, slot.getString("plant"))); // add new PieEntry object with label and arbitrary value of 1 to entries arraylist
                colors.add(slot.getInt("color")); // add color to colors
                Log.i("i", "Plant read: "+slot.getString("plant")); // log to logcat

            }

            createChartUI();

            Log.d("Load", "Chart state restored successfully");
        } catch (FileNotFoundException e) {
            // first run, ignore
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void updateSlotName(int slotIndex, String plantName) {
        if (! plantName.isBlank()) {
            entries.get(slotIndex).setLabel(plantName); // set entry's label at index to plantName
            chart.invalidate(); // refresh view
        }
        else {
            entries.get(slotIndex).setLabel("Slot "+(slotIndex+1)); // if empty, default slot num
            chart.invalidate(); // refresh view
        }
    }


    private void updateSlotColor(int slotIndex, int newColor) {
        colors.set(slotIndex, newColor); // set color in colors array
        dataSet.setColors(colors); // set colors for pie chart slices
        chart.invalidate(); // refresh view
    }



}