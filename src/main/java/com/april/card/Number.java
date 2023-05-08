package com.april.card;

public enum Number {
    ACE(12),
    TWO(13),
    THREE(1),
    FOUR(2),
    FIVE(3),
    SIX(4),
    SEVEN(5),
    EIGHT(6),
    NINE(7),
    TEN(8),
    JACK(9),
    QUEEN(10),
    KING(11);

    private final int level;

    Number(int level) {
        this.level = level;
    }

	// 大於
    public boolean greaterThan(Number other) {
        return this.level > other.level;
    }

	// 小於
    public boolean lessThan(Number other) {
        return this.level < other.level;
    }

	// 相等
    public boolean equals(Number other) {
        return this.level == other.level;
    }

	// 取得實際代表的數字
    public int getValue() {
		return (this.level + 1) % 13 + 1;
    }
}
