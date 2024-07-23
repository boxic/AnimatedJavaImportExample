package game.slam.util.animation.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AnimationBlueprint(
        BlueprintSettings blueprint_settings,
        Resources resources,
        Rig rig,
        List<Animation> animations
) {
    public record BlueprintSettings(
            // The namespace to export the project to. This is the namespace that will be used in the exported Resource Pack and Data Pack.
            String export_namespace,
            // Whether or not to show the bounding box in the editor.
            boolean show_bounding_box,
            // Whether or not to automatically calculate the bounding box based on the model's geometry.
            // NOTE: The auto bounding box will NOT take bone offsets from animations into account, so the bounding box may be smaller than needed in some cases.
            boolean auto_bounding_box,
            // Determines the culling box of the model. The model will stop rendering when this box is off-screen.
            List<Integer> bounding_box,
            // Determines how the Resource Pack should be exported.
            // Raw - Exports the Resource Pack as a folder.
            // Zip - Exports the Resource Pack as a .zip file.
            // None - Disables Resource Pack exporting.
            String resource_pack_export_mode,
            // The item to display the Blueprints models in-game. Multiple Blueprints can be placed on the same item and they will be merged automatically.
            String display_item,
            // The offset to use for the Custom Model Data of the Display Item. Allows multiple Blueprints on the same item, but in separate, unaffiliated Resource Packs.
            int custom_model_data_offset,
            // Whether or not to enable the advanced Resource Pack settings. (Individual RP Folder Selection)
            boolean enable_advanced_resource_pack_settings,
            // The root folder of the Resource Pack to export the project into.
            String resource_pack,
            // Where to place the Display Item. This should be a path to a .json file in a Resource Pack.
            String display_item_path,
            // Where to place all of the exported models. This should be a path to a folder in a Resource Pack.
            String model_folder,
            // Where to place all of the exported textures. This should be a path to a folder in a Resource Pack.
            String texture_folder,
            // Whether or not to bake the exported animations.
            // Baked animations have their frames pre-calculated and stored in the exported JSON file, reducing the complexity of rendering the model in-game.
            // Some Plugins may require this to be enabled to function correctly.
            boolean baked_animations,
            String json_file,
            int customModelDataOffset
    ) {}

    public record Resources(
            String textureExportFolder,
            String modelExportFolder,
            String displayItemPath,
            // Map<UUID, Model> models, // Not currently needed - useful for server-side resource pack generation
            // Map<UUID, Map<UUID, Model>> // Not currently needed  - useful for server-side resource pack generation
            Map<UUID, Texture> textures // Also not currently needed, but it's already here - useful for server-side resource pack generation
    ) {
        public record Texture(
                String name,
                // The ID of the texture used when making vanilla models.
                String id,
                // The path in the resource pack that the models that reference this texture expect the texture to be at.
                String expectedPath,
                // A data URL containing the texture image.
                String src
        ) {}
    }

    public record Rig(
            List<NodeTransform> default_transforms,
            Map<UUID, Node> node_map,
            NodeStructure node_structure,
            // A map of variant UUIDs to variant objects.
            Map<UUID, Variant> variants
    ) {
        public record Node(
                NodeType type,
                UUID parent,
                String name,
                UUID uuid,
                String modelPath,
                int customModelData,
                String resourceLocation,
                BoundingBox boundingBox,
                Map<UUID, BoneConfig> configs,
                float scale
        ) {
            public record BoundingBox(
                    List<Float> min,
                    List<Float> max
            ) {}

            public record BoneConfig(
                    String billboard,
                    boolean override_brightness,
                    float brightness_override,
                    boolean enchanted,
                    boolean glowing,
                    boolean override_glow_color,
                    String glow_color,
                    boolean inherit_settings,
                    boolean invisible,
                    String nbt,
                    float shadow_radius,
                    float shadow_strength,
                    boolean use_nbt
            ) {}
        }

        public record NodeStructure(
                UUID uuid,
                List<NodeStructure> children
        ) {}

        // A variant of the rig. Variants are used to change the appearance of the rig by swapping out different textures at runtime.
        public record Variant(
                // The name of the variant.
                String name,
                // The display name of the variant. Only used for fancy display purposes such as UI elements.
                String display_name,
                UUID uuid,
                // A map of default texture UUID -> variant texture UUID. If a texture is not in this map, it will be assumed to be the same as the default texture.
                Map<UUID, UUID> texture_map,
                // A list of node UUIDs that should be excluded / ignored when this variant is applied.
                List<UUID> excluded_nodes,
                // Whether or not this variant is the default variant.
                boolean is_default
        ) {}
    }

    public record Animation(
            String name,
            String storageSafeName,
            int duration,
            float loopDelay,
            String loopMode,
            List<Frame> frames,
            List<UUID> includedNodes,
            // Custom data
            boolean reversed,
            int tickRatio
    ) {
        public record Frame(
                float time,
                List<NodeTransform> node_transforms,
                UUID variant
        ) {}
    }

    public record NodeTransform(
            NodeType type,
            String name,
            UUID uuid,
            // The transformation matrix of the node. The matrix is a 4x4 matrix in row-major order.
            List<Float> matrix,
            // The decomposed matrix transformation of the node.
            Transformation transformation,
            // A vector3 representing the position of the node.
            List<Float> pos,
            // A vector3 representing the rotation of the node.
            List<Float> rot,
            // A vector2 representing the head rotation of the node.
            // Used for correctly rotating located entities, and cameras, as they don't have access to a Z axis.
            List<Float> head_rot,
            // A vector3 representing the scale of the node.
            List<Float> scale,
            // The instant-interpolation mode of the node.
            // - `pre-post`
            String interpolation,
            String commands,
            String execute_condition
    ) {
        public record Transformation(
                // A vector3 representing the translation of the node.
                List<Float> translation,
                // A quaternion representing the left rotation of the node.
                List<Float> left_rotation,
                // A vector3 representing the scale of the node.
                List<Float> scale
        ) {}
    }
}
