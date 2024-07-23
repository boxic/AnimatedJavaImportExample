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
            String export_namespace,
            boolean show_bounding_box,
            boolean auto_bounding_box,
            List<Integer> bounding_box,
            String resource_pack_export_mode,
            String display_item,
            int custom_model_data_offset,
            boolean enable_advanced_resource_pack_settings,
            String resource_pack,
            String display_item_path,
            String model_folder,
            String texture_folder,
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
                String id,
                String expectedPath,
                String src
        ) {}
    }

    public record Rig(
            List<NodeTransform> default_transforms,
            Map<UUID, Node> node_map,
            NodeStructure node_structure,
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

        public record Variant(
                String name,
                String display_name,
                UUID uuid,
                Map<UUID, UUID> texture_map,
                List<UUID> excluded_nodes,
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
            List<Float> matrix,
            Transformation transformation,
            List<Float> pos,
            List<Float> rot,
            List<Float> head_rot,
            List<Float> scale,
            String interpolation,
            String commands,
            String execute_condition
    ) {
        public record Transformation(
                List<Float> translation,
                List<Float> left_rotation,
                List<Float> scale
        ) {}
    }
}