package com.april.card;


import java.util.Comparator;

import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter
public class Card {
    private Suit suit;
    private Number number;

    public Card(Suit suit, Number number) {
        this.suit = suit;
        this.number = number;
    }

    @Override
    public String toString() {
        return this.suit + "-" + this.number;
    }

    @Override
    public boolean equals(Object object) {
        // 如果 object 屬於 Card 實例
        if (object instanceof Card) {
            Card other = (Card) object;
            // 比較 Suit 和 Number
            return this.suit.equals(other.suit) && this.number.equals(other.number);
        }
        return false;
    }

	// 用於檢驗順子類的排序
	public static Comparator<Card> getComparatorWithValue() {
		return new Comparator<Card>() {
			@Override
			public int compare(Card a, Card b) {
				return a.getNumber().getValue() - b.getNumber().getValue();
			}
		};
	}

	// 同牌型且組合相同時
	// 比較兩張牌的大小關係
	public boolean greaterThan(Card other) {
		if (this.number.greaterThan(other.number)) {
			return true;
		}
		if (this.number.lessThan(other.number)) {
			return false;
		}
		return this.suit.greaterThan(other.suit);
	}

	// 根據 level 排序
	public static Comparator<Card> getComparatorWithLevel() {
		return new Comparator<Card>() {
			@Override
			public int compare(Card a, Card b) {
				if (a.greaterThan(b)) {
					return 1;
				}
				return -1;
			}
		};
	}
}
