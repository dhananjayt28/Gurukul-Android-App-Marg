package in.jivanmuktas.www.marg.model;

public class Chapter {
    String chapterName;
    String chapterId;

    public Chapter(String chapterName) {
        this.chapterName = chapterName;

    }

    public Chapter() {
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
}
