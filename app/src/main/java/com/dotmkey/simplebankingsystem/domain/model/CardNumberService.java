package com.dotmkey.simplebankingsystem.domain.model;

import java.util.Arrays;
import java.util.Random;

public class CardNumberService {

    public String generate() {
        var random = new Random();
        var withoutCheckSum = "400000".concat(String.valueOf(random.nextInt(999999999 - 100000000 + 1) + 100000000));

        var sum = 0;
        for (var i = 0; i < withoutCheckSum.length(); i++) {
            var digit = Integer.parseInt("" + withoutCheckSum.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
                digit = digit > 9 ? (digit / 10) + (digit % 10) : digit;
            }
            sum += digit;
        }
        var mod = sum % 10;

        return withoutCheckSum.concat(String.valueOf(mod == 0 ? 0 : 10 - mod));
    }

    public boolean isValid(String cardNumber) {
        if (cardNumber.length() < 16) {
            return false;
        }

        var digits = new int[cardNumber.length()];
        for (var i = 0; i < cardNumber.length(); i++) {
            digits[i] = Integer.parseInt("" + cardNumber.charAt(i));
        }

        for (var i = digits.length - 2; i >= 0; i -= 2) {
            var num = digits[i] * 2;
            digits[i] = num > 9 ? num % 10 + num / 10 : num;
        }

        var sum = Arrays.stream(digits).sum();

        return sum % 10 == 0;
    }
}
