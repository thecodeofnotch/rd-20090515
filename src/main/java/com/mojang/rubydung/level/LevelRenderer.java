package com.mojang.rubydung.level;

public class LevelRenderer {

    private static final int CHUNK_SIZE = 8;

    private final Chunk[] chunks;

    private final int chunkAmountX;
    private final int chunkAmountY;
    private final int chunkAmountZ;

    /**
     * Create renderer for level
     *
     * @param level The rendered level
     */
    public LevelRenderer(Level level) {
        // Calculate amount of chunks of level
        this.chunkAmountX = level.width / CHUNK_SIZE;
        this.chunkAmountY = level.depth / CHUNK_SIZE;
        this.chunkAmountZ = level.height / CHUNK_SIZE;

        // Create chunk array
        this.chunks = new Chunk[this.chunkAmountX * this.chunkAmountY * this.chunkAmountZ];

        // Fill level with chunks
        for (int x = 0; x < this.chunkAmountX; ++x) {
            for (int y = 0; y < this.chunkAmountY; ++y) {
                for (int z = 0; z < this.chunkAmountZ; ++z) {
                    // Calculate min bounds for chunk
                    int minChunkX = x * CHUNK_SIZE;
                    int minChunkY = y * CHUNK_SIZE;
                    int minChunkZ = z * CHUNK_SIZE;

                    // Calculate max bounds for chunk
                    int maxChunkX = (x + 1) * CHUNK_SIZE;
                    int maxChunkY = (y + 1) * CHUNK_SIZE;
                    int maxChunkZ = (z + 1) * CHUNK_SIZE;

                    // Check for chunk bounds out of level
                    maxChunkX = Math.min(level.width, maxChunkX);
                    maxChunkY = Math.min(level.depth, maxChunkY);
                    maxChunkZ = Math.min(level.height, maxChunkZ);

                    // Create chunk based on bounds
                    Chunk chunk = new Chunk(level, minChunkX, minChunkY, minChunkZ, maxChunkX, maxChunkY, maxChunkZ);
                    this.chunks[(x + y * this.chunkAmountX) * this.chunkAmountZ + z] = chunk;
                }
            }
        }
    }

    /**
     * Render all chunks of the level
     *
     * @param layer The render layer
     */
    public void render(int layer) {
        for (Chunk chunk : this.chunks) {
            chunk.render(layer);
        }
    }

    /**
     * Mark all chunks inside of the given area as dirty.
     *
     * @param minX Minimum on X axis
     * @param minY Minimum on Y axis
     * @param minZ Minimum on Z axis
     * @param maxX Maximum on X axis
     * @param maxY Maximum on Y axis
     * @param maxZ Maximum on Z axis
     */
    public void setDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        // To chunk coordinates
        minX /= CHUNK_SIZE;
        minY /= CHUNK_SIZE;
        minZ /= CHUNK_SIZE;
        maxX /= CHUNK_SIZE;
        maxY /= CHUNK_SIZE;
        maxZ /= CHUNK_SIZE;

        // Minimum and maximum y
        minY = Math.max(0, minY);
        maxY = Math.min(15, maxY);

        // Mark all chunks as dirty
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    // Get chunk at this position
                    Chunk chunk = this.chunks[(x + y * this.chunkAmountX) * this.chunkAmountZ + z];

                    // Set dirty
                    chunk.setDirty();
                }
            }
        }
    }
}