package com.schulte.table.data.model;

public class GuideItem {
    public final int iconRes;
    public final String title;
    public final String summary;
    public final String content;

    public GuideItem(int iconRes, String title, String summary, String content) {
        this.iconRes = iconRes;
        this.title = title;
        this.summary = summary;
        this.content = content;
    }
}
