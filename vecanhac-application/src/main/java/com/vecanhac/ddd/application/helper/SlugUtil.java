package com.vecanhac.ddd.application.helper;

import java.text.Normalizer;

public class SlugUtil {

    public static String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]+", "") // loại dấu
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // loại ký tự đặc biệt
                .replaceAll("\\s+", "-") // space → "-"
                .replaceAll("-+", "-")   // nhiều "-" → một "-"
                .replaceAll("^-|-$", ""); // bỏ "-" đầu/cuối
        return slug;
    }
}