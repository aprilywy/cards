package com.april.card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum Type {
    STRAIGHT_FLUSH(3),  // 同花順
    FOUR_OF_A_KIND(2),  // 鐵支
    FULL_HOUSE(1),  // 葫蘆
    STRAIGHT(1),    // 順子
    PAIR(1),    // 一對
    NONE(1);    // 孤支

    // level為使用階級作為出牌驗證，大的階級才能蓋過小的階級，同階級也要是同牌型
    private final int level;

    Type(int level) {
        this.level = level;
    }

    public boolean levelGreaterThan(Type other) {
        if (other == null) {
            return true;
        }
        return this.level > other.level;
    }

    public boolean levelLessThan(Type other) {
        if (other == null) {
            return false;
        }
        return this.level < other.level;
    }

    public boolean levelEquals(Type other) {
        if (other == null) {
            return false;
        }
        return this.level == other.level;
    }

    // 檢驗牌型的各種方法
    // 同花順檢驗
    public static boolean isStraightFlush(ArrayList<Card> cards) {
//        if (cards.size() != 5) {
//            return false;
//        }

        // 同時滿足同花和順子的條件
        return Type.isStraight(cards) && Type.isFlush(cards);
    }

    // 鐵支檢驗
    public static boolean isFourOfAKind(ArrayList<Card> cards) {
        if (cards.size() != 5) {
            return false;
        }
        Number number = cards.get(0).getNumber();

        // 計算不同數字出現幾次
        Map<Number, Integer> map = new HashMap<>();
        for (Card card : cards) {
            if (map.containsKey(card.getNumber())) {
                int tmp = map.get(card.getNumber());
                map.put(card.getNumber(), tmp + 1);
            } else {
                map.put(card.getNumber(), 1);
            }
        }

        // 只有兩種數字
        boolean onlyContainTwoNumbers = map.keySet().size() == 2;

        // 數字的數量不是 1 張就是 4 張
        boolean isCombinedByOneAndFourNumbers = map.get(number) == 1 || map.get(number) == 4;

        // 同時滿足才是鐵支
        return onlyContainTwoNumbers && isCombinedByOneAndFourNumbers;
    }

    // 葫蘆檢驗
    public static boolean isFullHouse(ArrayList<Card> cards) {
        if (cards.size() != 5) {
            return false;
        }
        Number number = cards.get(0).getNumber();
        Map<Number, Integer> map = new HashMap<>();
        for (Card card : cards) {
            if (map.containsKey(card.getNumber())) {
                int tmp = map.get(card.getNumber());
                map.put(card.getNumber(), tmp + 1);
            } else {
                map.put(card.getNumber(), 1);
            }
        }

        // 只有兩種數字
        boolean onlyContainTwoNumbers = map.keySet().size() == 2;

        // 數字的數量不是 2 張就是 3 張
        boolean isCombinedByTwoAndThreeNumbers = map.get(number) == 2 || map.get(number) == 3;

        // 同時符合才是葫蘆
        return onlyContainTwoNumbers && isCombinedByTwoAndThreeNumbers;
    }

    // 滿足順子的條件
    private static boolean isStraight(ArrayList<Card> cards) {
        if (cards.size() != 5) {
            return false;
        }

        // 先根據數字做排序
        cards.sort(Card.getComparatorWithValue());
        int previous = cards.get(0).getNumber().getValue();

        // 只要後一張數字是前一張數字 -1 就是連續的，連續 5 張就是順子
        for (int i = 0; i < 5; i ++) {
            if (cards.get(i).getNumber().getValue() - previous != 1) {
                return false;
            }
            previous = cards.get(i).getNumber().getValue();
        }

        return true;
    }

    // 滿足同花的條件
    private static boolean isFlush(ArrayList<Card> cards) {
        if (cards.size() != 5) {
            return false;
        }

        Suit suit = cards.get(0).getSuit();

        // 全部花色相同
        for (Card card : cards) {
            if (!card.getSuit().equals(suit)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPair(ArrayList<Card> cards) {
        if (cards.size() != 2) {
            return false;
        }

        // 兩張數字一致
        return cards.get(0).getNumber().equals(cards.get(1).getNumber());
    }

    // 辨別牌型
    public static Type getType(ArrayList<Card> cards) {
        int size = cards.size();
        if (size == 5) {
            if (isStraightFlush(cards)) {
                return Type.STRAIGHT_FLUSH;
            } else if (isFourOfAKind(cards)) {
                return Type.FOUR_OF_A_KIND;
            } else if (isFullHouse(cards)) {
                return Type.FULL_HOUSE;
            } else if (isStraight(cards)) {
                return Type.STRAIGHT;
            }
        } else if (size == 2) {
            if (isPair(cards)) {
                return Type.PAIR;
            }
        } else if (size == 1) {
            return Type.NONE;
        }
        return null;
    }
}
