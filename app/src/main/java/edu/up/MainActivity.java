package edu.up;

import android.os.Bundle;
import android.view.View;
import android.graphics.Color;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Main activity for Face Maker.
 * DATE: 2/17/2026
 * @author Farid Socias
 */

public class MainActivity extends AppCompatActivity {
    private static final int RED_MASK_SHIFT = 16;
    private static final int GREEN_MASK_SHIFT = 8;
    private static final int COLOR_COMPONENT_MASK = 0xFF;
    private FaceSurfaceView faceView;
    private Face face;
    private Spinner hairStyleSpinner;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private RadioGroup featureRadioGroup;
    private Button randomFaceButton;

    private enum FaceFeature { HAIR,  EYES,  SKIN }

    private FaceFeature currentFeature = FaceFeature.EYES;

    private boolean ignoreSeekEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        connectViews();
        setupSpinner();
        setupRadioGroup();
        setupSeekBars();
        setupRandomButton();

        /* [5% Part B]

         * this is the startup behavior; Face is already randomized in Face constructor
         * and the ui must match the Face values and show the random face
         */
        syncUserInterfaceToFace();
        faceView.drawNow();

        applyInsets();
    }

    private void connectViews() {
        faceView = findViewById(R.id.faceSurface);
        face = faceView.getFace();

        hairStyleSpinner = findViewById(R.id.hairSpinner);

        redSeekBar = findViewById(R.id.redSeekBar);
        greenSeekBar = findViewById(R.id.greenSeekBar);
        blueSeekBar = findViewById(R.id.blueSeekBar);

        featureRadioGroup = findViewById(R.id.faceRadioGroup);
        randomFaceButton = findViewById(R.id.randomFaceButton);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.hair_styles,
                        android.R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        hairStyleSpinner.setAdapter(adapter);

        /* [12% Part B]
         * Spinner changes hair style. Update model and redraw.
         */
        hairStyleSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        face.setHairStyle(position);
                        faceView.drawNow();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    private void setupRadioGroup() {
        /* [18% Part B]
         * Radio buttons switch the target feature.
         * SeekBars must jump to the current feature's color.
         */
        featureRadioGroup.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    currentFeature = targetFromRadioId(checkedId);
                    setSeekBarsFromColor(colorForCurrentFeature());
                }
        );
    }

    private FaceFeature targetFromRadioId(int checkedId) {
        if (checkedId == R.id.hairRadioGroup) {
            return FaceFeature.HAIR;
        }
        if (checkedId == R.id.skinRadioGroup) {
            return FaceFeature.SKIN;
        }
        return FaceFeature.EYES;
    }

    private void setupSeekBars() {
        SeekBar.OnSeekBarChangeListener listener =
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {
                        /* [15% Part B]
                         * SeekBars set the color of the selected feature.
                         * Ignore callbacks when we set SeekBars programmatically.
                         */
                        if (ignoreSeekEvents) {
                            return;
                        }

                        int color = buildColorFromSeekBars();
                        setColorForCurrentFeature(color);
                        faceView.drawNow();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                };

        redSeekBar.setOnSeekBarChangeListener(listener);
        greenSeekBar.setOnSeekBarChangeListener(listener);
        blueSeekBar.setOnSeekBarChangeListener(listener);
    }

    private void setupRandomButton() {
        /* [5% Part B]
         * Random Face button creates a new random face and updates UI to match.
         */
        randomFaceButton.setOnClickListener(v -> {
            face.randomize();
            syncUserInterfaceToFace();
            faceView.drawNow();
        });
    }

    private void syncUserInterfaceToFace() {
        hairStyleSpinner.setSelection(face.getHairStyle());

        featureRadioGroup.check(R.id.eyeRadioGroup);
        currentFeature = FaceFeature.EYES;

        setSeekBarsFromColor(face.getEyeColor());
    }

    private int buildColorFromSeekBars() {
        int r = redSeekBar.getProgress();
        int g = greenSeekBar.getProgress();
        int b = blueSeekBar.getProgress();
        return Color.rgb(r, g, b);
    }

    private int colorForCurrentFeature() {
        if (currentFeature == FaceFeature.HAIR) {
            return face.getHairColor();
        }
        if (currentFeature == FaceFeature.SKIN) {
            return face.getSkinColor();
        }
        return face.getEyeColor();
    }

    private void setColorForCurrentFeature(int color) {
        if (currentFeature == FaceFeature.HAIR) {
            face.setHairColor(color);
            return;
        }
        if (currentFeature == FaceFeature.SKIN) {
            face.setSkinColor(color);
            return;
        }
        face.setEyeColor(color);
    }

    private void setSeekBarsFromColor(int color) {
        ignoreSeekEvents = true;
        redSeekBar.setProgress(redFromColor(color));
        greenSeekBar.setProgress(greenFromColor(color));
        blueSeekBar.setProgress(blueFromColor(color));
        ignoreSeekEvents = false;
    }

    private int redFromColor(int color) {
        return (color >> RED_MASK_SHIFT) & COLOR_COMPONENT_MASK;
    }

    private int greenFromColor(int color) {
        return (color >> GREEN_MASK_SHIFT) & COLOR_COMPONENT_MASK;
    }

    private int blueFromColor(int color) {
        return color & COLOR_COMPONENT_MASK;
    }

    private void applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars() );
                    v.setPadding(systemBars.left,  systemBars.top,  systemBars.right,  systemBars.bottom);
                    return insets;
                }
        );
    }
}