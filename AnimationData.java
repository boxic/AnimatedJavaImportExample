package game.slam.util.animation.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class AnimationData {

    // Variables
    private AnimationBlueprint animationBlueprint;

    // Constructor
    public AnimationData(String fileName) {
        try {
            // Grab animation Json file
            String resourcePath = "/animations/" + fileName + ".json";
            InputStream inputStream = AnimationData.class.getResourceAsStream(resourcePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }

            // Create reader
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

            // Create Gson instance and teach it how to serialize/deserialize our custom data types
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(NodeType.class, new NodeType.TypeAdapter())
                    .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
                    .create();

            // Create animation blueprint object from Json
            this.animationBlueprint = gson.fromJson(reader, AnimationBlueprint.class);

            // Close reader
            reader.close();

            // Parse custom data entries
            this.animationBlueprint = this.parseCustomData();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load animation data from " + fileName, e);
        }
    }

    // Methods
    private AnimationBlueprint parseCustomData() {

        // Parse and modify animations
        List<AnimationBlueprint.Animation> modifiedAnimations = this.animationBlueprint.animations().stream()
                .map(this::parseAndModifyAnimation)
                .toList();

        // Return a new copy of the animation blueprint, with our custom data added
        return new AnimationBlueprint(
                this.animationBlueprint.blueprint_settings(),
                this.animationBlueprint.resources(),
                this.animationBlueprint.rig(),
                modifiedAnimations
        );
    }

    private AnimationBlueprint.Animation parseAndModifyAnimation(AnimationBlueprint.Animation original) {

        // Split name from data
        String[] parts = original.name().split("\\|");

        // Set actual name
        String name = parts[0];

        // Get extra data
        boolean reversed;
        int tickRatio;
        // There is extra data
        if (parts.length > 1) {
            // Get data
            int data = Integer.parseInt(parts[1]);
            // Negative = reversed
            reversed = data < 0;
            // Make sure tick ratio is always positive
            tickRatio = Math.abs(data);
        }
        // No extra data, use defaults
        else {
            reversed = false;
            tickRatio = 1;
        }

        // Construct modified Animation
        return new AnimationBlueprint.Animation(
                name,
                original.storageSafeName(),
                original.duration(),
                original.loopDelay(),
                original.loopMode(),
                original.frames(),
                original.includedNodes(),
                reversed,
                tickRatio
        );
    }

    // Getter method to access the parsed data
    public AnimationBlueprint getAnimationBlueprint() {
        return this.animationBlueprint;
    }

    // You can add more methods here to provide easy access to specific parts of the data
    // For example:

    public AnimationBlueprint.BlueprintSettings getBlueprintSettings() {
        return this.animationBlueprint.blueprint_settings();
    }

    public List<AnimationBlueprint.Animation> getAnimations() {
        return this.animationBlueprint.animations();
    }

    public AnimationBlueprint.Animation getAnimationByName(String name) {
        return this.animationBlueprint.animations().stream()
                .filter(anim -> anim.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    // Add more methods as needed for your specific use cases
}