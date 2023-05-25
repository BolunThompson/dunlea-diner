package com.mygdx.game;

import java.util.Optional;

class DayState {
    final String mapFile;
    final String name;

    final int wantedOrders;
    int orders;

    final float maxTime = 180;
    float currentTime;

    final Optional<DayState> nextDay;

    DayState(String mapFile, String name, int wantedOrders, Optional<DayState> nextDay) {
        this.mapFile = mapFile;
        this.name = name;
        this.wantedOrders = wantedOrders;
        this.nextDay = nextDay;
    }

    DayState(String mapFile, String name, int wantedOrders, DayState nextDay) {
        this(mapFile, name, wantedOrders, Optional.of(nextDay));
    }

    DayState(String mapFile, String name, int wantedOrders) {
        this(mapFile, name, wantedOrders, Optional.empty());
    }

    void reset() {
        this.orders = 0;
        this.currentTime = 0;
    }

    boolean isOver() {
        return /* this.orders >= this.wantedOrders || */ this.currentTime >= this.maxTime;
    }

    boolean isWon() {
        return this.orders >= this.wantedOrders;
    }

}
