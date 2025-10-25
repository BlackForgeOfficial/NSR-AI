package com.nsr.ai.api;

/**
 * Listener interface for pet-related events.
 */
public interface PetListener {
    /**
     * Called when a pet-related event occurs.
     * @param petData A snapshot of the pet's data at the time of the event.
     */
    void onPetEvent(PetDataSnapshot petData);
}
