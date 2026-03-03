package com.cainiao.base.util;

enum Level {
    ERROR(4), WARN(3), INFO(2), DEBUG(1), TRACE(0);
    int level;

    Level(int level) {
        this.level = level;
    }

    public int asInt() {
        return level;
    }
}