package com.letruxux.more_curios_slots;

public enum SlotName {
    HEAD("head"),
    FEET("feet"),
    HANDS("hands"),
    CHARM("charm"),
    BELT("belt"),
    RING("ring"),
    BACK("back"),
    BRACELET("bracelet"),
    NECKLACE("necklace"),
    BODY("body"),
    CURIO("curio");

    private final String value;

    SlotName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
