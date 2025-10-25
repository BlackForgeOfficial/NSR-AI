package com.nsr.ai.api;

import java.util.UUID;

/**
 * Represents a snapshot of a pet's data at a specific moment.
 * This class is immutable.
 */
public class PetDataSnapshot {
    private final UUID owner;
    private final String data; // Placeholder for actual pet data

    /**
     * Constructs a new PetDataSnapshot.
     * @param owner The UUID of the pet's owner.
     * @param data A string representation of the pet's data (e.g., personality, mood, bond level).
     */
    public PetDataSnapshot(UUID owner, String data) {
        this.owner = owner;
        this.data = data;
    }

    /**
     * Gets the UUID of the pet's owner.
     * @return The owner's UUID.
     */
    public UUID getOwner() { return owner; }
    /**
     * Gets the string representation of the pet's data.
     * @return The pet's data string.
     */
    public String getData() { return data; }
}
